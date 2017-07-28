package com.hedgehogproductions.therapyguide.listen;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.listenservice.ListenService;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListenPresenter implements ListenContract.UserActionsListener {

    private static final int MAX_BIND_WAIT_COUNT = 10;

    private final ListenContract.View mListenView;
    private final Context mContext;

    private ListenService mListenService;
    private boolean mListenServiceBound;

    public ListenPresenter( @NonNull ListenContract.View listenView, Context context ) {
        mListenView = checkNotNull(listenView, "listenView cannot be null");
        mContext = context;

        mListenService = null;
        mListenServiceBound = false;

        Intent listenServiceIntent = new Intent(mContext, ListenService.class);
        listenServiceIntent.putExtra(ListenService.TRACK, R.raw.track_1);
        mContext.startService(listenServiceIntent);
        mContext.bindService(listenServiceIntent, mListenServiceConnection, 0);
        //TODO Store looping preference as app data and reload on open

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
        // Wait for service to be bound
        int tryCount = 0;
        while(!mListenServiceBound && tryCount < MAX_BIND_WAIT_COUNT) {
            ++tryCount;
            try{Thread.sleep(100);}catch(InterruptedException ignored){}
        }
        //TODO Toggle looping status even if player not started
        if( !mListenServiceBound || null == mListenService ) {
            handlePlayerUnavailable();
        }
        else {
            mListenService.switchLooping();
            if (mListenService.isLooping()) {
                mListenView.showStopLoop();
                mListenView.showLoopMessage();
            } else {
                mListenView.showLoop();
            }
        }
    }

    @Override
    public void handlePlayerUnavailable() {
        mContext.unbindService(mListenServiceConnection);
        //TODO Show error message
        //TODO file bug report?
    }

    // Defines callbacks for service binding, passed to bindService()
    private ServiceConnection mListenServiceConnection = new ServiceConnection() {

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
