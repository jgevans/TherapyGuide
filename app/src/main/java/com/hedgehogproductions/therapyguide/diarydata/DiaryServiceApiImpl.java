package com.hedgehogproductions.therapyguide.diarydata;


import java.util.ArrayList;
import java.util.List;

public class DiaryServiceApiImpl implements DiaryServiceApi {

    @Override
    public void getAllDiaryEntries(final DiaryServiceCallback callback) {
        List<DiaryEntry> entries = new ArrayList<>();
        callback.onLoaded(entries);
    }
}
