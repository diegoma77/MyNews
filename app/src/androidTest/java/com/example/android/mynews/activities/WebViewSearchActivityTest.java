package com.example.android.mynews.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.android.mynews.R;
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

public class WebViewSearchActivityTest {

    @Rule
    public ActivityTestRule<WebViewSearchActivity> webViewSearchActivityTestRule =
            new ActivityTestRule<WebViewSearchActivity>(WebViewSearchActivity.class, true, false);

    private WebViewSearchActivity mActivity = null;

    Instrumentation.ActivityMonitor displaySearchArticlesActivityMonitor =
            getInstrumentation().addMonitor(
                    DisplaySearchArticlesActivity.class.getName(),
                    null,
                    false);

    private String articleUrl;

    private Context targetContext;

    private Intent intent;

    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = webViewSearchActivityTestRule.getActivity();

        articleUrl = "https://www.nytimes.com/1990/10/19/business/textron-posts-gain-in-profits.html";

        targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        intent = new Intent(targetContext, WebViewSearchActivity.class);
        intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, articleUrl);

    }

    @Test
    public void testHomeButton() {

        webViewSearchActivityTestRule.launchActivity(intent);

        onView(withContentDescription(R.string.go_back)).perform(click());
        Activity displaySearcgArticlesActivity = getInstrumentation().waitForMonitorWithTimeout(displaySearchArticlesActivityMonitor, 5000);
        assertNotNull(displaySearcgArticlesActivity);
        displaySearcgArticlesActivity.finish();

    }

    @Test
    public void testWebViewDisplayed () {

        webViewSearchActivityTestRule.launchActivity(intent);

        onView(withId(R.id.webView)).check(matches(isDisplayed()));

    }


    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }


}
