package com.hedgehogproductions.therapyguide.about;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.intro.IntroActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainScreenAboutTest {

    private SharedPreferences.Editor mPreferencesEditor;

    /* Rule to launch activity under test */
    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(
                    MainActivity.class,
                    true,
                    false); // Don't start activity immediately

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setUp() {
        // create a SharedPreferences editor
        mPreferencesEditor = getInstrumentation().getTargetContext()
                .getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE).edit();
    }

    @Test
    public void selectAboutFromMenu_showsAbout() {

        // Set SharedPreferences data
        mPreferencesEditor.putBoolean(IntroActivity.SHOW_INTRO_PREF, false);
        mPreferencesEditor.commit();

        mActivityTestRule.launchActivity(null);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        //Select menu item
        onView(withText(R.string.show_about)).perform(click());

        onView(withId(R.id.about_image)).check(matches(isDisplayed()));
    }

}
