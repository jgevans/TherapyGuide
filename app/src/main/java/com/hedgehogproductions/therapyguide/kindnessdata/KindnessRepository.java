package com.hedgehogproductions.therapyguide.kindnessdata;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

/**
 * Main entry point for accessing kindness data.
 */
public interface KindnessRepository {
    interface LoadKindnessCallback {
        void onKindnessLoaded(List<KindnessEntry> kindnessEntries);
    }

    interface LoadKindnessEntryCallback {
        void onEntryLoaded(KindnessEntry kindnessEntry);
    }


    void getKindnessDiary(@NonNull KindnessRepository.LoadKindnessCallback callback);

    void getKindnessEntry(Date date, @NonNull LoadKindnessEntryCallback callback);

    void saveKindnessEntry(@NonNull KindnessEntry entry);

    void updateKindnessEntry(@NonNull KindnessEntry entry) throws IndexOutOfBoundsException;

    void updateKindnessEntryCompleteness(@NonNull KindnessEntry entry) throws IndexOutOfBoundsException;

    void deleteKindnessEntry(@NonNull KindnessEntry entry) throws IndexOutOfBoundsException;

}
