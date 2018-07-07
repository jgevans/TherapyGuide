package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.support.annotation.NonNull;

import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessRepository;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditKindnessEntryPresenter implements EditKindnessEntryContract.UserActionsListener {

    private final KindnessRepository mKindnessRepository;
    private final EditKindnessEntryContract.View mEditKindnessEntryView;
    private KindnessEntry mKindnessEntry;
    private long mCreationTimestamp;
    private boolean mComplete;

    public EditKindnessEntryPresenter(
            @NonNull KindnessRepository kindnessRepository, @NonNull EditKindnessEntryContract.View editKindnessEntryView ) {
        mKindnessRepository = checkNotNull(kindnessRepository, "kindness repository cannot be null");
        mEditKindnessEntryView = checkNotNull(editKindnessEntryView, "edit view cannot be null");
        mKindnessEntry = new KindnessEntry();
        mCreationTimestamp = ~0;
        mComplete = false;
    }

    @Override
    public void openKindnessEntry(long timestamp) {

        mKindnessRepository.getKindnessEntry(timestamp, new KindnessRepository.LoadKindnessEntryCallback() {
            @Override
            public void onEntryLoaded(KindnessEntry kindnessEntry) {
                if (null == kindnessEntry) {
                    mEditKindnessEntryView.showMissingEntryError();
                } else {
                    setKindnessEntry(kindnessEntry);
                    setCreationTimestamp(kindnessEntry.getCreationTimestamp());
                    setCompleteness(kindnessEntry.isComplete());
                }
            }
        });
    }

    void setCreationTimestamp(long timestamp) {
        mCreationTimestamp = timestamp;
    }

    void setCompleteness(boolean complete) {
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
    public void updateKindnessEntry(KindnessWords words, KindnessThoughts thoughts,
                                    KindnessActions actions, KindnessSelf self) {
        KindnessEntry newKindnessEntry =
                new KindnessEntry(mCreationTimestamp, words, thoughts, actions, self, mComplete);
        if( newKindnessEntry.isEmpty() ) {
            mEditKindnessEntryView.showEmptyEntryError();
        }
        else {
            mKindnessRepository.updateKindnessEntry(newKindnessEntry);
            mEditKindnessEntryView.showKindnessView();
        }
    }

    @Override
    public void saveNewKindnessEntry(long timestamp, KindnessWords words, KindnessThoughts thoughts,
                                     KindnessActions actions, KindnessSelf self) {
        KindnessEntry newKindnessEntry = new KindnessEntry(timestamp, words, thoughts, actions, self);
        if( newKindnessEntry.isEmpty() ) {
            mEditKindnessEntryView.showEmptyEntryError();
        }
        else {
            mKindnessRepository.saveKindnessEntry(newKindnessEntry);
            mEditKindnessEntryView.showKindnessView();
        }
    }

    @Override
    public void deleteKindnessEntry() {
        mKindnessRepository.getKindnessEntry(mCreationTimestamp, new KindnessRepository.LoadKindnessEntryCallback() {
            @Override
            public void onEntryLoaded(KindnessEntry kindnessEntry) {
                mKindnessRepository.deleteKindnessEntry(kindnessEntry);
            }
        });
    }
}
