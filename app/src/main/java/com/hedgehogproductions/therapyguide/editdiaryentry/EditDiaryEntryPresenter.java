package com.hedgehogproductions.therapyguide.editdiaryentry;


import android.support.annotation.NonNull;

import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;
import com.hedgehogproductions.therapyguide.diarydata.DiaryRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditDiaryEntryPresenter implements EditDiaryEntryContract.UserActionsListener {

    private final DiaryRepository mDiaryRepository;
    private final EditDiaryEntryContract.View mEditDiaryEntryView;
    private long mCreationTimestamp;

    public EditDiaryEntryPresenter(
            @NonNull DiaryRepository diaryRepository, @NonNull EditDiaryEntryContract.View editDiaryEntryView ) {
        mDiaryRepository = checkNotNull(diaryRepository, "diary repository cannot be null");
        mEditDiaryEntryView = checkNotNull(editDiaryEntryView, "edit view cannot be null");
        mCreationTimestamp = ~0;
    }

    @Override
    public void openDiaryEntry(long timestamp) {

        mDiaryRepository.getDiaryEntry(timestamp, new DiaryRepository.LoadDiaryEntryCallback() {
            @Override
            public void onEntryLoaded(DiaryEntry diaryEntry) {
                if (null == diaryEntry) {
                    mEditDiaryEntryView.showMissingEntryError();
                } else {
                    showDiaryEntry(diaryEntry);
                    setCreationTimestamp(diaryEntry.getCreationTimestamp());
                }
            }
        });
    }

    void setCreationTimestamp(long timestamp) {
        mCreationTimestamp = timestamp;
    }

    private void showDiaryEntry(DiaryEntry entry) {
        String text = entry.getText();
        mEditDiaryEntryView.showDiaryText(text);
    }

    @Override
    public void updateDiaryEntry(String text) {
        DiaryEntry newDiaryEntry = new DiaryEntry(mCreationTimestamp, text);
        if( newDiaryEntry.isEmpty() ) {
            mEditDiaryEntryView.showEmptyEntryError();
        }
        else {
            mDiaryRepository.updateDiaryEntry(newDiaryEntry);
            mEditDiaryEntryView.showDiaryView();
        }
    }

    @Override
    public void saveNewDiaryEntry(long timestamp, String text) {
        DiaryEntry newDiaryEntry = new DiaryEntry(timestamp, text);
        if( newDiaryEntry.isEmpty() ) {
            mEditDiaryEntryView.showEmptyEntryError();
        }
        else {
            mDiaryRepository.saveDiaryEntry(newDiaryEntry);
            mEditDiaryEntryView.showDiaryView();
        }
    }

    @Override
    public void instigateDiaryEntryDeletion() {
        mEditDiaryEntryView.showDiaryEntryDeletionMessage();
    }

    @Override
    public void deleteDiaryEntry() {
        mDiaryRepository.getDiaryEntry(mCreationTimestamp, new DiaryRepository.LoadDiaryEntryCallback() {
            @Override
            public void onEntryLoaded(DiaryEntry diaryEntry) {
                mDiaryRepository.deleteDiaryEntry(diaryEntry);
            }
        });
    }
}
