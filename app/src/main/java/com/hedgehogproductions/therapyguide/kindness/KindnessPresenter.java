package com.hedgehogproductions.therapyguide.kindness;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class KindnessPresenter implements KindnessContract.UserActionsListener {
    public static final String LAST_KINDNESS_NOTIFICATION_PREF = "last_kindness_notification";

    private final KindnessRepository mKindnessRepository;
    private final KindnessContract.View mKindnessView;

    KindnessPresenter(
            @NonNull KindnessRepository kindnessRepository, @NonNull KindnessContract.View kindnessView ) {
        mKindnessRepository = checkNotNull(kindnessRepository, "kindnessRepository cannot be null");
        mKindnessView = checkNotNull(kindnessView, "kindnessView cannot be null");
    }

    @Override
    public void loadKindnessDiary() {

        mKindnessRepository.getKindnessDiary(new KindnessRepository.LoadKindnessCallback() {
            @Override
            public void onKindnessLoaded(List<KindnessEntry> kindnessDiary) {
                mKindnessView.showKindnessDiary(kindnessDiary);
            }
        });
    }

    @Override
    public void addNewKindnessEntry() {
        mKindnessView.showAddKindnessEntry();
    }

    @Override
    public void updateKindnessEntry(@NonNull KindnessEntry selectedEntry) {
        checkNotNull(selectedEntry, "KindnessEntry cannot be null");
        mKindnessView.showUpdateKindnessEntry(selectedEntry.getCreationDate());
    }

    @Override
    public void updateKindnessEntryCompleteness(@NonNull KindnessEntry selectedEntry) {
        checkNotNull(selectedEntry, "KindnessEntry cannot be null");
        mKindnessRepository.updateKindnessEntryCompleteness(selectedEntry);
    }

    @Override
    public void instigateKindnessEntryDeletion(final int position) {
        mKindnessView.showKindnessEntryDeletionMessage(position);
    }

    @Override
    public void deleteKindnessEntry(KindnessEntry entry) {
        mKindnessRepository.deleteKindnessEntry(entry);
    }

    @Override
    public void setKindnessNotificationShown(Context context) {
        // Save the time of the notification so we know it when we set up a new one
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        editor.apply();
    }
}
