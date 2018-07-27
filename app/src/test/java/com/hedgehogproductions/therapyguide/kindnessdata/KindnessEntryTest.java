package com.hedgehogproductions.therapyguide.kindnessdata;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class KindnessEntryTest {

    @Test
    public void diaryEntry_CorrectEntry_HasCorrectData() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        KindnessWords words = KindnessWords.APPEARANCE;
        KindnessThoughts thoughts = KindnessThoughts.FORGIVE;
        KindnessActions actions = KindnessActions.CAKE;
        KindnessSelf self = KindnessSelf.COMPASSION;

        KindnessEntry entry = new KindnessEntry(calendar.getTime(), words, thoughts, actions, self);
        assertEquals(entry.getCreationDate(), calendar.getTime());
        assertEquals(entry.getWords(), words);
        assertEquals(entry.getThoughts(), thoughts);
        assertEquals(entry.getActions(), actions);
        assertEquals(entry.getSelf(), self);
    }

    @Test
    public void emptyTimestamp_EntryIsEmpty() {
        KindnessEntry entry = new KindnessEntry(null, KindnessWords.APPEARANCE, KindnessThoughts.FORGIVE, KindnessActions.CAKE, KindnessSelf.COMPASSION);
        assertTrue(entry.isEmpty());
    }

    @Test
    public void markComplete_EntryIsComplete() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        KindnessWords words = KindnessWords.APPEARANCE;
        KindnessThoughts thoughts = KindnessThoughts.FORGIVE;
        KindnessActions actions = KindnessActions.CAKE;
        KindnessSelf self = KindnessSelf.COMPASSION;

        KindnessEntry entry = new KindnessEntry(calendar.getTime(), words, thoughts, actions, self);
        assertFalse(entry.isComplete());
        entry.setComplete(true);
        assertTrue(entry.isComplete());
    }
}
