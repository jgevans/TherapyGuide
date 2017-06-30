package com.hedgehogproductions.therapyguide.listen;


import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListenPresenter implements ListenContract.UserActionsListener {

    private final ListenContract.View mListenView;
    private MediaPlayer mMediaPlayer;

    private boolean mLooping;

    public ListenPresenter( @NonNull ListenContract.View listenView ) {
        mListenView = checkNotNull(listenView, "listenView cannot be null");

        // TODO store as app config for persistence
        mLooping = false;
    }

    @Override
    public void handlePlayRequest() {
        if (null == mMediaPlayer) {
            setMediaPlayer(mListenView.getNewMediaPlayer());
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mListenView.showPlay();

        } else {
            mMediaPlayer.start();
            mListenView.showPause();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer player) {
                    player.release();
                    mListenView.showPlay();
                    mMediaPlayer = null;
                }

            });
        }
    }

    @Override
    public void handleStopRequest() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mListenView.showPlay();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void handleRestartRequest() {
        if (null != mMediaPlayer) {
            mMediaPlayer.seekTo(0);
        }
    }

    @Override
    public void handleLoopRequest() {
        mLooping = !mLooping;
        if (null != mMediaPlayer) {
            mMediaPlayer.setLooping(mLooping);
        }
        if (mLooping) {
            mListenView.showStopLoop();
            mListenView.showLoopMessage();
        } else {
            mListenView.showLoop();
        }
    }

    @Override
    public void pausePlayer() {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mListenView.showPlay();
        }
    }

    // Allows injection for testing
    void setMediaPlayer(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }

    // Available for testing
    boolean isLooping() {
        return mLooping;
    }
}
