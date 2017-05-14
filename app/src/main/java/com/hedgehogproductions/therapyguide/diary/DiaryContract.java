package com.hedgehogproductions.therapyguide.diary;

import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DiaryContract {

    interface View {

        void showDiary(List<DiaryEntry> entries);

        void showAddDiaryEntry();

        void showDiaryEntryDeletionMessage(final int position);
    }

    interface UserActionsListener {

        void loadDiary();

        void addNewDiaryEntry();

        void deleteDiaryEntry(final int position);
    }
}