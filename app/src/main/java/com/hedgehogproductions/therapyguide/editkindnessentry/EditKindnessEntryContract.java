package com.hedgehogproductions.therapyguide.editkindnessentry;

import com.hedgehogproductions.therapyguide.kindnessdata.KindnessCategories;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;

import java.util.Date;

interface EditKindnessEntryContract {
    interface View {

        void showKindnessView();

        void showEmptyEntryError();

        void showMissingEntryError();

        boolean moveToNextView(KindnessCategories kindnessCategory);
    }

    interface UserActionsListener {

        void updateKindnessEntry(KindnessCategories category, String value);

        void saveNewKindnessEntry(Date date, KindnessCategories category, String value);

        void openKindnessEntry(Date date);

        KindnessEntry getKindnessEntry();

        void deleteKindnessEntry();

    }
}
