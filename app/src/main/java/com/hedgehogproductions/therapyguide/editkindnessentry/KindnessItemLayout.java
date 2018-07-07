package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Checkable;

public class KindnessItemLayout extends LinearLayout implements Checkable {

    private boolean mChecked;

    public KindnessItemLayout(Context context) {
        super(context);
    }

    public KindnessItemLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
    }
}
