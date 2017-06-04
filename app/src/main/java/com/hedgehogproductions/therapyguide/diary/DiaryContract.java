package com.hedgehogproductions.therapyguide.diary;

import android.support.annotation.NonNull;

import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DiaryContract {

    interface View {

        void showDiary(List<DiaryEntry> entries);

        void showAddDiaryEntry();

        void showUpdateDiaryEntry(long selectedEntryTimestamp);

        void showDiaryEntryDeletionMessage(final int position);
    }

    interface UserActionsListener {

        void loadDiary();

        void addNewDiaryEntry();

        void updateDiaryEntry(@NonNull DiaryEntry selectedEntry);

        void instigateDiaryEntryDeletion(final int position);

        void deleteDiaryEntry(DiaryEntry entry);
    }
}