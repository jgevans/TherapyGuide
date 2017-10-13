package com.hedgehogproductions.therapyguide.settings;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.PreferenceMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.SettingsActions;
import com.hedgehogproductions.therapyguide.alarmhandler.AlarmHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    @After
    public void clearSharedPrefs() {
        SharedPreferences prefs = getInstrumentation().getTargetContext()
                .getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    @Test
    public void mainScreenShowsSettingsButton() {
        // Verify settings button is displayed on screen
        onView(withId(R.id.settings)).check(matches(isDisplayed()));

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Verify the button is still displayed is displayed on screen
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }

    @Test
    public void pressSettingsShowsCorrectSettings() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Verify the toolbar and back button is shown
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withContentDescription("Navigate up")).check(matches(isDisplayed()));

        // Verify the diary alert setting is shown
        onData(PreferenceMatchers.withKey("pref_diary_alert")).check(matches(isDisplayed()));
        onData(PreferenceMatchers.withKey("pref_diary_alert"))
                .onChildView(withText(R.string.pref_diary_alert_summary_off)).check(matches(isDisplayed()));

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());
        onData(PreferenceMatchers.withKey("pref_diary_alert"))
                .onChildView(withText(R.string.pref_diary_alert_summary_on)).check(matches(isDisplayed()));

        // Verify the diary alert time is shown
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).check(matches(isDisplayed()));
        onData(PreferenceMatchers.withKey("pref_diary_alert_time"))
                .onChildView(withText("16:00")).check(matches(isDisplayed()));
    }

    @Test
    public void uncheckRemindersDisablesReminderTime() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // Verify the diary alert is enabled
        onData(PreferenceMatchers.withKey("pref_diary_alert"))
                .onChildView(withClassName(is(AppCompatCheckBox.class.getName())))
                .check(matches(isChecked()));

        // Disable the diary alert and verify
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());
        onData(PreferenceMatchers.withKey("pref_diary_alert"))
                .onChildView(withClassName(is(AppCompatCheckBox.class.getName())))
                .check(matches(not(isChecked())));

        // Verify diary alert time is disabled
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).check(matches(not(isEnabled())));
    }

    @Test
    public void pressBackArrowExitsSettings() {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Click back
        onView(withContentDescription("Navigate up")).perform(click());

        // Verify Diary is displayed
        onView(withId(R.id.diary_view)).check(matches(isDisplayed()));
    }

    @Test
    public void pressBackExitsSettings() {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Click back
        onView(withId(R.id.toolbar)).perform(pressBack());

        // Verify Diary is displayed
        onView(withId(R.id.diary_view)).check(matches(isDisplayed()));
    }

    @Test
    public void pressReminderTimeSettingShowsDialog() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // Click on reminder time setting
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).perform(click());

        // Verify buttons are displayed
        onView(withText(R.string.pref_diary_alert_time_save_button)).check(matches(isDisplayed()));
        onView(withText(R.string.pref_diary_alert_time_cancel_button)).check(matches(isDisplayed()));

        // Verify a TimePicker is displayed
        onView(withClassName(equalTo(TimePicker.class.getName()))).check(matches(isDisplayed()));

        // Verify the default time is shown
        if (android.os.Build.VERSION.SDK_INT < 21) {
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("hour"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("4"))))).check(matches(isDisplayed()));
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("minute"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("00"))))).check(matches(isDisplayed()));
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("amPm"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("PM"))))).check(matches(isDisplayed()));
        }
        else {
            onView(withResourceName("hours")).check(matches(withText("4")));
            onView(withResourceName("minutes")).check(matches(withText("00")));
            onView(withResourceName("pm_label")).check(matches(isChecked()));
        }
    }

    @Test
    public void backOnReminderTimeDialogDoesNotRecordTime() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // Click on reminder time setting
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).perform(click());

        // Set the time
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(SettingsActions.setTime(5, 30));

        // Rotate the view
        rotateScreen();

        // Click back
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(pressBack());

        // Verify old time is still displayed
        onData(PreferenceMatchers.withKey("pref_diary_alert_time"))
                .onChildView(withText("16:00")).check(matches(isDisplayed()));
    }

    @Test
    public void cancelReminderTimeDialogDoesNotRecordTime() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // Click on reminder time setting
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).perform(click());

        // Set the time
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(SettingsActions.setTime(5, 30));

        // Rotate the view
        rotateScreen();

        // Click cancel
        onView(withText("Cancel")).perform(click());

        // Verify old time is still displayed
        onData(PreferenceMatchers.withKey("pref_diary_alert_time"))
                .onChildView(withText("16:00")).check(matches(isDisplayed()));
    }

    @Test
    public void saveNewReminderTimeSavesInPreferences() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // Click on reminder time setting
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).perform(click());

        // Set the time
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(SettingsActions.setTime(5, 30));

        // Click save
        onView(withText("Save")).perform(click());

        // Verify new time displayed
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).check(matches(isDisplayed()));
        onData(PreferenceMatchers.withKey("pref_diary_alert_time"))
                .onChildView(withText("05:30")).check(matches(isDisplayed()));

        // Click on reminder time setting
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).perform(click());

        // Verify TimePickerPreference displays saved time
        if (android.os.Build.VERSION.SDK_INT < 21) {
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("hour"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("5"))))).check(matches(isDisplayed()));
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("minute"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("30"))))).check(matches(isDisplayed()));
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("amPm"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("AM"))))).check(matches(isDisplayed()));
        }
        else {
            onView(withResourceName("hours")).check(matches(withText("5")));
            onView(withResourceName("minutes")).check(matches(withText("30")));
            onView(withResourceName("am_label")).check(matches(isChecked()));
        }
    }

    @Test
    public void rotateMaintainsSetTime() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // Click on reminder time setting
        onData(PreferenceMatchers.withKey("pref_diary_alert_time")).perform(click());

        // Set the time
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(SettingsActions.setTime(5, 30));

        // Rotate the view
        rotateScreen();

        // Verify the time is still the same
        if (android.os.Build.VERSION.SDK_INT < 21) {
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("hour"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("5"))))).check(matches(isDisplayed()));
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("minute"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("30"))))).check(matches(isDisplayed()));
            onView(allOf(
                    is(instanceOf(NumberPicker.class)),
                    withResourceName("amPm"),
                    hasDescendant(allOf(
                            withResourceName("numberpicker_input"),
                            withText("AM"))))).check(matches(isDisplayed()));
        }
        else {
            onView(withResourceName("hours")).check(matches(withText("5")));
            onView(withResourceName("minutes")).check(matches(withText("30")));
            onView(withResourceName("am_label")).check(matches(isChecked()));
        }
    }

    @Test
    public void reminderOff_NoAlarmActive() {
        assertFalse(isAlarmSet());
    }

    @Test
    public void turnOnReminder_AlarmActive() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // TODO Investigate Robolectric for testing alarmmanager
        // Check an alarm is set
        assertTrue(isAlarmSet());
    }

    @Test
    public void turnOnReminderThenTurnOff_NoAlarmActive() {
        // Click on settings
        onView(withId(R.id.settings)).perform(click());

        // Enable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // Check an alarm is set
        assertTrue(isAlarmSet());

        // Disable the diary alert
        onData(PreferenceMatchers.withKey("pref_diary_alert")).perform(click());

        // Check an alarm is not set
        assertFalse(isAlarmSet());
    }

    private void rotateScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;

        getCurrentActivity().setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getInstrumentation().waitForIdleSync();
    }

    private Activity getCurrentActivity() {
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity[0] = resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity[0];
    }

    private boolean isAlarmSet() {
        Context context = getInstrumentation().getTargetContext();
        Intent intent = new Intent(context, AlarmHandler.class);
        PendingIntent service = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_NO_CREATE
        );
        return service != null;
    }
}