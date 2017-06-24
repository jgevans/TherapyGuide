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

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Override
    public void getDiaryEntry(long timestamp, DiaryServiceCallback<DiaryEntry> callback) {
        DiaryEntry foundEntry = null;
        for( DiaryEntry entry : DIARY_SERVICE_DATA) {
            if( entry.equals(timestamp) ) {
                foundEntry = entry;
                break;
            }
        }
        callback.onLoaded(foundEntry);
    }

    @Override
    public void saveDiaryEntry(DiaryEntry entry) {
        DIARY_SERVICE_DATA.add(entry);
    }

    @Override
    public void updateDiaryEntry(DiaryEntry entry) {
        int indexOfEntry = DIARY_SERVICE_DATA.indexOf(entry);
        if( indexOfEntry == -1 ) {
            throw new ArrayIndexOutOfBoundsException("Entry to update not found");
        }
        else {
            DIARY_SERVICE_DATA.set(indexOfEntry, entry);
        }
    }

    @Override
    public void deleteDiaryEntry(DiaryEntry entry) {
        DIARY_SERVICE_DATA.remove(entry);
    }

}