package com.hedgehogproductions.therapyguide.listenservice;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.hedgehogproductions.therapyguide.notifications.NotificationHandler;

public class ListenService extends Service implements ListenServiceContract {

    public static final String FINISHED_PLAYBACK_NOTIFICATION = "com.hedgehogproductions.therapyguide.broadcast.FINISHED_PLAYBACK_NOTIFICATION";

    public static final String TRACK = "track_name";
    public static final String LOOPING = "looping_state";

    private static final int PLAYER_NOTIFICATION_ID = 1;
    private static final int mStartMode = START_NOT_STICKY;

    // Binder given to all clients for interacting with the service
    private final ListenServiceBinder mBinder = new ListenServiceBinder();

    private Notification mPlayerNotification;
    private MediaPlayer mMediaPlayer;
    private int mTrack;
    private boolean mLooping;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Set up the notification for the ListenService
        mPlayerNotification = NotificationHandler.getListenPlayerNotification(this);

        mTrack = intent.getIntExtra(TRACK, 0);
        mLooping = intent.getBooleanExtra(LOOPING, false);

        if(null == mMediaPlayer) {
            mMediaPlayer =
                    MediaPlayer.create(getApplicationContext(), mTrack);
            mMediaPlayer.setLooping(mLooping);
        }
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        if(null != mMediaPlayer) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
    }

    @Override
    public void play() {
        if (null == mMediaPlayer) {
            mMediaPlayer =
                    MediaPlayer.create(getApplicationContext(), mTrack);
            mMediaPlayer.setLooping(mLooping);
        }
        startForeground(PLAYER_NOTIFICATION_ID, mPlayerNotification);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer player) {
                stopForeground(true);
                if(null != mMediaPlayer) {
                    mMediaPlayer.release();
                }
                mMediaPlayer = null;
                // Broadcast a notification so the app knows playback has completed
                Intent intent = new Intent();
                intent.setAction(FINISHED_PLAYBACK_NOTIFICATION);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                stopForeground(true);
            }

        });
    }

    @Override
    public void pause() {
        if (null == mMediaPlayer) {
            mMediaPlayer =
                    MediaPlayer.create(getApplicationContext(), mTrack);
            mMediaPlayer.setLooping(mLooping);
        }
        mMediaPlayer.pause();
    }

    @Override
    public void restart() {
        if (null == mMediaPlayer) {
            mMediaPlayer =
                    MediaPlayer.create(getApplicationContext(), mTrack);
            mMediaPlayer.setLooping(mLooping);
        }
        mMediaPlayer.seekTo(0);
    }

    @Override
    public void stop() {
        if (null != mMediaPlayer) {
            stopForeground(true);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void setLooping(boolean looping) throws RuntimeException {
        mLooping = looping;
        if (null != mMediaPlayer) {
            mMediaPlayer.setLooping(mLooping);
        }
    }

    @Override
    public boolean isPlaying() throws RuntimeException {
        return null != mMediaPlayer && mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isLooping() throws RuntimeException {
        return null != mMediaPlayer && mMediaPlayer.isLooping();
    }

    @Override
    public int getCurrentPosition() throws RuntimeException {
        if(null != mMediaPlayer) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class ListenServiceBinder extends Binder {
        public ListenService getService() {
            // Return this instance of ListenService so clients can call public methods
            return ListenService.this;
        }
    }
}
