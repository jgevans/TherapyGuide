package com.hedgehogproductions.therapyguide.kindnessdata;

import android.os.Build;

import java.util.Date;
import java.util.Objects;

public class KindnessEntry {

    private Date mCreationDate = null;
    private KindnessCategories mKindnessCategory = KindnessCategories.NONE;
    private String mKindnessValue = null;

    private boolean mComplete;

    public KindnessEntry() {}

    public KindnessEntry(Date date) {
        mCreationDate = date;
    }

    public KindnessEntry(Date date, KindnessCategories category, String value) {
        this(date, category, value, false);
    }

    public KindnessEntry(Date date, KindnessCategories category, String value, boolean complete) {
        mCreationDate = date;
        mKindnessCategory = category;
        mKindnessValue = value;
        mComplete = complete;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public KindnessCategories getCategory() {
        return mKindnessCategory;
    }
    public String getValue() {
        return mKindnessValue;
    }

    public void setCategory(KindnessCategories category) {
        mKindnessCategory = category;
    }
    public void setValue(String value) {
        mKindnessValue = value;
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    public boolean isEmpty() {
        return mCreationDate == null || mKindnessCategory == KindnessCategories.NONE ||
                mKindnessValue == null;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (o instanceof KindnessEntry) {
            KindnessEntry user = (KindnessEntry) o;
            return mCreationDate.equals(user.mCreationDate);
        }
        else {
            return o instanceof Date && mCreationDate.equals(o);
        }
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(mCreationDate);
        }
        else {
            return ((int) mCreationDate.getTime());
        }
    }

    @Override
    public String toString() {
        String value;
        if( mKindnessValue == null ) {
            value = "(Not Chosen)";
        } else {
            value = mKindnessValue;
        }
        return String.valueOf(mCreationDate) + ": " + value + "(" + mKindnessCategory.toString() + ")";
    }
}
