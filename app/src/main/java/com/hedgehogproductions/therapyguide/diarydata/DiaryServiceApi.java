package com.hedgehogproductions.therapyguide.diarydata;


import java.util.List;

interface DiaryServiceApi {

    interface DiaryServiceCallback<T> {

        void onLoaded(T notes);
    }

    void getAllDiaryEntries(DiaryServiceCallback<List<DiaryEntry>> callback);

    void saveDiaryEntry(DiaryEntry entry);

    void deleteDiaryEntry(DiaryEntry entry);
}
