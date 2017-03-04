package com.hedgehogproductions.therapyguide;

class DiaryEntry {
    private final long creationTimestamp;
    private String text;

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

