package com.hedgehogproductions.therapyguide.listen;



/**
 * This specifies the contract between the view and the presenter.
 */
interface ListenContract {
    interface View {

        void showPlay();

        void showPause();

        void showLoop();

        void showStopLoop();

        void showLoopMessage();
    }

    interface UserActionsListener {

        void tearDown();

        void handlePlayRequest();

        void handleStopRequest();

        void handleRestartRequest();

        void handleLoopRequest();

        boolean isLooping();
    }
}
