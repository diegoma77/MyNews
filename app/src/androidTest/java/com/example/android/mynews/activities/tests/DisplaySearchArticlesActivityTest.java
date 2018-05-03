package com.example.android.mynews.activities.tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.TextView;

import com.example.android.mynews.R;
import com.example.android.mynews.activities.DisplaySearchArticlesActivity;
import com.example.android.mynews.activities.SearchArticlesActivity;
import com.example.android.mynews.activities.WebViewSearchActivity;
import com.example.android.mynews.activities.helpers.RecyclerViewMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Diego Fajardo on 03/05/2018.
 */

public class DisplaySearchArticlesActivityTest {

    /**
     * This RULE specifies that this activity is launched
     */
    //Always make this public
    @Rule
    public ActivityTestRule<DisplaySearchArticlesActivity> displaySearchArticlesActivityTestRule =
            new ActivityTestRule<DisplaySearchArticlesActivity>(DisplaySearchArticlesActivity.class);

    private DisplaySearchArticlesActivity mActivity = null;

    Instrumentation.ActivityMonitor searchArticlesMonitor =
            getInstrumentation().addMonitor(
                    SearchArticlesActivity.class.getName(),
                    null,
                    false);

    /** Add the rest of activities
     * */
    Instrumentation.ActivityMonitor webViewSearchActivityMonitor =
            getInstrumentation().addMonitor(
                    WebViewSearchActivity.class.getName(),
                    null,
                    false);

    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = displaySearchArticlesActivityTestRule.getActivity();
        assertThat(mActivity, notNullValue());

    }

    @Test
    public void testHomeButton() {

        onView(withContentDescription(R.string.go_back)).perform(click());
        Activity searchArticlesActivity = getInstrumentation().waitForMonitorWithTimeout(searchArticlesMonitor, 5000);
        assertNotNull(searchArticlesActivity);
        searchArticlesActivity.finish();

    }

    @Test
    public void testRecyclerView () {

        //We check if the RecyclerView is displayed
        onView(withRecyclerView(R.id.recycler_view).atPosition(0)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.recycler_view).atPosition(4)).check(matches(isDisplayed()));

        //We click an item
        onView(withRecyclerView(R.id.recycler_view).atPosition(4)).perform(click());

        Activity searchArticlesActivity = getInstrumentation().waitForMonitorWithTimeout(webViewSearchActivityMonitor, 5000);
        assertNotNull(searchArticlesActivity);

    }

    private static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }

}
