package com.hedgehogproductions.therapyguide.editkindnessentry;

import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

import java.nio.file.WatchEvent;

public interface EditKindnessEntryContract {
    interface View {

        void showKindnessView();

        void showEmptyEntryError();

        void showMissingEntryError();

        void showKindnessDetail(KindnessWords words, KindnessThoughts thoughts,
                                KindnessActions actions, KindnessSelf self, boolean complete);

        void showEntryDeletionMessage();
    }

    interface UserActionsListener {

        void updateKindnessEntry(KindnessWords words, KindnessThoughts thoughts,
                                 KindnessActions actions, KindnessSelf self);

        void saveNewKindnessEntry(long timestamp, KindnessWords words, KindnessThoughts thoughts,
                                  KindnessActions actions, KindnessSelf self);

        void openKindnessEntry(long timestamp);

        void instigateKindnessEntryDeletion();

        void deleteKindnessEntry();

    }
}
