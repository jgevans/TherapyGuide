package com.hedgehogproductions.therapyguide.editkindnessentry;

import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

import java.util.Date;

interface EditKindnessEntryContract {
    interface View {

        void showKindnessView();

        void showEmptyEntryError();

        void showMissingEntryError();

        boolean moveToNextView();
    }

    interface UserActionsListener {

        void updateKindnessEntry(KindnessWords words, KindnessThoughts thoughts,
                                 KindnessActions actions, KindnessSelf self);

        void saveNewKindnessEntry(Date date, KindnessWords words, KindnessThoughts thoughts,
                                  KindnessActions actions, KindnessSelf self);

        void openKindnessEntry(Date date);

        KindnessEntry getKindnessEntry();

        void deleteKindnessEntry();

    }
}
