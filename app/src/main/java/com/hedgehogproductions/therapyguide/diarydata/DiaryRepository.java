package com.hedgehogproductions.therapyguide.diarydata;


import android.support.annotation.NonNull;

import java.util.List;

/**
 * Main entry point for accessing diary data.
 */
public interface DiaryRepository {
    interface LoadDiaryCallback {

        void onDiaryLoaded(List<DiaryEntry> diaryEntries);
    }


    void getDiary(@NonNull DiaryRepository.LoadDiaryCallback callback);

    void refreshData();

}