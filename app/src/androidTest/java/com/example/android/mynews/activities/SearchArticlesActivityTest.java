package com.example.android.mynews.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.TextInputEditText;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.mynews.R;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.supportsInputMethods;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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
    public void testThatViewsAreNotNullWritableOrCheckableAndDisplaySearchArticlesActivityIsLoaded() {

        /** If we can find the views, we can conclude that the activity is launched successfully */

        onView(withId(R.id.search_button)).check(matches(isClickable()));
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

        Activity displaySearchArticlesActivity = getInstrumentation()
                .waitForMonitorWithTimeout(displaySearchArticlesActivityMonitor,
                        5000);

        assertNotNull(displaySearchArticlesActivity);

        displaySearchArticlesActivity.finish();

    }

    @Test
    public void testMainActivityIsLoaded() {

        View view1 = mActivity.findViewById(android.R.id.home);

        onView(withContentDescription(
                R.string.home_button_search_activity_content_description))
                .perform(click());

        Activity mainActivity = getInstrumentation()
                .waitForMonitorWithTimeout(mainActivityMonitor,
                        5000);

        assertNotNull(mainActivity);

        mainActivity.finish();

    }


    @Test
    public void testBeginDateAlertDialogWorks() {

        //We check that the button is available
        View view11 = mActivity.findViewById(R.id.search_button_beginDate);
        onView(withId(R.id.search_button_beginDate))
                .check(matches(isClickable()));

        assertNotNull(view11);

        //We store the content of the tv in a String to compare this String with the
        //one that will be get from the modified text of the TextView when a new Date
        //is set.
        TextView tv = (TextView) mActivity.findViewById(R.id.search_beginDate_date);
        String tvBeforeClick = tv.getText().toString();

        onView(withId(R.id.search_button_beginDate)).check(matches(isDisplayed())).perform(click());

        //We check that the alert Dialog is displayed checking that the two buttons are displayed
        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click());
        String tvAfterClick = tv.getText().toString();

        //We use two strings to prove that, after clicking OK in the alertDialog,
        //the date is updated (to today, in this case). It is updated because the
        //text view content is not the same, which means it has changed.
        assertNotSame("Should not be same object", tvBeforeClick, tvAfterClick);

    }

    @Test
    public void testEndDateAlertDialogWorks() {

        //We check that the button is available
        View view11 = mActivity.findViewById(R.id.search_button_endDate);
        onView(withId(R.id.search_button_endDate))
                .check(matches(isClickable()));

        assertNotNull(view11);

        //We store the content of the tv in a String to compare this String with the
        //one that will be get from the modified text of the TextView when a new Date
        //is set.
        TextView tv = (TextView) mActivity.findViewById(R.id.search_endDate_date);
        String tvBeforeClick = tv.getText().toString();

        onView(withId(R.id.search_button_endDate)).check(matches(isDisplayed())).perform(click());

        //We check that the alert Dialog is displayed checking that the two buttons are displayed
        onView(withId(android.R.id.button1)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).check(matches(isDisplayed()));

        onView(withId(android.R.id.button1)).perform(click());
        String tvAfterClick = tv.getText().toString();

        //We use two strings to prove that, after clicking OK in the alertDialog,
        //the date is updated (to today, in this case). It is updated because the
        //text view content is not the same, which means it has changed.
        assertNotSame("Should not be same object", tvBeforeClick, tvAfterClick);

    }

    @Test
    public void testUrlAdapters() {

        //TESTING THE SEARCH QUERY URL TRANSFORMATION
        TextView tvSearchQuery = (TextView) mActivity.findViewById(
                R.id.search_text_input_edit_text);

        onView(withId(R.id.search_text_input_edit_text))
                .perform(typeText("This is a test"));

        String searchQueryAfterTransformation = mActivity.getSearchQueryAndAdaptForUrl();

        //If the String has no spaces between the words then it's suitable for the Url
        assertTrue(!searchQueryAfterTransformation.contains(" "));

        //TESTING THE CHECKBOXES TO URL TRANSFORMATION
        onView(withId(R.id.search_checkBox_arts)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_business)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_politics)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_sports)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_travel)).perform(click()).check(matches(isChecked()));

        List<String> mockOfSectionsList = new ArrayList<>();
        mockOfSectionsList.add("Arts");
        mockOfSectionsList.add("Business");
        mockOfSectionsList.add("Entrepeneurs");
        mockOfSectionsList.add("Politics");
        mockOfSectionsList.add("Sports");
        mockOfSectionsList.add("Travel");

        String checkboxesToString = mActivity
                .getNewDeskValuesAndAdaptForUrl(mockOfSectionsList);

        //If the String has no spaces between the words then it's suitable for the Url
        assertTrue(!checkboxesToString.contains(" "));

    }


    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }

}






