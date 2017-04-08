package com.hedgehogproductions.therapyguide.diarydata;

public class DiaryEntry {
    private final long creationTimestamp;
    private final String text;

    public DiaryEntry(long timestamp, String text) {
        this.creationTimestamp = timestamp;
        this.text = text;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getText() {
        return text;
    }
}

