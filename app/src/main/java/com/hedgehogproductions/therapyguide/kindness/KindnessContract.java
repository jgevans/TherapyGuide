package com.hedgehogproductions.therapyguide.kindness;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;

import java.util.Date;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface KindnessContract {

    interface View {

        void showKindnessDiary(List<KindnessEntry> entries);

        void showAddKindnessEntry();

        void showUpdateKindnessEntry(Date selectedEntryDate);

        void showKindnessEntryDeletionMessage(final int position);
    }

    interface UserActionsListener {

        void loadKindnessDiary();

        void addNewKindnessEntry();

        void updateKindnessEntry(@NonNull KindnessEntry selectedEntry);

        void updateKindnessEntryCompleteness(@NonNull KindnessEntry selectedEntry);

        void instigateKindnessEntryDeletion(final int position);

        void deleteKindnessEntry(KindnessEntry entry);

        void setKindnessNotificationShown(Context context);

    }
}
