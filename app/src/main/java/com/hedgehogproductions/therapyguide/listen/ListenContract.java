package com.hedgehogproductions.therapyguide.listen;


import android.media.MediaPlayer;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ListenContract {
    interface View {

        MediaPlayer getNewMediaPlayer();

        void showPlay();

        void showPause();

        void showLoop();

        void showStopLoop();

        void showLoopMessage();
    }

    interface UserActionsListener {

        void handlePlayRequest();

        void handleStopRequest();

        void handleRestartRequest();

        void handleLoopRequest();

        void pausePlayer();
    }
}
