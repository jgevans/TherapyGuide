package com.hedgehogproductions.therapyguide.editkindnessentry;

import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

public interface EditKindnessEntryContract {
    interface View {

        void showKindnessView();

        void showEmptyEntryError();

        void showMissingEntryError();
    }

    interface UserActionsListener {

        void updateKindnessEntry(KindnessWords words, KindnessThoughts thoughts,
                                 KindnessActions actions, KindnessSelf self);

        void saveNewKindnessEntry(long timestamp, KindnessWords words, KindnessThoughts thoughts,
                                  KindnessActions actions, KindnessSelf self);

        void openKindnessEntry(long timestamp);

        KindnessEntry getKindnessEntry();

        void deleteKindnessEntry();

    }
}
