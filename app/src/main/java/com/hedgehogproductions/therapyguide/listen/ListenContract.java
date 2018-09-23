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

        void setTrackPurchaseButtonVisibility();

        void setTrackPrice(String price);
    }

    interface UserActionsListener {

        void tearDown();

        void handlePlayRequest();

        void handleStopRequest();

        void handleRestartRequest();

        void handleLoopRequest();

        void handlePurchaseTrackRequest();

        boolean isLooping();

        boolean isTrackPurchased();
    }
}
