package com.hedgehogproductions.therapyguide.editdiaryentry;


public interface EditDiaryEntryContract {
    interface View {

        void showDiaryView();

        void showEmptyEntryError();

        void showMissingEntryError();

        void showDiaryText(String text);

        void showDiaryEntryDeletionMessage();
    }

    interface UserActionsListener {

        void updateDiaryEntry(String text);

        void saveNewDiaryEntry(long timestamp, String text);

        void openDiaryEntry(long timestamp);

        void instigateDiaryEntryDeletion();

        void deleteDiaryEntry();

    }
}
