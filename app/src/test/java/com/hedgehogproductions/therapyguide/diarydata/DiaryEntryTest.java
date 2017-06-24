package com.hedgehogproductions.therapyguide.diarydata;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class DiaryEntryTest {

    @Test
    public void diaryEntry_CorrectEntry_HasCorrectData() {
        long timestamp = System.currentTimeMillis();
        String text1 = "text1";
        String text2 = "text2";
        String text3 = "text3";
        String text4 = "text4";
        String text5 = "text5";

        DiaryEntry entry = new DiaryEntry(timestamp, text1, text2, text3, text4, text5);
        assertEquals(entry.getCreationTimestamp(), timestamp);
        assertEquals(entry.getText1(), text1);
        assertEquals(entry.getText2(), text2);
        assertEquals(entry.getText3(), text3);
        assertEquals(entry.getText4(), text4);
        assertEquals(entry.getText5(), text5);
    }

    @Test
    public void emptyTimestamp_EntryIsEmpty() {
        long timestamp = 0;
        String text = "text2";

        DiaryEntry entry = new DiaryEntry(timestamp, text, text, text, text, text);
        assertTrue(entry.isEmpty());
    }

    @Test
    public void emptyText_EntryIsEmpty() {
        long timestamp = System.currentTimeMillis();
        String text1 = "";
        String text2 = "text2";

        DiaryEntry entry = new DiaryEntry(timestamp, text2, text2, text2, text1, text2);
        assertTrue(entry.isEmpty());
    }
}