package com.hedgehogproductions.therapyguide.diarydata;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Fake implementation of {@link DiaryServiceApi} to inject a fake service in a hermetic test.
 */
public class FakeDiaryServiceApiImpl implements DiaryServiceApi {
    private static final List<DiaryEntry> DIARY_SERVICE_DATA = Lists.newArrayList();

    @Override
    public void getAllDiaryEntries(DiaryServiceCallback<List<DiaryEntry>> callback) {
        callback.onLoaded(DIARY_SERVICE_DATA);
    }

    @Override
    public void getDiaryEntry(long timestamp, DiaryServiceCallback<DiaryEntry> callback) {
        DiaryEntry entry = null;
        // TODO Search list and set entry, if found
        callback.onLoaded(entry);
    }

    @Override
    public void saveDiaryEntry(DiaryEntry entry) {
        DIARY_SERVICE_DATA.add(entry);
    }

    @Override
    public void updateDiaryEntry(DiaryEntry entry) {
        // TODO Find and update entry
    }

    @Override
    public void deleteDiaryEntry(DiaryEntry entry) { DIARY_SERVICE_DATA.remove(entry); }

}