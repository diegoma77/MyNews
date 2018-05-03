package com.example.android.mynews.activities.tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.android.mynews.R;
import com.example.android.mynews.activities.MainActivity;
import com.example.android.mynews.activities.WebViewMainActivity;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;

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
import static org.junit.Assert.assertNotNull;

/**
 * Created by Diego Fajardo on 03/05/2018.
 */

public class WebViewMainActivityTest {

    @Rule
    public ActivityTestRule<WebViewMainActivity> webViewMainActivityTestRule =
            new ActivityTestRule<WebViewMainActivity>(WebViewMainActivity.class);

    private WebViewMainActivity mActivity = null;

    Instrumentation.ActivityMonitor mainActivityMonitor =
            getInstrumentation().addMonitor(
                    MainActivity.class.getName(),
                    null,
                    false);

    private String articleUrl;

    private Context targetContext;

    private Intent intent;

    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = webViewMainActivityTestRule.getActivity();

        articleUrl = "https://www.nytimes.com/1990/10/19/business/textron-posts-gain-in-profits.html";

        targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        intent = new Intent(targetContext, WebViewMainActivity.class);
        intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, articleUrl);

    }

    @Test
    public void testHomeButton() {

        webViewMainActivityTestRule.launchActivity(intent);

        onView(withContentDescription(R.string.go_back)).perform(click());
        Activity displaySearcgArticlesActivity = getInstrumentation().waitForMonitorWithTimeout(mainActivityMonitor, 5000);
        assertNotNull(displaySearcgArticlesActivity);
        displaySearcgArticlesActivity.finish();

    }

    @Test
    public void testWebViewDisplayed () {

        webViewMainActivityTestRule.launchActivity(intent);

        onView(withId(R.id.webView)).check(matches(isDisplayed()));

    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }
}
