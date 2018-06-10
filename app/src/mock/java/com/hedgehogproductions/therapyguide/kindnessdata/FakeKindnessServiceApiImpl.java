package com.hedgehogproductions.therapyguide.kindnessdata;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Fake implementation of {@link KindnessServiceApi} to inject a fake service in a hermetic test.
 */
public class FakeKindnessServiceApiImpl implements KindnessServiceApi {
    private static final List<KindnessEntry> KINDNESS_SERVICE_DATA = Lists.newArrayList();

    @Override
    public void getAllKindnessEntries(KindnessServiceCallback<List<KindnessEntry>> callback) {
        callback.onLoaded(KINDNESS_SERVICE_DATA);
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Override
    public void getKindnessEntry(long timestamp, KindnessServiceCallback<KindnessEntry> callback) {
        KindnessEntry foundEntry = null;
        for( KindnessEntry entry : KINDNESS_SERVICE_DATA) {
            if( entry.equals(timestamp) ) {
                foundEntry = entry;
                break;
            }
        }
        callback.onLoaded(foundEntry);
    }

    @Override
    public void saveKindnessEntry(KindnessEntry entry) {
        KINDNESS_SERVICE_DATA.add(entry);
    }

    @Override
    public void updateKindnessEntry(KindnessEntry entry) {
        int indexOfEntry = KINDNESS_SERVICE_DATA.indexOf(entry);
        if( indexOfEntry == -1 ) {
            throw new ArrayIndexOutOfBoundsException("Entry to update not found");
        }
        else {
            KINDNESS_SERVICE_DATA.set(indexOfEntry, entry);
        }
    }

    @Override
    public void deleteKindnessEntry(KindnessEntry entry) {
        KINDNESS_SERVICE_DATA.remove(entry);
    }

}