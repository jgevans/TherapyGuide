package com.hedgehogproductions.therapyguide.kindnessdata;

import java.util.List;

interface KindnessServiceApi {

    interface KindnessServiceCallback<T> {

        void onLoaded(T diary);
    }

    void getAllKindnessEntries(KindnessServiceCallback<List<KindnessEntry>> callback);

    void getKindnessEntry(long timestamp, KindnessServiceCallback<KindnessEntry> callback);

    void saveKindnessEntry(KindnessEntry entry);

    void updateKindnessEntry(KindnessEntry entry);

    void deleteKindnessEntry(KindnessEntry entry);
}
