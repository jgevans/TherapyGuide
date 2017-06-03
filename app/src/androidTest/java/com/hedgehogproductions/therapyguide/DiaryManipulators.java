package com.hedgehogproductions.therapyguide;


import android.support.test.espresso.contrib.RecyclerViewActions;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DiaryManipulators {
    /**
     * Convenience method that adds an entry into the diary
     */
    public static void addNewDiaryEntry(String entryText) {
        // Click on the add diary entry button
        onView(withId(R.id.create_button)).perform(click());

        // Add diary entry text and close the keyboard
        onView(withId(R.id.adddiaryentry_entry_text)).perform(typeText(entryText),
                closeSoftKeyboard());

        // Save the entry
        onView(withId(R.id.adddiaryentry_save_button)).perform(click());
    }

    /**
     * Convenience method that deletes an entry from the diary
     */
    public static void deleteDiaryEntry(String entryText) {
        // Swipe the entry
        onView(withId(R.id.diary_view))
                .perform(scrollTo(hasDescendant(withText(entryText))));
        onView(withId(R.id.diary_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(entryText)), swipeLeft()));

        // Confirm Entry deletion
        onView(withText(R.string.ok_delete_diary_entry)).perform(click());
    }

    /**
     * Convenience method that deletes a set number of entries from the diary
     */
    public static void deleteWholeDiary(int entries) {
        // For each entry
        for( int entry = 0; entry < entries; ++ entry ) {
            // Swipe it
            onView(withId(R.id.diary_view))
                    .perform(scrollToPosition(0))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

            // Confirm Entry deletion
            onView(withText(R.string.ok_delete_diary_entry)).perform(click());
        }
    }
}
