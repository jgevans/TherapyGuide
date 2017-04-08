package com.hedgehogproductions.therapyguide.diarydata;


import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class DiaryRepositories {

    private DiaryRepositories() {}

    private static DiaryRepository repository = null;

    public synchronized static DiaryRepository getInMemoryRepoInstance(@NonNull DiaryServiceApi diaryServiceApi) {
        checkNotNull(diaryServiceApi);
        if (null == repository) {
            repository = new InMemoryDiaryRepository(diaryServiceApi);
        }
        return repository;
    }
}