package com.hedgehogproductions.therapyguide.diarydata;

public class DiaryEntry {
    private final long creationTimestamp;
    private String text;

    public DiaryEntry(long timestamp, String text) {
        this.creationTimestamp = timestamp;
        this.text = text;
    }
}

