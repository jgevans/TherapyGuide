package com.hedgehogproductions.therapyguide.kindness;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.intro.IntroActivity;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessServiceApi;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessServiceApiImpl;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hedgehogproductions.therapyguide.KindnessManipulators.createKindnessEntry;
import static com.hedgehogproductions.therapyguide.KindnessManipulators.deleteKindnessEntry;
import static com.hedgehogproductions.therapyguide.Matchers.kindnessWithText;
import static com.hedgehogproductions.therapyguide.Matchers.viewWithBackgroundColour;
import static com.hedgehogproductions.therapyguide.Matchers.withItemText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

/**
 * Tests for the kindness screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class KindnessScreenTest {

    private SharedPreferences.Editor mEditor;

    /* Rule to launch activity under test */
    @Rule
    public final ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setSharedPrefs() {
        SharedPreferences prefs = getInstrumentation().getTargetContext()
                .getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        mEditor = prefs.edit();
        mEditor.putBoolean(IntroActivity.SHOW_INTRO_PREF, false);
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, 1);
        mEditor.apply();
    }

    @Before
    @After
    public void clearDatabaseEntries() {
        final KindnessServiceApiImpl db = new KindnessServiceApiImpl(InstrumentationRegistry.getTargetContext());
        db.getAllKindnessEntries(new KindnessServiceApi.KindnessServiceCallback<List<KindnessEntry>>() {
            @Override
            public void onLoaded(List<KindnessEntry> entries) {
                for (KindnessEntry entry : entries) {
                    db.deleteKindnessEntry(entry);
                }
            }
        });
    }

    @Test
    public void openKindnessTabOnNewDay_showsKindnessReminderMessage() {
        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Check that the kindness selector screen is displayed
        onView(withText(R.string.dialog_kindness_reminder)).check(matches(isDisplayed()));

    }

    @Test
    public void openKindnessTabOnOldDay_doesNotShowSelector() {
        mMainActivityTestRule.launchActivity(null);

        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_love, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_volunteer);

        // Navigate away and back
        onView(withText(R.string.listen_tab_name)).perform(click());
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Check that the kindness selector screen is not displayed
        onView(withText(R.string.dialog_kindness_reminder)).check(doesNotExist());

        // Delete entry to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_love));
    }


    @Test
    public void dismissKindnessReminderMessage_showsKindnessTabAndDoesNotShowMessageAgain() {
        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab and the 'Maybe Later' button
        onView(withText(R.string.kindness_tab_name)).perform(click());
        onView(withText(R.string.cancel_kindness_reminder)).perform(click());

        // Change tab and back again
        onView(withText(R.string.listen_tab_name)).perform(click());
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Check that the kindness selector screen is not displayed
        onView(withText(R.string.dialog_kindness_reminder)).check(doesNotExist());
    }

    @Test
    public void acceptKindnessReminderMessage_opensEditKindnessEntryUi() {
        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab and the 'OK' button
        onView(withText(R.string.kindness_tab_name)).perform(click());
        onView(withText(R.string.ok_kindness_reminder)).perform(click());

        // Check that the Add kindness screen is shown
        onView(withId(R.id.editkindnessentry_pager)).check(matches(isDisplayed()));
    }

    @Test
    public void clickAddKindnessEntryButton_opensEditKindnessEntryUi() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Click on the add kindness entry button
        onView(withId(R.id.create_kindness_button)).perform(click());

        // Check that the Add kindness screen is shown
        onView(withId(R.id.editkindnessentry_pager)).check(matches(isDisplayed()));
    }

    @Test
    public void editKindnessButton_NotVisibleIfEntryCreatedForToday() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create an entry for today
        createKindnessEntry(R.string.kindness_words_love, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_volunteer);

        // Verify the add kindness entry button is not displayed
        onView(withId(R.id.create_kindness_button)).check(matches(not(isDisplayed())));

        // Delete it to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_love));
    }

    @Test
    public void addKindnessEntry_showsEntryCorrectly() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Scroll to added entry, by finding its text
        onView(withId(R.id.kindness_view)).perform(
                scrollTo(hasDescendant(withText(getTargetContext().getResources().getString(R.string.kindness_words_call)))));

        // Verify entry is displayed on screen
        onView(withItemText("Today")).check(matches(isDisplayed()));
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))).check(matches(isDisplayed()));
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_thoughts_gossip))).check(matches(isDisplayed()));
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_actions_cake))).check(matches(isDisplayed()));
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_self_draw))).check(matches(isDisplayed()));

        // Delete it to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_call));

    }

    @Test
    public void createMultipleEntries_showsEntriesInCorrectOrder() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        // Pre-create old entry in database
        KindnessEntry oldEntry = new KindnessEntry(new Date(1532563200000L),
                KindnessWords.ABILITIES, KindnessThoughts.WISHES, KindnessActions.ASSIST, KindnessSelf.COMPLIMENT, false);
        KindnessServiceApiImpl db = new KindnessServiceApiImpl(InstrumentationRegistry.getTargetContext());
        db.saveKindnessEntry(oldEntry);

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create today's entry
        createKindnessEntry(R.string.kindness_words_love, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_volunteer);

        // Verify entry is displayed on screen
        onView(withItemText("Today")).check(
                isCompletelyAbove(withItemText("26 Jul 2018")));
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_love))).check(
                isCompletelyAbove(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_abilities))));
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_thoughts_gossip))).check(
                isCompletelyAbove(withItemText(getTargetContext().getResources().getString(R.string.kindness_thoughts_wishes))));
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_actions_cake))).check(
                isCompletelyAbove(withItemText(getTargetContext().getResources().getString(R.string.kindness_actions_assist))));
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_self_volunteer))).check(
                isCompletelyAbove(withItemText(getTargetContext().getResources().getString(R.string.kindness_self_compliment))));

        // Delete entries to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_love));
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_abilities));
    }

    @Test
    public void goBackFromAddKindnessEntryScreen_endsAtKindnessDiary() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Click on the add kindness entry button
        onView(withId(R.id.create_kindness_button)).perform(click());

        // Click back
        onView(withContentDescription("Navigate up")).perform(click());

        // Verify Kindness Diary is displayed
        onView(withId(R.id.kindness_view)).check(matches(isDisplayed()));
    }

    @Test
    public void swipeOnEntry_ShowsDeletionDialog() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Swipe the entry
        onView(withId(R.id.kindness_view))
                .perform(scrollTo(hasDescendant(withText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Verify deletion dialog message is shown
        onView(withText(R.string.dialog_delete_entry)).check(matches(isDisplayed()));

        // Delete it to clean up
        onView(withText(R.string.ok_delete_entry)).perform(click());
    }

    @Test
    public void cancelEntryDeletion_LeavesEntryInView() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Swipe the entry
        onView(withId(R.id.kindness_view))
                .perform(scrollTo(hasDescendant(withText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Verify deletion dialog message is shown
        onView(withText(R.string.dialog_delete_entry)).check(matches(isDisplayed()));

        // Cancel Entry deletion
        onView(withText(R.string.cancel_delete_entry)).perform(click());

        // Verify dialog message gone and entry still in view
        onView(withText(R.string.dialog_delete_entry)).check(doesNotExist());
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))).check(matches(isDisplayed()));

        // Delete it anyway to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_call));
    }

    @Test
    public void confirmEntryDeletion_RemovesEntryFromView() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Swipe the entry
        onView(withId(R.id.kindness_view))
                .perform(scrollTo(hasDescendant(withText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Verify deletion dialog message is shown
        onView(withText(R.string.dialog_delete_entry)).check(matches(isDisplayed()));

        // Delete it to clean up
        onView(withText(R.string.ok_delete_entry)).perform(click());

        // Verify dialog message gone and entry still in view
        onView(withText(R.string.dialog_delete_entry)).check(doesNotExist());
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))).check(doesNotExist());
    }

    @Test
    public void dismissEntryDeletion_LeavesEntryInView() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Swipe the entry
        onView(withId(R.id.kindness_view))
                .perform(scrollTo(hasDescendant(withText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));

        // Verify deletion dialog message is shown
        onView(withText(R.string.dialog_delete_entry)).check(matches(isDisplayed()));

        // Cancel Entry deletion
        onView(withText(R.string.dialog_delete_entry)).perform(pressBack());

        // Verify dialog message gone and entry still in view
        onView(withText(R.string.dialog_delete_entry)).check(doesNotExist());
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))).check(matches(isDisplayed()));

        // Delete it anyway to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_call));
    }

    @Test
    public void clickEntry_DisplaysUpdateEntryUI() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Click on the entry
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))).perform(click());

        // Verify the update screen is displayed on screen
        onView(withId(R.id.editkindnessentry_pager)).check(matches(isDisplayed()));

        // Cancel
        onView(withId(R.id.editkindnessentry_pager)).perform(pressBack());

        // delete the entry to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_call));
    }

    @Test
    public void cancelUpdate_ShowsKindnessDiaryWithUnchangedEntry() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Click on the entry
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))).perform(click());

        // Verify the update screen is displayed on screen
        onView(withId(R.id.editkindnessentry_pager)).check(matches(isDisplayed()));

        // Cancel
        onView(withId(R.id.editkindnessentry_cancel_button)).perform(click());

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.kindness_view)).perform(
                scrollTo(hasDescendant(withText(getTargetContext().getResources().getString(R.string.kindness_words_call)))));

        // Verify entry is displayed on screen
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))).check(matches(isDisplayed()));

        // delete the entry to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_call));
    }

    @Test
    public void updateEntry_ShowsKindnessDiaryWithUpdatedEntry() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Click on the entry
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))).perform(click());

        // Verify the update screen is displayed on screen
        onView(withId(R.id.editkindnessentry_pager)).check(matches(isDisplayed()));

        // Hit next to get to actions screen
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());

        // Select new kindness action
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_actions_traffic)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_actions_message))))
                .perform(click());

        // Hit save
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.kindness_view)).perform(
                scrollTo(hasDescendant(withText(getTargetContext().getResources().getString(R.string.kindness_words_call)))));

        // Verify entry is displayed on screen
        onView(withItemText(getTargetContext().getResources().getString(R.string.kindness_actions_traffic))).check(matches(isDisplayed()));

        // delete the entry to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_call));
    }

    @Test
    public void completeKindnessEntry_marksDayComplete() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Click on the entry checkbox
        onView(
                allOf(
                        withId(R.id.kindness_card_checkbox),
                        hasSibling(hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))))))
                .perform(click());

        // Verify the checkbox is ticked and the card is the right colour
        onView(
                allOf(
                        withId(R.id.kindness_card_checkbox),
                        hasSibling(hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))))))
                .check(matches(isChecked()));
        onView(allOf(withId(R.id.kindness_card_view),
                hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .check(matches(viewWithBackgroundColour(getTargetContext().getResources().getColor(R.color.completeKindness))));

        // Change tabs and back again
        onView(withText(R.string.listen_tab_name)).perform(click());
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Verify again
        onView(
                allOf(
                        withId(R.id.kindness_card_checkbox),
                        hasSibling(hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))))))
                .check(matches(isChecked()));
        onView(allOf(withId(R.id.kindness_card_view),
                hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .check(matches(viewWithBackgroundColour(getTargetContext().getResources().getColor(R.color.completeKindness))));

        // delete the entry to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_call));
    }

    @Test
    public void incompleteKindnessEntry_marksDayIncomplete() {
        mEditor.putLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, System.currentTimeMillis());
        mEditor.apply();

        mMainActivityTestRule.launchActivity(null);

        // Click on the kindness tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Create a generic entry
        createKindnessEntry(R.string.kindness_words_call, R.string.kindness_thoughts_gossip,
                R.string.kindness_actions_cake, R.string.kindness_self_draw);

        // Click on the entry checkbox
        onView(
                allOf(
                        withId(R.id.kindness_card_checkbox),
                        hasSibling(hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))))))
                .perform(click());

        // Verify the checkbox is ticked and the card is the right colour
        onView(
                allOf(
                        withId(R.id.kindness_card_checkbox),
                        hasSibling(hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))))))
                .check(matches(isChecked()));
        onView(allOf(withId(R.id.kindness_card_view),
                    hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .check(matches(viewWithBackgroundColour(getTargetContext().getResources().getColor(R.color.completeKindness))));

        // Click on the entry checkbox again
        onView(
                allOf(
                        withId(R.id.kindness_card_checkbox),
                        hasSibling(hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))))))
                .perform(click());

        // Verify the checkbox is NOT ticked and the card is the right colour
        onView(
                allOf(
                        withId(R.id.kindness_card_checkbox),
                        hasSibling(hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))))))
                .check(matches(not(isChecked())));
        onView(allOf(withId(R.id.kindness_card_view),
                hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .check(matches(viewWithBackgroundColour(getTargetContext().getResources().getColor(R.color.incompleteKindness))));

        // Change tabs and back again
        onView(withText(R.string.listen_tab_name)).perform(click());
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Verify again
        onView(
                allOf(
                        withId(R.id.kindness_card_checkbox),
                        hasSibling(hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call))))))
                .check(matches(not(isChecked())));
        onView(allOf(withId(R.id.kindness_card_view),
                hasDescendant(withItemText(getTargetContext().getResources().getString(R.string.kindness_words_call)))))
                .check(matches(viewWithBackgroundColour(getTargetContext().getResources().getColor(R.color.incompleteKindness))));

        // delete the entry to clean up
        deleteKindnessEntry(getTargetContext().getResources().getString(R.string.kindness_words_call));
    }


}
