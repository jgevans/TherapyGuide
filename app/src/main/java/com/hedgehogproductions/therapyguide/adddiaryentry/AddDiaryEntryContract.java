package com.hedgehogproductions.therapyguide.adddiaryentry;


public interface AddDiaryEntryContract {
    interface View {

        void showDiaryView();

        void showEmptyEntryError();

    }

    interface UserActionsListener {

        void saveNewDiaryEntry(long timestamp, String text);

    }
}
