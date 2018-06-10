package com.hedgehogproductions.therapyguide;

import android.content.Context;

import com.hedgehogproductions.therapyguide.diarydata.DiaryRepositories;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;
import com.hedgehogproductions.therapyguide.diarydata.DiaryServiceApiImpl;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessRepositories;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessRepository;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessServiceApiImpl;

public class Injection {
    public static DiaryRepository provideDiaryRepository(Context context) {
        return DiaryRepositories.getInMemoryRepoInstance(new DiaryServiceApiImpl(context));
    }

    public static KindnessRepository provideKindnessRepository(Context context) {
        return KindnessRepositories.getInMemoryRepoInstance(new KindnessServiceApiImpl(context));
    }
}
