package com.hedgehogproductions.therapyguide.kindnessdata;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class InMemoryKindnessRepository implements KindnessRepository {

    private final KindnessServiceApi mKindnessServiceApi;

    @VisibleForTesting
    List<KindnessEntry> mCachedEntries;

    // Invalidate but don't delete cache
    private boolean cacheValid = true;

    public InMemoryKindnessRepository(@NonNull KindnessServiceApi kindnessServiceApi) {
        mKindnessServiceApi = checkNotNull(kindnessServiceApi);
    }

    @Override
    public void getKindnessDiary(@NonNull final LoadKindnessCallback callback) {
        checkNotNull(callback);
        // Load from API only if needed.
        if (mCachedEntries == null || !cacheValid) {
            mKindnessServiceApi.getAllKindnessEntries(new KindnessServiceApi.KindnessServiceCallback<List<KindnessEntry>>() {
                @Override
                public void onLoaded(List<KindnessEntry> notes) {
                    mCachedEntries = ImmutableList.copyOf(notes);
                    callback.onKindnessLoaded(mCachedEntries);
                }
            });
            cacheValid = true;
        } else {
            callback.onKindnessLoaded(mCachedEntries);
        }
    }

    @Override
    public void getKindnessEntry(long timestamp, @NonNull final LoadKindnessEntryCallback callback) {
        checkNotNull(callback);
        // Load entry from API.
        mKindnessServiceApi.getKindnessEntry(timestamp, new KindnessServiceApi.KindnessServiceCallback<KindnessEntry>() {
            @Override
            public void onLoaded(KindnessEntry entry) {
                callback.onEntryLoaded(entry);
            }
        });
    }

    @Override
    public void saveKindnessEntry(@NonNull KindnessEntry entry) {
        checkNotNull(entry);
        mKindnessServiceApi.saveKindnessEntry(entry);
        refreshData();
    }

    @Override
    public void updateKindnessEntry(@NonNull KindnessEntry entry) throws IndexOutOfBoundsException {
        checkNotNull(entry);
        if( !mCachedEntries.contains(entry) ) {
            throw new IndexOutOfBoundsException("KindnessEntry not found");
        }
        mKindnessServiceApi.updateKindnessEntry(entry);
        refreshData();
    }

    @Override
    public void updateKindnessEntryCompleteness(@NonNull KindnessEntry entry) throws IndexOutOfBoundsException {
        checkNotNull(entry);
        if( !mCachedEntries.contains(entry) ) {
            throw new IndexOutOfBoundsException("KindnessEntry not found");
        }
        mKindnessServiceApi.updateKindnessEntry(entry);
        cacheValid = false;
    }

    @Override
    public void deleteKindnessEntry(@NonNull KindnessEntry entry) throws IndexOutOfBoundsException {
        checkNotNull(entry);
        if( !mCachedEntries.contains(entry) ) {
            throw new IndexOutOfBoundsException("KindnessEntry not found");
        }
        mKindnessServiceApi.deleteKindnessEntry(entry);
        refreshData();
    }

    public void refreshData() {
        mCachedEntries = null;
    }
}
