package com.example.android.mynews.activities.tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import com.example.android.mynews.R;
import com.example.android.mynews.activities.DisplayNotificationsActivity;
import com.example.android.mynews.activities.NotificationsActivity;
import com.example.android.mynews.activities.WebViewNotificationsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Diego Fajardo on 03/05/2018.
 */

public class DisplayNotificationsActivityTest {

    /**
     * This RULE specifies that this activity is launched
     */
    //Always make this public
    @Rule
    public ActivityTestRule<DisplayNotificationsActivity> displayNotificationsActivityTestRule =
            new ActivityTestRule<DisplayNotificationsActivity>(DisplayNotificationsActivity.class);

    private DisplayNotificationsActivity mActivity = null;

    Instrumentation.ActivityMonitor notificationsActivityMonitor =
            getInstrumentation().addMonitor(
                    NotificationsActivity.class.getName(),
                    null,
                    false);

    /** Add the rest of activities
     * */
    Instrumentation.ActivityMonitor webViewNotificationsMonitor =
            getInstrumentation().addMonitor(
                    WebViewNotificationsActivity.class.getName(),
                    null,
                    false);



    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = displayNotificationsActivityTestRule.getActivity();
        assertThat(mActivity, notNullValue());

    }

    @Test
    public void testHomeButton() {

        onView(withContentDescription(R.string.go_back)).perform(click());
        Activity searchArticlesActivity = getInstrumentation().waitForMonitorWithTimeout(notificationsActivityMonitor, 5000);
        assertNotNull(searchArticlesActivity);
        searchArticlesActivity.finish();

    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }

}
