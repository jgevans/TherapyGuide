package com.hedgehogproductions.therapyguide;

import android.content.Context;

import com.hedgehogproductions.therapyguide.diarydata.DiaryRepositories;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;
import com.hedgehogproductions.therapyguide.diarydata.DiaryServiceApiImpl;

public class Injection {
    public static DiaryRepository provideDiaryRepository(Context context) {
        return DiaryRepositories.getInMemoryRepoInstance(new DiaryServiceApiImpl(context));
    }
}
