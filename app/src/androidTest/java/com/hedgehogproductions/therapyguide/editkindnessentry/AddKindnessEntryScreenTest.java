package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.Matchers;
import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static com.hedgehogproductions.therapyguide.KindnessManipulators.deleteKindnessEntry;
import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.EDIT_MODE;
import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.FROM_MAIN_ACTIVITY;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Tests for the edit kindness entry screen, in add mode, the screen which allows the creation
 *  of a new kindness entry.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddKindnessEntryScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<EditKindnessEntryActivity> mEntryActivityTestRule =
            new ActivityTestRule<EditKindnessEntryActivity>(EditKindnessEntryActivity.class) {

                @Override
                protected Intent getActivityIntent() {
                    Intent editIntent = new Intent();
                    editIntent.putExtra(EDIT_MODE, false);
                    editIntent.putExtra(FROM_MAIN_ACTIVITY, false);
                    return editIntent;
                }
            };

    @Test
    public void showNoDeleteOptionForAddMode() {
    }

    @Test
    public void showInstructionsUnlessTurnedOff() {

    }

    @Test
    public void doNotShowInstructionsIfTurnedOff() {

    }

    @Test
    public void saveKindnessEntry_showsKindnessDiaryWithEntry() {
//        String wordsSelectionText = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.kindness_words_abilities);
//        String thoughtsSelectionText = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.kindness_thoughts_listen);
//        String actionsSelectionText = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.kindness_actions_door);
//        String selfSelectionText = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.kindness_self_movie);
//        onView(withId(R.id.editkindnessentry_words_spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is(wordsSelectionText))).perform(click());
//        onView(withId(R.id.editkindnessentry_words_spinner)).check(matches(withSpinnerText(containsString(wordsSelectionText))));
//        onView(withId(R.id.editkindnessentry_thoughts_spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is(thoughtsSelectionText))).perform(click());
//        onView(withId(R.id.editkindnessentry_thoughts_spinner)).check(matches(withSpinnerText(containsString(thoughtsSelectionText))));
//        onView(withId(R.id.editkindnessentry_actions_spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is(actionsSelectionText))).perform(click());
//        onView(withId(R.id.editkindnessentry_actions_spinner)).check(matches(withSpinnerText(containsString(actionsSelectionText))));
//        onView(withId(R.id.editkindnessentry_self_spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is(selfSelectionText))).perform(click());
//        onView(withId(R.id.editkindnessentry_self_spinner)).check(matches(withSpinnerText(containsString(selfSelectionText))));
//
//        onView(withId(R.id.editkindnessentry_save_button)).perform(click());
//
//        // Verify entry is displayed on screen
//        onView(Matchers.withItemText(wordsSelectionText)).check(matches(isDisplayed()));
//        onView(Matchers.withItemText(thoughtsSelectionText)).check(matches(isDisplayed()));
//        onView(Matchers.withItemText(actionsSelectionText)).check(matches(isDisplayed()));
//        onView(Matchers.withItemText(selfSelectionText)).check(matches(isDisplayed()));
//        //TODO ensure not complete is visible
//
//        // Delete it to clean up
//        deleteKindnessEntry(wordsSelectionText);
    }


    @Test
    public void cancelEntry_showsDiary() {
        onView(withId(R.id.editkindnessentry_cancel_button)).perform(click());

        onView(withId(R.id.kindness_view)).check(matches(isDisplayed()));
    }
}
