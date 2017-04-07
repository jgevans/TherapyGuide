package com.hedgehogproductions.therapyguide.diarydata;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DiaryEntryTest {

    @Test
    public void diaryEntry_CorrectEntry_HasCorrectData() {
        long timestamp = System.currentTimeMillis();
        String text = "text1";

        DiaryEntry entry = new DiaryEntry(timestamp, text);
        assertEquals(entry.getCreationTimestamp(), timestamp);
        assertEquals(entry.getText(), text);
    }
}