package com.hedgehogproductions.therapyguide.editdiaryentry;


interface EditDiaryEntryContract {
    interface View {

        void showDiaryView();

        void showEmptyEntryError();

        void showMissingEntryError();

        void showDiaryText(String text1, String text2, String text3, String text4, String text5);

        void showDiaryEntryDeletionMessage();
    }

    interface UserActionsListener {

        void updateDiaryEntry(String text1, String text2, String text3, String text4, String text5);

        void saveNewDiaryEntry(
                long timestamp, String text1, String text2, String text3, String text4, String text5);

        void openDiaryEntry(long timestamp);

        void instigateDiaryEntryDeletion();

        void deleteDiaryEntry();

    }
}
