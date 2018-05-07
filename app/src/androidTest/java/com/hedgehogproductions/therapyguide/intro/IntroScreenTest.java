package com.hedgehogproductions.therapyguide.intro;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for the intro screen, the screen which contains the introduction to the application
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntroScreenTest {

    private static final int INTRO_SCREEN_COUNT = 4;

    /* Rule to launch activity under test */
    @Rule
    public final ActivityTestRule<IntroActivity> mIntroActivityTestRule =
            new ActivityTestRule<>(IntroActivity.class);


    @Test
    public void intro_showsIntroduction() {

        // Verify the correct fields and buttons are shown
        onView(withId(R.id.intro_next_button)).check(matches(isDisplayed()));
        onView(withId(R.id.intro_skip_button)).check(matches(isDisplayed()));
        onView(withText(R.string.intro_next_button_text)).check(matches(isDisplayed()));

        onView(withId(R.id.intro_image)).check(matches(isDisplayed()));
        onView(withText(R.string.intro_intro_text1)).check(matches(isDisplayed()));

    }

    @Test
    public void nextToEnd_showsDone() {
        for( int screen = 0; screen < INTRO_SCREEN_COUNT-1; ++screen ) {
            onView(withId(R.id.intro_next_button)).perform(click());
        }

        onView(withText(R.string.intro_done_button_text)).check(matches(isDisplayed()));
    }

    @Test
    public void swipeToEnd_showsDone() {
        for( int screen = 0; screen < INTRO_SCREEN_COUNT; ++screen ) {
            onView(withId(R.id.intro_pager)).perform(swipeLeft());
        }
        onView(withText(R.string.intro_done_button_text)).check(matches(isDisplayed()));
    }

    @Test
    public void swipeToSleep_showsSleep() {
        onView(withId(R.id.intro_pager)).perform(swipeLeft());

        onView(withId(R.id.sleep_image)).check(matches(isDisplayed()));
        onView(withText(R.string.intro_sleep_text1)).check(matches(isDisplayed()));
    }

    @Test
    public void swipeToPositivity_showsPositivity() {
        onView(withId(R.id.intro_pager)).perform(swipeLeft());
        onView(withId(R.id.intro_pager)).perform(swipeLeft());

        onView(withId(R.id.positivity_image)).check(matches(isDisplayed()));
        onView(withText(R.string.intro_positivity_text1)).check(matches(isDisplayed()));
    }

    @Test
    public void swipeToKindness_showsKindness() {
        onView(withId(R.id.intro_pager)).perform(swipeLeft());
        onView(withId(R.id.intro_pager)).perform(swipeLeft());
        onView(withId(R.id.intro_pager)).perform(swipeLeft());

        onView(withId(R.id.kindness_image)).check(matches(isDisplayed()));
        onView(withText(R.string.intro_kindness_text1)).check(matches(isDisplayed()));
    }

    @Test
    public void skip_exitsIntro() {
        onView(withId(R.id.intro_skip_button)).perform(click());
        assertTrue(mIntroActivityTestRule.getActivity().isFinishing());
    }

    @Test
    public void swipeToEndAndFinish_exitsIntro() {
        for( int screen = 0; screen < INTRO_SCREEN_COUNT; ++screen ) {
            onView(withId(R.id.intro_pager)).perform(swipeLeft());
        }
        onView(withText(R.string.intro_done_button_text)).perform(click());
        assertTrue(mIntroActivityTestRule.getActivity().isFinishing());
    }
}