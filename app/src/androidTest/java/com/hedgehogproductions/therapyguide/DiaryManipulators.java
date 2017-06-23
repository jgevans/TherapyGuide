package com.hedgehogproductions.therapyguide;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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
        onView(withId(R.id.editdiaryentry_entry_text)).perform(typeText(entryText),
                closeSoftKeyboard());

        // Save the entry
        onView(withId(R.id.editdiaryentry_save_button)).perform(click());
    }

    /**
     * Convenience method that deletes an entry from the diary
     */
    public static void deleteDiaryEntry(String entryText) {
        // Scroll diary to entry, by finding its text
        onView(withId(R.id.diary_view)).perform(scrollTo(hasDescendant(withText(entryText))));

        // Click on the entry
        onView(withText(entryText)).perform(click());

        // Click delete
        onView(withId(R.id.editdiaryentry_delete_button)).perform(click());

        // Click confirm
        onView(withText(R.string.ok_delete_diary_entry)).perform(click());

        // Verify that diary is in view and entry has been removed
        onView(withId(R.id.diary_view)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(entryText)).check(doesNotExist());
    }
}
