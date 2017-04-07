package com.hedgehogproductions.therapyguide.diarydata;


import java.util.List;

public interface DiaryServiceApi {

    interface DiaryServiceCallback<T> {

        void onLoaded(T notes);
    }

    void getAllDiaryEntries(DiaryServiceCallback<List<DiaryEntry>> callback);
}
