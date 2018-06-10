package com.hedgehogproductions.therapyguide.diary;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.Matchers;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.intro.IntroActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeUp;
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
import static org.hamcrest.Matchers.not;

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

    @Before
    public void setSharedPrefs() {
        SharedPreferences prefs = getInstrumentation().getTargetContext()
                .getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IntroActivity.SHOW_INTRO_PREF, false);
        editor.apply();
    }


    @Test
    public void clickAddDiaryEntryButton_opensEditDiaryEntryUiInAddMode() {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Click on the add diary entry button
        onView(withId(R.id.create_button)).perform(click());

        // Check that the edit diary entry screen is displayed
        onView(withId(R.id.editdiaryentry_entry_text1)).check(matches(isDisplayed()));

        // Check that the delete button is not shown
        onView(withId(R.id.editdiaryentry_delete_button)).check(matches(not(isDisplayed())));
    }

    @Test
    public void addEntryToDiary() {
        String newDiaryText1 = "I executed an Espresso test";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.diary_view)).perform(
                scrollTo(hasDescendant(withText(newDiaryText1))));

        // Verify entry is displayed on screen
        onView(Matchers.withItemText(newDiaryText1)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText2)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText3)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText4)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText5)).check(matches(isDisplayed()));

        // Delete it to clean up
        deleteDiaryEntry(newDiaryText1);
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
        String newDiaryText1 = "I'm going to delete this entry";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Swipe the entry
        onView(withId(R.id.diary_view))
                .perform(scrollTo(hasDescendant(withText(newDiaryText1))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Verify deletion dialog message is shown
        onView(withText(R.string.dialog_delete_entry)).check(matches(isDisplayed()));

        // Delete it to clean up
        onView(withText(R.string.ok_delete_entry)).perform(click());
    }

    @Test
    public void cancelEntryDeletion_LeavesEntryInView() {
        String newDiaryText1 = "I'm NOT going to delete this entry";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Swipe the entry
        onView(withId(R.id.diary_view))
                .perform(scrollTo(hasDescendant(withText(newDiaryText1))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Cancel Entry deletion
        onView(withText(R.string.cancel_delete_entry)).perform(click());

        // Verify dialog message gone and entry still in view
        onView(withText(R.string.dialog_delete_entry)).check(doesNotExist());
        onView(Matchers.withItemText(newDiaryText1)).check(matches(isDisplayed()));

        // Delete it anyway to clean up
        deleteDiaryEntry(newDiaryText1);
    }

    @Test
    public void confirmEntryDeletion_RemovesEntryFromView() {
        String newDiaryText1 = "I'm going to delete this entry";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Delete entry
        deleteDiaryEntry(newDiaryText1);

        // Verify dialog message gone and entry gone from view
        onView(withText(R.string.dialog_delete_entry)).check(doesNotExist());
        onView(Matchers.withItemText(newDiaryText1)).check(doesNotExist());
    }

    @Test
    public void dismissEntryDeletion_LeavesEntryInView() {
        String newDiaryText1 = "I'm NOT going to delete this entry";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Swipe the entry
        onView(withId(R.id.diary_view))
                .perform(scrollTo(hasDescendant(withText(newDiaryText1))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Cancel Entry deletion
        onView(withText(R.string.dialog_delete_entry)).perform(pressBack());

        // Verify dialog message gone and entry still in view
        onView(withText(R.string.dialog_delete_entry)).check(doesNotExist());
        onView(Matchers.withItemText(newDiaryText1)).check(matches(isDisplayed()));

        // Delete it anyway to clean up
        deleteDiaryEntry(newDiaryText1);
    }

    @Test
    public void largeDiary_LoadsFully() {
        final int numEntries = 7;
        String newDiaryText, newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5;
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        for(int entryNumber = 1; entryNumber <= numEntries; ++entryNumber) {
            newDiaryText = "New Diary Entry " + String.valueOf(entryNumber);
            newDiaryText1 = newDiaryText + " - pt1";
            newDiaryText2 = newDiaryText + " - pt2";
            newDiaryText3 = newDiaryText + " - pt3";
            newDiaryText4 = newDiaryText + " - pt4";
            newDiaryText5 = newDiaryText + " - pt5";
            addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);
        }

        for(int entryNumber = 1; entryNumber <= numEntries; ++entryNumber) {
            // Scroll diary to added entry, by finding its text
            onView(withId(R.id.diary_view)).perform(
                    scrollTo(hasDescendant(withText(
                            "New Diary Entry " + String.valueOf(entryNumber) + " - pt1"))));

            // Verify entry is displayed on screen
            onView(Matchers.withItemText("New Diary Entry " + String.valueOf(entryNumber) + " - pt1"))
                    .check(matches(isDisplayed()));
        }

        for(int entryNumber = 1; entryNumber <= numEntries; ++entryNumber) {
            deleteDiaryEntry("New Diary Entry " + String.valueOf(entryNumber) + " - pt1");
        }
    }

    @Test
    public void clickEntry_DisplaysUpdateEntryUI() {
        String newDiaryText1 = "I'm NOT going to update this entry";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Click on the entry
        onView(Matchers.withItemText(newDiaryText1)).perform(click());

        // Verify the update screen is displayed on screen
        onView(withId(R.id.editdiaryentry_entry_text1)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_entry_text2)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_entry_text3)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_entry_text4)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_entry_text5)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_delete_button)).check(matches(isDisplayed()));

        // Cancel
        onView(withId(R.id.editdiaryentry_entry_text2)).perform(closeSoftKeyboard()).perform(pressBack());

        // delete the entry to clean up
        deleteDiaryEntry(newDiaryText1);
    }

    @Test
    public void cancelUpdate_ShowsDiaryWithUnchangedEntry() {
        String newDiaryText1 = "I'm NOT going to update this entry";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Click on the entry
        onView(Matchers.withItemText(newDiaryText1)).perform(click());

        // Cancel the update
        onView(withId(R.id.editdiaryentry_cancel_button)).perform(click());

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.diary_view)).perform(
                scrollTo(hasDescendant(withText(newDiaryText1))));

        // Verify entry is displayed on screen
        onView(Matchers.withItemText(newDiaryText1)).check(matches(isDisplayed()));

        // delete the entry to clean up
        deleteDiaryEntry(newDiaryText1);
    }

    @Test
    public void updateEntry_ShowsDiaryWithUpdatedEntry() {
        String newDiaryText1 = "I'm going to update this entry";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";
        String updatedDiaryText = "This entry has been updated";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Click on the entry
        onView(withText(newDiaryText1)).perform(click());

        // Update diary entry text and close the keyboard
        onView(withId(R.id.editdiaryentry_entry_text1)).perform(clearText()).perform(typeText(updatedDiaryText),
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
        String newDiaryText1 = "I'm going to delete this entry from the update screen";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add an entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Click on the entry
        onView(withText(newDiaryText1)).perform(click());

        // Make sure the delete button is visible
        onView(withId(R.id.contentFrame)).perform(swipeUp());

        // Click delete
        onView(withId(R.id.editdiaryentry_delete_button)).perform(click());

        // Click confirm
        onView(withText(R.string.ok_delete_entry)).perform(click());

        // Verify that diary is in view and entry has been removed
        onView(withId(R.id.diary_view)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText1)).check(doesNotExist());
    }
}
