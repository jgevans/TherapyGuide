package com.hedgehogproductions.therapyguide.adddiaryentry;

import android.support.annotation.NonNull;

import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddDiaryEntryPresenter implements AddDiaryEntryContract.UserActionsListener {

    private final DiaryRepository mDiaryRepository;
    private final AddDiaryEntryContract.View mAddDiaryEntryView;

    public AddDiaryEntryPresenter(
            @NonNull DiaryRepository diaryRepository, @NonNull AddDiaryEntryContract.View diaryView ) {
        mDiaryRepository = checkNotNull(diaryRepository, "diaryRepository cannot be null");
        mAddDiaryEntryView = checkNotNull(diaryView, "diaryView cannot be null");
    }

    @Override
    public void saveNewDiaryEntry(long timestamp, String text) {
        DiaryEntry newDiaryEntry = new DiaryEntry(timestamp, text);
        mDiaryRepository.saveDiaryEntry(newDiaryEntry);
        mAddDiaryEntryView.showDiaryView();
    }
}
