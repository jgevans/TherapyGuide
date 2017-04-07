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

    }

    interface UserActionsListener {

        void loadDiary();

        void addNewDiaryEntry();
    }
}