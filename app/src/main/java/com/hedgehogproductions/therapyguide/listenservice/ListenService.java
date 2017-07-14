package com.hedgehogproductions.therapyguide.listenservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class ListenService extends Service implements ListenServiceContract {

    // Binder given to all clients for interacting with the service
    private final IBinder mBinder = new ListenServiceBinder();

    public static final String TRACK = "track_name";

    private MediaPlayer mMediaPlayer;

    private static final int mStartMode = START_NOT_STICKY;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(null == mMediaPlayer){
            initialisePlayer(intent.getIntExtra(TRACK, 0));
        }
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if( null == mMediaPlayer) {
            initialisePlayer(intent.getIntExtra(TRACK, 0));
        }
        return mBinder;
    }

    private void initialisePlayer( int track ) throws ExceptionInInitializerError {
        if(track == 0) {
            throw new ExceptionInInitializerError("No track to initialise the player with");
        }
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), track);
    }

    @Override
    public void play() throws RuntimeException {
        if (null != mMediaPlayer) {
            mMediaPlayer.start();
        } else {
            throw new RuntimeException("Media Player does not exist");
        }
    }

    @Override
    public void pause() throws RuntimeException {
        if (null != mMediaPlayer) {
            mMediaPlayer.pause();
        } else {
            throw new RuntimeException("Media Player does not exist");
        }
    }

    @Override
    public void restart() throws RuntimeException {
        if (null != mMediaPlayer) {
            mMediaPlayer.seekTo(0);
        } else {
            throw new RuntimeException("Media Player does not exist");
        }
    }

    @Override
    public void stop() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        stopSelf();
    }

    @Override
    public void switchLooping() throws RuntimeException {
        if (null != mMediaPlayer) {
            mMediaPlayer.setLooping(!mMediaPlayer.isLooping());
        } else {
            throw new RuntimeException("Media Player does not exist");
        }
    }

    @Override
    public boolean isPlaying() throws RuntimeException {
        if(null != mMediaPlayer) {
            return mMediaPlayer.isPlaying();
        } else {
            throw new RuntimeException("Media Player does not exist");
        }
    }

    @Override
    public boolean isLooping() throws RuntimeException {
        if (null != mMediaPlayer) {
            return mMediaPlayer.isLooping();
        } else {
            throw new RuntimeException("Media Player does not exist");
        }
    }

    @Override
    public int getCurrentPosition() throws RuntimeException {
        if(null != mMediaPlayer) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            throw new RuntimeException("Media Player does not exist");
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class ListenServiceBinder extends Binder {
        ListenService getService() {
            // Return this instance of ListenService so clients can call public methods
            return ListenService.this;
        }
    }
}
