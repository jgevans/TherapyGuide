package com.hedgehogproductions.therapyguide.diary;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.Matchers;
import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hedgehogproductions.therapyguide.DiaryManipulators.addNewDiaryEntry;
import static com.hedgehogproductions.therapyguide.DiaryManipulators.deleteDiaryEntry;
import static com.hedgehogproductions.therapyguide.DiaryManipulators.deleteWholeDiary;

/**
 * Tests for the diary screen, the screen which contains a list of all
 *  diary entries.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DiaryScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);



    @Test
    public void clickAddDiaryEntryButton_opensAddDiaryEntryUi() {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Click on the add diary entry button
        onView(withId(R.id.create_button)).perform(click());

        // Check if the add diary entry screen is displayed
        onView(withId(R.id.adddiaryentry_entry_text)).check(matches(isDisplayed()));
    }

    @Test
    public void addEntryToDiary() {
        String newDiaryText = "I executed an Espresso test";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.diary_view)).perform(
                scrollTo(hasDescendant(withText(newDiaryText))));

        // Verify entry is displayed on screen
        onView(Matchers.withItemText(newDiaryText)).check(matches(isDisplayed()));

        // Delete it to clean up
        deleteDiaryEntry(newDiaryText);
    }

    @Test
    public void goBackFromAddDiaryEntryScreen_EndsAtDiary() {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Click on the add diary entry button
        onView(withId(R.id.create_button)).perform(click());

        // Click back
        onView(withContentDescription("Navigate up")).perform(click());

        // Verify Diary is displayed
        onView(withId(R.id.diary_view)).check(matches(isDisplayed()));
    }

    @Test
    public void swipeOnEntry_ShowsDeletionDialog() {
        String newDiaryText = "I'm going to delete this entry";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Swipe the entry
        onView(withId(R.id.diary_view))
                .perform(scrollTo(hasDescendant(withText(newDiaryText))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Verify deletion dialog message is shown
        onView(withText(R.string.dialog_delete_diary_entry)).check(matches(isDisplayed()));

        // Delete it to clean up
        onView(withText(R.string.ok_delete_diary_entry)).perform(click());
    }

    @Test
    public void cancelEntryDeletion_LeavesEntryInView() {
        String newDiaryText = "I'm NOT going to delete this entry";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Swipe the entry
        onView(withId(R.id.diary_view))
                .perform(scrollTo(hasDescendant(withText(newDiaryText))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Cancel Entry deletion
        onView(withText(R.string.cancel_delete_diary_entry)).perform(click());

        // Verify dialog message gone and entry still in view
        onView(withText(R.string.dialog_delete_diary_entry)).check(doesNotExist());
        onView(Matchers.withItemText(newDiaryText)).check(matches(isDisplayed()));

        // Delete it anyway to clean up
        deleteDiaryEntry(newDiaryText);
    }

    @Test
    public void confirmEntryDeletion_RemovesEntryFromView() {
        String newDiaryText = "I'm going to delete this entry";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Delete entry
        deleteDiaryEntry(newDiaryText);

        // Verify dialog message gone and entry gone from view
        onView(withText(R.string.dialog_delete_diary_entry)).check(doesNotExist());
        onView(Matchers.withItemText(newDiaryText)).check(doesNotExist());
    }

    @Test
    public void dismissEntryDeletion_LeavesEntryInView() {
        String newDiaryText = "I'm NOT going to delete this entry";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Swipe the entry
        onView(withId(R.id.diary_view))
                .perform(scrollTo(hasDescendant(withText(newDiaryText))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Cancel Entry deletion
        onView(withText(R.string.dialog_delete_diary_entry)).perform(pressBack());

        // Verify dialog message gone and entry still in view
        onView(withText(R.string.dialog_delete_diary_entry)).check(doesNotExist());
        onView(Matchers.withItemText(newDiaryText)).check(matches(isDisplayed()));

        // Delete it anyway to clean up
        deleteDiaryEntry(newDiaryText);
    }

    @Test
    public void largeDiary_LoadsFully() {
        final int numEntries = 7;
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        for(int entryNumber = 1; entryNumber <= numEntries; ++entryNumber) {
            addNewDiaryEntry("New Diary Entry " + String.valueOf(entryNumber) );
        }

        for(int entryNumber = 1; entryNumber <= numEntries; ++entryNumber) {
            // Scroll diary to added entry, by finding its text
            onView(withId(R.id.diary_view)).perform(
                    scrollTo(hasDescendant(withText(
                            "New Diary Entry " + String.valueOf(entryNumber)))));

            // Verify entry is displayed on screen
            onView(Matchers.withItemText("New Diary Entry " + String.valueOf(entryNumber)))
                    .check(matches(isDisplayed()));
        }

        deleteWholeDiary(numEntries);
    }

    @Test
    public void clickEntry_DisplaysUpdateEntryUI() {
        String newDiaryText = "I'm NOT going to update this entry";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Click on the entry
        onView(Matchers.withItemText(newDiaryText)).perform(click());

        // Verify the update screen is displayed on screen
        onView(withId(R.id.editdiaryentry_entry_text)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_delete_button)).check(matches(isDisplayed()));

        // Cancel
        onView(withId(R.id.editdiaryentry_entry_text)).perform(closeSoftKeyboard()).perform(pressBack());

        // delete the entry to clean up
        deleteDiaryEntry(newDiaryText);
    }

    @Test
    public void cancelUpdate_ShowsDiaryWithUnchangedEntry() {
        String newDiaryText = "I'm NOT going to update this entry";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Click on the entry
        onView(Matchers.withItemText(newDiaryText)).perform(click());

        // Cancel the update
        onView(withId(R.id.editdiaryentry_cancel_button)).perform(click());

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.diary_view)).perform(
                scrollTo(hasDescendant(withText(newDiaryText))));

        // Verify entry is displayed on screen
        onView(Matchers.withItemText(newDiaryText)).check(matches(isDisplayed()));

        // delete the entry to clean up
        deleteDiaryEntry(newDiaryText);
    }

    @Test
    public void updateEntry_ShowsDiaryWithUpdatedEntry() {
        String newDiaryText = "I'm going to update this entry";
        String updatedDiaryText = "This entry has been updated";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Click on the entry
        onView(withText(newDiaryText)).perform(click());

        // Update diary entry text and close the keyboard
        onView(withId(R.id.editdiaryentry_entry_text)).perform(clearText()).perform(typeText(updatedDiaryText),
                closeSoftKeyboard());

        // Save the update
        onView(withId(R.id.editdiaryentry_save_button)).perform(click());

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.diary_view)).perform(
                scrollTo(hasDescendant(withText(updatedDiaryText))));

        // Verify entry is displayed on screen
        onView(Matchers.withItemText(updatedDiaryText)).check(matches(isDisplayed()));

        // delete the entry to clean up
        deleteDiaryEntry(updatedDiaryText);
    }

    @Test
    public void deleteEntryFromUpdate_RemovesEntryFromDiaryView() {
        String newDiaryText = "I'm going to delete this entry from the update screen";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText);

        // Click on the entry
        onView(withText(newDiaryText)).perform(click());

        // Click delete
        onView(withId(R.id.editdiaryentry_delete_button)).perform(click());

        // Click confirm
        onView(withText(R.string.ok_delete_diary_entry)).perform(click());

        // Verify that diary is in view and entry has been removed
        onView(withId(R.id.diary_view)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText)).check(doesNotExist());
    }
}
