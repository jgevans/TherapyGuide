package com.hedgehogproductions.therapyguide.diarydata;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Fake implementation of {@link DiaryServiceApi} to inject a fake service in a hermetic test.
 */
public class FakeDiaryServiceApiImpl implements DiaryServiceApi {
    private static final List<DiaryEntry> DIARY_SERVICE_DATA = Lists.newArrayList(
            new DiaryEntry(System.currentTimeMillis(), "Entry One")
    );

    @Override
    public void getAllDiaryEntries(DiaryServiceCallback<List<DiaryEntry>> callback) {
        callback.onLoaded(DIARY_SERVICE_DATA);
    }

    @Override
    public void saveDiaryEntry(DiaryEntry entry) {
        DIARY_SERVICE_DATA.add(entry);
    }

}