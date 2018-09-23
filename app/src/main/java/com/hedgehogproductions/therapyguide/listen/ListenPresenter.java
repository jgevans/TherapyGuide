package com.hedgehogproductions.therapyguide.listen;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.billing.BillingManager;
import com.hedgehogproductions.therapyguide.billing.SimpleBillingManager;
import com.hedgehogproductions.therapyguide.listenservice.ListenService;
import com.hedgehogproductions.therapyguide.listenservice.ListenServiceContract;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hedgehogproductions.therapyguide.MainActivity.PREFERENCES;

public class ListenPresenter implements ListenContract.UserActionsListener {

    private static final int MAX_BIND_WAIT_COUNT = 10;
    public static final String LOOPING_PREF = "looping";

    private final ListenContract.View mListenView;
    private final Context mContext;

    private ListenServiceContract mListenService;
    private boolean mListenServiceBound;

    private BillingManager mBillingManager;

    private boolean mLooping;

    public ListenPresenter( @NonNull ListenContract.View listenView, Context context ) {
        mListenView = checkNotNull(listenView, "listenView cannot be null");
        mContext = context;

        mListenService = null;
        mListenServiceBound = false;

        mBillingManager = new SimpleBillingManager((Activity)context, new TrackStatusUpdatedListener() {
            @Override
            public void onTrackStatusUpdated() {
                mListenView.setTrackPurchaseButtonVisibility();
                mListenView.setTrackPrice(mBillingManager.getTrackCost());
            }
        });

        // Restore looping preference
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        mLooping = settings.getBoolean(LOOPING_PREF, false);

        Intent listenServiceIntent = new Intent(mContext, ListenService.class);
        listenServiceIntent.putExtra(ListenService.TRACK, R.raw.track_1);
        listenServiceIntent.putExtra(ListenService.LOOPING, mLooping);
        mContext.startService(listenServiceIntent);
        mContext.bindService(listenServiceIntent, mListenServiceConnection, 0);
    }

    @Override
    public void tearDown() {
        if(mListenServiceBound) {
            mContext.unbindService(mListenServiceConnection);
        }
    }

    @Override
    public void handlePlayRequest() {
        // Wait for service to be bound
        int tryCount = 0;
        while(!mListenServiceBound && tryCount < MAX_BIND_WAIT_COUNT) {
            ++tryCount;
            try{Thread.sleep(100);}catch(InterruptedException ignored){}
        }
        if( !mListenServiceBound || null == mListenService ) {
            handlePlayerUnavailable();
        }
        else {
            if (mListenService.isPlaying()) {
                mListenService.pause();
                mListenView.showPlay();

            } else {
                mListenService.play();
                mListenView.showPause();
                // Create and register a broadcast receiver to handle
                // when the service finishes playback
                BroadcastReceiver playbackCompleteBroadcastReceiver = new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context content, Intent intent) {
                        mListenView.showPlay();
                    }
                };
                IntentFilter filter =
                        new IntentFilter(ListenService.FINISHED_PLAYBACK_NOTIFICATION);
                LocalBroadcastManager.getInstance(mContext)
                        .registerReceiver(playbackCompleteBroadcastReceiver, filter);
            }
        }
    }

    @Override
    public void handleStopRequest() {
        // Wait for service to be bound
        int tryCount = 0;
        while(!mListenServiceBound && tryCount < MAX_BIND_WAIT_COUNT) {
            ++tryCount;
            try{Thread.sleep(100);}catch(InterruptedException ignored){}
        }
        if( !mListenServiceBound || null == mListenService ) {
            handlePlayerUnavailable();
        }
        else {
            mListenService.stop();
            mListenView.showPlay();
        }
    }

    @Override
    public void handleRestartRequest() {
        // Wait for service to be bound
        int tryCount = 0;
        while(!mListenServiceBound && tryCount < MAX_BIND_WAIT_COUNT) {
            ++tryCount;
            try{Thread.sleep(100);}catch(InterruptedException ignored){}
        }
        if( !mListenServiceBound || null == mListenService ) {
            handlePlayerUnavailable();
        }
        else {
            mListenService.restart();
        }
    }

    @Override
    public void handleLoopRequest() {
        // Switch looping state and store in preferences
        //   regardless of the status of the ListenService
        mLooping = !mLooping;

        SharedPreferences settings = mContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(LOOPING_PREF, mLooping);
        editor.apply();


        // Wait for service to be bound
        int tryCount = 0;
        while(!mListenServiceBound && tryCount < MAX_BIND_WAIT_COUNT) {
            ++tryCount;
            try{Thread.sleep(100);}catch(InterruptedException ignored){}
        }

        if( !mListenServiceBound || null == mListenService ) {
            handlePlayerUnavailable();
        }
        else {
            mListenService.setLooping(mLooping);
            if (mLooping) {
                mListenView.showStopLoop();
                mListenView.showLoopMessage();
            } else {
                mListenView.showLoop();
            }
        }
    }

    @Override
    public void handlePurchaseTrackRequest() {
        mBillingManager.purchaseTrack();
        mListenView.setTrackPurchaseButtonVisibility();
    }

    @Override
    public boolean isLooping() {
        return mLooping;
    }

    @Override
    public boolean isTrackPurchased() {
        return mBillingManager.isTrackPurchased();
    }

    private void handlePlayerUnavailable() {
        mContext.unbindService(mListenServiceConnection);
        //TODO Show error message
        //TODO file bug report?
    }

    // Defines callbacks for service binding, passed to bindService()
    private final ServiceConnection mListenServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to ListenService, cast the IBinder and get ListenService instance
            ListenService.ListenServiceBinder binder = (ListenService.ListenServiceBinder) service;
            mListenService = binder.getService();
            mListenServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mListenService = null;
            mListenServiceBound = false;
        }
    };
}
