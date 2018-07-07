package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.support.annotation.NonNull;

class KindnessItem {
    private final String mText;
    private final boolean mSelected;

    KindnessItem(@NonNull String text, boolean selected) {
        mText = text;
        mSelected = selected;
    }

    public String getText() {
        return mText;
    }

    public boolean isSelected() {
        return mSelected;
    }

}
