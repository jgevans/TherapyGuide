package com.hedgehogproductions.therapyguide.diarydata;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class InMemoryDiaryRepository implements DiaryRepository {

    private final DiaryServiceApi mDiaryServiceApi;

    @VisibleForTesting
    List<DiaryEntry> mCachedEntries;

    public InMemoryDiaryRepository(@NonNull DiaryServiceApi diaryServiceApi) {
        mDiaryServiceApi = checkNotNull(diaryServiceApi);
    }

    @Override
    public void getDiary(@NonNull final LoadDiaryCallback callback) {
        checkNotNull(callback);
        // Load from API only if needed.
        if (mCachedEntries == null) {
            mDiaryServiceApi.getAllDiaryEntries(new DiaryServiceApi.DiaryServiceCallback<List<DiaryEntry>>() {
                @Override
                public void onLoaded(List<DiaryEntry> notes) {
                    mCachedEntries = ImmutableList.copyOf(notes);
                    callback.onDiaryLoaded(mCachedEntries);
                }
            });
        } else {
            callback.onDiaryLoaded(mCachedEntries);
        }
    }

    @Override
    public void getDiaryEntry(long timestamp, @NonNull final LoadDiaryEntryCallback callback) {
        checkNotNull(callback);
        // Load entry from API.
        mDiaryServiceApi.getDiaryEntry(timestamp, new DiaryServiceApi.DiaryServiceCallback<DiaryEntry>() {
            @Override
            public void onLoaded(DiaryEntry entry) {
                callback.onEntryLoaded(entry);
            }
        });
    }

    @Override
    public void saveDiaryEntry(@NonNull DiaryEntry entry) {
        checkNotNull(entry);
        mDiaryServiceApi.saveDiaryEntry(entry);
        refreshData();
    }

    @Override
    public void updateDiaryEntry(@NonNull DiaryEntry entry) throws IndexOutOfBoundsException {
        checkNotNull(entry);
        if( !mCachedEntries.contains(entry) ) {
            throw new IndexOutOfBoundsException("DiaryEntry not found");
        }
        mDiaryServiceApi.updateDiaryEntry(entry);
        refreshData();
    }

    @Override
    public void deleteDiaryEntry(@NonNull DiaryEntry entry) throws IndexOutOfBoundsException {
        checkNotNull(entry);
        if( !mCachedEntries.contains(entry) ) {
            throw new IndexOutOfBoundsException("DiaryEntry not found");
        }
        mDiaryServiceApi.deleteDiaryEntry(entry);
        refreshData();
    }

    public void refreshData() {
        mCachedEntries = null;
    }
}
