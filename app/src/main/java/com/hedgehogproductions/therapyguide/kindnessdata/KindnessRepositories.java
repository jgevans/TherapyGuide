package com.hedgehogproductions.therapyguide.kindnessdata;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class KindnessRepositories {
    private KindnessRepositories() {}

    private static KindnessRepository repository = null;

    public synchronized static KindnessRepository getInMemoryRepoInstance(@NonNull KindnessServiceApi kindnessServiceApi) {
        checkNotNull(kindnessServiceApi);
        if (null == repository) {
            repository = new InMemoryKindnessRepository(kindnessServiceApi);
        }
        return repository;
    }

}
