package com.hedgehogproductions.therapyguide;


import com.hedgehogproductions.therapyguide.diarydata.DiaryRepositories;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;
import com.hedgehogproductions.therapyguide.diarydata.DiaryServiceApiImpl;

public class Injection {
    public static DiaryRepository provideDiaryRepository() {
        return DiaryRepositories.getInMemoryRepoInstance(new DiaryServiceApiImpl());
    }
}
