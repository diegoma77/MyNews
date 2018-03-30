package com.example.android.mynews.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.TextInputEditText;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.mynews.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.supportsInputMethods;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Diego Fajardo on 30/03/2018.
 */
public class SearchArticlesActivityTest {
    /**
     * This RULE specifies that this activity is launched
     */
    //Always make this public
    @Rule
    public ActivityTestRule<SearchArticlesActivity> searchArticlesActivityActivityTestRule =
            new ActivityTestRule<SearchArticlesActivity>(SearchArticlesActivity.class);

    private SearchArticlesActivity mActivity = null;

    Instrumentation.ActivityMonitor mainActivityMonitor =
            getInstrumentation().addMonitor(
                    MainActivity.class.getName(),
                    null,
                    false);

    Instrumentation.ActivityMonitor displaySearchArticlesActivityMonitor =
            getInstrumentation().addMonitor(
                    DisplaySearchArticlesActivity.class.getName(),
                    null,
                    false);

    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = searchArticlesActivityActivityTestRule.getActivity();

    }

    @Test
    public void testThatViewsAreNotNullAndWritableOrCheckable() {

        /** If we can find the views, we can conclude that the activity is launched successfully */

        //Inside date picker
        View view11 = mActivity.findViewById(R.id.search_beginDate_date);
        View view12 = mActivity.findViewById(R.id.search_endDate_date);

        onView(withId(R.id.search_button)).check(matches(isDisplayed()));
        onView(withId(R.id.search_beginDate_date)).check(matches(isDisplayed()));
        onView(withId(R.id.search_endDate_date)).check(matches(isDisplayed()));

        onView(withId(R.id.search_text_input_edit_text))
                .perform(typeText("This is a test"));

        onView(withId(R.id.search_checkBox_arts)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_business)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_politics)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_sports)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_travel)).perform(click()).check(matches(isChecked()));

        onView(withId(R.id.search_button)).perform(click());

        // TODO: 30/03/2018 Test fails

        Activity displaySearchArticlesActivity = getInstrumentation()
                .waitForMonitorWithTimeout(displaySearchArticlesActivityMonitor,
                        5000);

        assertNotNull(displaySearchArticlesActivity);

        displaySearchArticlesActivity.finish();

    }




    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }
}






