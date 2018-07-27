package com.hedgehogproductions.therapyguide;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.hedgehogproductions.therapyguide.editkindnessentry.KindnessItem;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;


public class Matchers {
    /**
     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
     *
     * <p>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link RecyclerView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    public static Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    // Custom matcher to match a toast
    public static Matcher<Root> showsToast() {
        return new TypeSafeMatcher<Root>() {

            @Override
            public boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if (type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    // windowToken == appToken means this window isn't contained by any other windows.
                    // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                    return windowToken == appToken;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }
        };
    }

    // Custom matcher to match kindness items with their text
    public static Matcher<Object> kindnessWithText(String expectedText) {
        Checks.checkNotNull(expectedText);
        return kindnessWithText(equalTo(expectedText));
    }

    private static Matcher<Object> kindnessWithText(final Matcher<String> itemMatcher) {
        Checks.checkNotNull(itemMatcher);

        return new BoundedMatcher<Object, KindnessItem>(KindnessItem.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("Kindness: ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(KindnessItem kindnessItem) {
                return itemMatcher.matches(kindnessItem.getText());
            }
        };
    }

    // Custom matcher to match card background colour
    public static Matcher<Object> viewWithBackgroundColour(int expectedColor) {
        return viewWithBackgroundColour(equalTo(expectedColor));
    }

    private static Matcher<Object> viewWithBackgroundColour(final Matcher<Integer> itemMatcher) {
        Checks.checkNotNull(itemMatcher);

        return new BoundedMatcher<Object, CardView>(CardView.class) {
            @Override
            public void describeTo(final Description description) {
                description.appendText("With background colour: ");
                itemMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(final CardView cardView) {
                return itemMatcher.matches(cardView.getCardBackgroundColor().getDefaultColor());
            }
        };
    }
}
