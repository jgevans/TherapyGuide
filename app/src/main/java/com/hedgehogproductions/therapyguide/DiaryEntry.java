package com.hedgehogproductions.therapyguide;

class DiaryEntry {
    private long timestamp;
    private String text;

    public DiaryEntry(long timestamp, String text) {
        this.timestamp = timestamp;
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }
}

