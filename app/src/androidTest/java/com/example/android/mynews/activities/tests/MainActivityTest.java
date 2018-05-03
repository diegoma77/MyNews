package com.example.android.mynews.activities.tests;

import android.app.Instrumentation;
import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.android.mynews.R;
import com.example.android.mynews.activities.DisplayNotificationsActivity;
import com.example.android.mynews.activities.MainActivity;
import com.example.android.mynews.activities.SearchArticlesActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Diego Fajardo on 03/05/2018.
 */

public class MainActivityTest {

    // TODO: 03/05/2018 I have to differentiate between the different fragments.
    // Doesnt let me test.

    /**
     * This RULE specifies that this activity is launched
     */
    //Always make this public
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;

    Instrumentation.ActivityMonitor searchArticlesMonitor =
            getInstrumentation().addMonitor(
                    SearchArticlesActivity.class.getName(),
                    null,
                    false);

    /** Add the rest of activities
     * */
    Instrumentation.ActivityMonitor displayNotificationsActivityMonitor =
            getInstrumentation().addMonitor(
                    DisplayNotificationsActivity.class.getName(),
                    null,
                    false);



    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = mainActivityActivityTestRule.getActivity();

        assertThat(mActivity, notNullValue());



    }

    @Test
    public void testSwipePage() {

        onView(withId(R.id.viewPager))
                .check(matches(isDisplayed()));

        onView(withId(R.id.viewPager))
                .perform(swipeLeft());

    }

    @Test
    public void testCheckTabLayoutIsDisplayed() {

        onView(withId(R.id.sliding_tabs))
                .perform(click())
                .check(matches(isDisplayed()));

    }
    @Test
    public void testCheckTabSwitch() {

        onView(withText("MOST POPULAR")).perform(click()).check(matches(isSelected()));
        SystemClock.sleep(800); //Wait a little until the content is loaded

        onView(withText("TOP STORIES")).perform(click()).check(matches(isSelected()));
        SystemClock.sleep(800); //Wait a little until the content is loaded

        onView(withText("BUSINESS")).perform(click()).check(matches(isSelected()));
        SystemClock.sleep(800); //Wait a little until the content is loaded

        onView(withText("SPORTS")).perform(click()).check(matches(isSelected()));
        SystemClock.sleep(800); //Wait a little until the content is loaded

    }

    @Test
    public void testTopRightButton() {

        // Open the action bar overflow or options menu (depending if the device has or not a hardware menu button.)
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getContext());

        onView(withSpinnerText(containsString("About"))).perform(click());

    }


    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }

}
