package com.hedgehogproductions.therapyguide.diarydata;

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
        return (mCreationTimestamp == 0) ||
                (mText == null || "".equals(mText));
    }
}

