package com.hedgehogproductions.therapyguide.kindnessdata;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class KindnessEntryTest {

    @Test
    public void diaryEntry_CorrectEntry_HasCorrectData() {
        long timestamp = System.currentTimeMillis();
        KindnessWords words = KindnessWords.APPEARANCE;
        KindnessThoughts thoughts = KindnessThoughts.FORGIVE;
        KindnessActions actions = KindnessActions.CAKE;
        KindnessSelf self = KindnessSelf.COMPASSION;

        KindnessEntry entry = new KindnessEntry(timestamp, words, thoughts, actions, self);
        assertEquals(entry.getCreationDate(), timestamp);
        assertEquals(entry.getWords(), words);
        assertEquals(entry.getThoughts(), thoughts);
        assertEquals(entry.getActions(), actions);
        assertEquals(entry.getSelf(), self);
    }

    @Test
    public void emptyTimestamp_EntryIsEmpty() {
        long timestamp = 0;
        KindnessWords words = KindnessWords.APPEARANCE;
        KindnessThoughts thoughts = KindnessThoughts.FORGIVE;
        KindnessActions actions = KindnessActions.CAKE;
        KindnessSelf self = KindnessSelf.COMPASSION;

        KindnessEntry entry = new KindnessEntry(timestamp, words, thoughts, actions, self);
        assertTrue(entry.isEmpty());
    }

    @Test
    public void markComplete_EntryIsComplete() {
        long timestamp = 0;
        KindnessWords words = KindnessWords.APPEARANCE;
        KindnessThoughts thoughts = KindnessThoughts.FORGIVE;
        KindnessActions actions = KindnessActions.CAKE;
        KindnessSelf self = KindnessSelf.COMPASSION;

        KindnessEntry entry = new KindnessEntry(timestamp, words, thoughts, actions, self);
        assertFalse(entry.isComplete());
        entry.setComplete(true);
        assertTrue(entry.isComplete());
    }
}
