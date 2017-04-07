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
    public void refreshData() {
        mCachedEntries = null;
    }
}
