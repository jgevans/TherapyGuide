package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.support.annotation.NonNull;

import com.hedgehogproductions.therapyguide.kindnessdata.KindnessCategories;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessRepository;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditKindnessEntryPresenter implements EditKindnessEntryContract.UserActionsListener {

    private final KindnessRepository mKindnessRepository;
    private final EditKindnessEntryContract.View mEditKindnessEntryView;
    private KindnessEntry mKindnessEntry;
    private Date mCreationDate;
    private boolean mComplete;

    EditKindnessEntryPresenter(
            @NonNull KindnessRepository kindnessRepository, @NonNull EditKindnessEntryContract.View editKindnessEntryView ) {
        mKindnessRepository = checkNotNull(kindnessRepository, "kindness repository cannot be null");
        mEditKindnessEntryView = checkNotNull(editKindnessEntryView, "edit view cannot be null");
        mKindnessEntry = new KindnessEntry();
        mCreationDate = null;
        mComplete = false;
    }

    @Override
    public void openKindnessEntry(Date date) {

        mKindnessRepository.getKindnessEntry(date, new KindnessRepository.LoadKindnessEntryCallback() {
            @Override
            public void onEntryLoaded(KindnessEntry kindnessEntry) {
                if (null == kindnessEntry) {
                    mEditKindnessEntryView.showMissingEntryError();
                } else {
                    setKindnessEntry(kindnessEntry);
                    setCreationDate(kindnessEntry.getCreationDate());
                    setCompleteness(kindnessEntry.isComplete());
                }
            }
        });
    }

    void setCreationDate(Date date) {
        mCreationDate = date;
    }

    private void setCompleteness(boolean complete) {
        mComplete = complete;
    }

    private void setKindnessEntry(KindnessEntry entry) {
        mKindnessEntry = entry;
    }

    @Override
    public KindnessEntry getKindnessEntry() {
        return mKindnessEntry;
    }

    @Override
    public void updateKindnessEntry(KindnessCategories category, String value) {
        KindnessEntry newKindnessEntry =
                new KindnessEntry(mCreationDate, category, value, mComplete);
        if( newKindnessEntry.isEmpty() ) {
            mEditKindnessEntryView.showEmptyEntryError();
        }
        else {
            mKindnessRepository.updateKindnessEntry(newKindnessEntry);
            setKindnessEntry(newKindnessEntry);
            setCreationDate(newKindnessEntry.getCreationDate());
            setCompleteness(newKindnessEntry.isComplete());
            mEditKindnessEntryView.showKindnessView();
        }
    }

    @Override
    public void saveNewKindnessEntry(Date date, KindnessCategories category, String value) {
        KindnessEntry newKindnessEntry = new KindnessEntry(date, category, value);
        if( newKindnessEntry.isEmpty() ) {
            mEditKindnessEntryView.showEmptyEntryError();
        }
        else {
            mKindnessRepository.saveKindnessEntry(newKindnessEntry);
            setKindnessEntry(newKindnessEntry);
            setCreationDate(newKindnessEntry.getCreationDate());
            setCompleteness(newKindnessEntry.isComplete());
            mEditKindnessEntryView.showKindnessView();
        }
    }

    @Override
    public void deleteKindnessEntry() {
        mKindnessRepository.getKindnessEntry(mCreationDate, new KindnessRepository.LoadKindnessEntryCallback() {
            @Override
            public void onEntryLoaded(KindnessEntry kindnessEntry) {
                mKindnessRepository.deleteKindnessEntry(kindnessEntry);
            }
        });
    }
}
