package com.hedgehogproductions.therapyguide.diarydata;

import android.os.Build;

import java.util.Objects;

public class DiaryEntry {
    private final long mCreationTimestamp;
    private final String mText1;
    private final String mText2;
    private final String mText3;
    private final String mText4;
    private final String mText5;

    public DiaryEntry(long timestamp,
                      String text1, String text2, String text3, String text4, String text5) {
        this.mCreationTimestamp = timestamp;
        this.mText1 = text1;
        this.mText2 = text2;
        this.mText3 = text3;
        this.mText4 = text4;
        this.mText5 = text5;
    }

    public long getCreationTimestamp() {
        return mCreationTimestamp;
    }

    public String getText1() {
        return mText1;
    }
    public String getText2() {
        return mText2;
    }
    public String getText3() {
        return mText3;
    }
    public String getText4() {
        return mText4;
    }
    public String getText5() {
        return mText5;
    }

    public boolean isEmpty() {
        return (mCreationTimestamp == 0) || mCreationTimestamp == ~0 ||
                mText1 == null || "".equals(mText1) ||
                mText2 == null || "".equals(mText2) ||
                mText3 == null || "".equals(mText3) ||
                mText4 == null || "".equals(mText4) ||
                mText5 == null || "".equals(mText5);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (o instanceof DiaryEntry) {
            DiaryEntry user = (DiaryEntry) o;
            return mCreationTimestamp == user.mCreationTimestamp;
        }
        else {
            return o instanceof Long && mCreationTimestamp == (Long) o;
        }
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(mCreationTimestamp);
        }
        else {
            return ((int) mCreationTimestamp);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(mCreationTimestamp + ": " + mText1 + "," + mText2 + ","
                + mText3 + "," + mText4 + "," + mText5);
    }
}

