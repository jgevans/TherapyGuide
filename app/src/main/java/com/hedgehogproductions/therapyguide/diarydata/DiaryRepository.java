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

    interface LoadDiaryEntryCallback {
        void onEntryLoaded(DiaryEntry diaryEntry);
    }


    void getDiary(@NonNull DiaryRepository.LoadDiaryCallback callback);

    void getDiaryEntry(long timestamp, @NonNull LoadDiaryEntryCallback callback);

    void saveDiaryEntry(@NonNull DiaryEntry entry);

    void updateDiaryEntry(@NonNull DiaryEntry entry) throws IndexOutOfBoundsException;

    void deleteDiaryEntry(@NonNull DiaryEntry entry) throws IndexOutOfBoundsException;

}