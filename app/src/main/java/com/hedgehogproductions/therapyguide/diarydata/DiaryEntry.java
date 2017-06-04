package com.hedgehogproductions.therapyguide.diarydata;

import android.os.Build;

import java.util.Objects;

public class DiaryEntry {
    private final long mCreationTimestamp;
    private final String mText;

    public DiaryEntry(long timestamp, String text) {
        this.mCreationTimestamp = timestamp;
        this.mText = text;
    }

    public long getCreationTimestamp() {
        return mCreationTimestamp;
    }

    public String getText() {
        return mText;
    }

    public boolean isEmpty() {
        return (mCreationTimestamp == 0) || mCreationTimestamp == ~0 ||
                (mText == null || "".equals(mText));
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof DiaryEntry)) {
            return false;
        }
        DiaryEntry user = (DiaryEntry) o;
        return mCreationTimestamp == user.mCreationTimestamp;
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

}

