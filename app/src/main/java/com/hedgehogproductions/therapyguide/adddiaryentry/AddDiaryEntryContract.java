package com.hedgehogproductions.therapyguide.adddiaryentry;


public interface AddDiaryEntryContract {
    interface View {

        void showDiaryView();
    }

    interface UserActionsListener {

        void saveNewDiaryEntry(long timestamp, String text);

    }
}
