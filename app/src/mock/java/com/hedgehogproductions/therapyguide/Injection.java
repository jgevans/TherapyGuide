package com.hedgehogproductions.therapyguide;

import android.content.Context;

import com.hedgehogproductions.therapyguide.diarydata.DiaryRepositories;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;
import com.hedgehogproductions.therapyguide.diarydata.FakeDiaryServiceApiImpl;
import com.hedgehogproductions.therapyguide.kindnessdata.FakeKindnessServiceApiImpl;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessRepositories;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessRepository;

/**
 * Enables injection of mock implementation for {@link DiaryRepository}
 *  at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static DiaryRepository provideDiaryRepository(Context context) {
        return DiaryRepositories.getInMemoryRepoInstance(new FakeDiaryServiceApiImpl());
    }

    public static KindnessRepository provideKindnessRepository(Context context) {
        return KindnessRepositories.getInMemoryRepoInstance(new FakeKindnessServiceApiImpl());
    }
}
