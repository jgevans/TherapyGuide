package com.hedgehogproductions.therapyguide.diary;

import android.support.annotation.NonNull;

import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DiaryPresenter implements DiaryContract.UserActionsListener {

    private final DiaryRepository mDiaryRepository;
    private final DiaryContract.View mDiaryView;

    public DiaryPresenter(
            @NonNull DiaryRepository diaryRepository, @NonNull DiaryContract.View diaryView ) {
        mDiaryRepository = checkNotNull(diaryRepository, "diaryRepository cannot be null");
        mDiaryView = checkNotNull(diaryView, "diaryView cannot be null");
    }

    @Override
    public void loadDiary() {

        mDiaryRepository.getDiary(new DiaryRepository.LoadDiaryCallback() {
            @Override
            public void onDiaryLoaded(List<DiaryEntry> diary) {
                mDiaryView.showDiary(diary);
            }
        });
    }

    @Override
    public void addNewDiaryEntry() {
        mDiaryView.showAddDiaryEntry();
    }

    @Override
    public void instigateDiaryEntryDeletion(final int position) {
        mDiaryView.showDiaryEntryDeletionMessage(position);
    }

    @Override
    public void deleteDiaryEntry(DiaryEntry entry) {
        mDiaryRepository.deleteDiaryEntry(entry);
    }
}
