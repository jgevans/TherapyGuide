package com.hedgehogproductions.therapyguide.diarydata;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class DiaryEntryTest {

    @Test
    public void diaryEntry_CorrectEntry_HasCorrectData() {
        long timestamp = System.currentTimeMillis();
        String text = "text1";

        DiaryEntry entry = new DiaryEntry(timestamp, text);
        assertEquals(entry.getCreationTimestamp(), timestamp);
        assertEquals(entry.getText(), text);
    }

    @Test
    public void emptyTimestamp_EntryIsEmpty() {
        long timestamp = 0;
        String text = "text2";

        DiaryEntry entry = new DiaryEntry(timestamp, text);
        assertTrue(entry.isEmpty());
    }

    @Test
    public void emptyText_EntryIsEmpty() {
        long timestamp = System.currentTimeMillis();
        String text = "";

        DiaryEntry entry = new DiaryEntry(timestamp, text);
        assertTrue(entry.isEmpty());
    }
}