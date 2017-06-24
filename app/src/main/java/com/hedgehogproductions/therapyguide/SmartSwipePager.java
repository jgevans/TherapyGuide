package com.hedgehogproductions.therapyguide;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SmartSwipePager extends ViewPager {
    public SmartSwipePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        Log.d("SmartSwipePager", "Recycler view? " + String.valueOf(v instanceof CardView));
        return v instanceof CardView || super.canScroll(v, checkV, dx, x, y);
    }
}
