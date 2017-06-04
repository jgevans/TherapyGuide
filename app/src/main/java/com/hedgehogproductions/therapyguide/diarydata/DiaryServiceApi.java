package com.hedgehogproductions.therapyguide.diarydata;


import java.util.List;

interface DiaryServiceApi {

    interface DiaryServiceCallback<T> {

        void onLoaded(T diary);
    }

    void getAllDiaryEntries(DiaryServiceCallback<List<DiaryEntry>> callback);

    void getDiaryEntry(long timestamp, DiaryServiceCallback<DiaryEntry> callback);

    void saveDiaryEntry(DiaryEntry entry);

    void updateDiaryEntry(DiaryEntry entry);

    void deleteDiaryEntry(DiaryEntry entry);
}
