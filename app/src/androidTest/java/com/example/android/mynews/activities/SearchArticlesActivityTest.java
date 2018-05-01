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
import static android.support.test.espresso.action.ViewActions.clearText;
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

    private Instrumentation.ActivityMonitor mainActivityMonitor =
            getInstrumentation().addMonitor(
                    MainActivity.class.getName(),
                    null,
                    false);

    private Instrumentation.ActivityMonitor displaySearchArticlesActivityMonitor =
            getInstrumentation().addMonitor(
                    DisplaySearchArticlesActivity.class.getName(),
                    null,
                    false);

    private CheckBox cb_arts;
    private CheckBox cb_business;
    private CheckBox cb_entrepreneurs;
    private CheckBox cb_politics;
    private CheckBox cb_sports;
    private CheckBox cb_travel;

    private List<String> listOfSections;

    private TextInputEditText mTextInputEditText;

    private View endDateButton;
    private View beginDateButton;

    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = searchArticlesActivityActivityTestRule.getActivity();

        //Checkboxes
        cb_arts = (CheckBox) mActivity.findViewById(R.id.search_checkBox_arts);
        cb_business = (CheckBox) mActivity.findViewById(R.id.search_checkBox_business);
        cb_entrepreneurs = (CheckBox) mActivity.findViewById(R.id.search_checkBox_entrepeneurs);
        cb_politics = (CheckBox) mActivity.findViewById(R.id.search_checkBox_politics);
        cb_sports = (CheckBox) mActivity.findViewById(R.id.search_checkBox_sports);
        cb_travel = (CheckBox) mActivity.findViewById(R.id.search_checkBox_travel);

        listOfSections = mActivity.getListOfSections();

        //TextInputEditText
        mTextInputEditText = (TextInputEditText) mActivity.findViewById(R.id.search_text_input_edit_text);

        //Views (buttons)
        endDateButton = mActivity.findViewById(R.id.search_button_endDate);
        beginDateButton = mActivity.findViewById(R.id.search_button_beginDate);

    }

    @Test
    public void testViewsAreDisplayed() {

        //If we can find the views, we can conclude that the activity is launched successfully
        onView(withId(R.id.search_button)).check(matches(isDisplayed()));

        onView(withId(R.id.search_beginDate_date)).check(matches(isDisplayed()));
        onView(withId(R.id.search_endDate_date)).check(matches(isDisplayed()));

        onView(withId(R.id.search_text_input_edit_text)).check(matches(isDisplayed()));

        onView(withId(R.id.search_checkBox_arts)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.search_checkBox_business)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.search_checkBox_politics)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.search_checkBox_sports)).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.search_checkBox_travel)).perform(click()).check(matches(isDisplayed()));

    }

    @Test
    public void testViewsAreWritableAndCheckable () {

        //Clearing the TextInputEditText and unchecking the checkboxes
        onView(withId(R.id.search_text_input_edit_text)).perform(clearText());

        if (cb_arts.isChecked()) onView(withId(R.id.search_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.search_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.search_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.search_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.search_checkBox_travel)).perform(click());

        //Writing something on the TextInputEditText and clicking the checkboxes
        onView(withId(R.id.search_text_input_edit_text))
                .perform(typeText("This is a test"))
                .check(matches(isDisplayed()));

        //Checking the checkboxes are checked when clicked
        onView(withId(R.id.search_checkBox_arts)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_business)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_politics)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_sports)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.search_checkBox_travel)).perform(click()).check(matches(isChecked()));

        //We uncheck all the checkboxes
        if (cb_arts.isChecked()) onView(withId(R.id.search_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.search_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.search_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.search_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.search_checkBox_travel)).perform(click());

    }

    @Test
    public void testBeginDateAlertDialogWorks() {

        //We check that the button is available and clickable
        onView(withId(R.id.search_button_beginDate)).check(matches(isDisplayed()))
                .check(matches(isClickable()));

        //We store the content of the tv (which is inside the button) in a String
        // to compare this String with the one that will be get from the modified
        // text of the TextView when a new Date is set.
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

        //We check that the button is available and clickable
        onView(withId(R.id.search_button_endDate)).check(matches(isDisplayed()))
                .check(matches(isClickable()));

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

        //Clearing the TextInputEditText and unchecking the checkboxes
        onView(withId(R.id.search_text_input_edit_text)).perform(clearText());

        if (cb_arts.isChecked()) onView(withId(R.id.search_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.search_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.search_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.search_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.search_checkBox_travel)).perform(click());

        //TESTING THE SEARCH QUERY URL TRANSFORMATION
        onView(withId(R.id.search_text_input_edit_text))
                .perform(typeText("This is a test"));

        String searchQueryAfterTransformation = mActivity.getSearchQueryAndAdaptForUrl();

        //If the String has no spaces between the words then it's suitable for the Url
        assertTrue(!searchQueryAfterTransformation.contains(" "));

        //TESTING THE CHECKBOXES TO URL TRANSFORMATION
        onView(withId(R.id.search_checkBox_arts)).perform(click());
        onView(withId(R.id.search_checkBox_business)).perform(click());
        onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click());
        onView(withId(R.id.search_checkBox_politics)).perform(click());
        onView(withId(R.id.search_checkBox_sports)).perform(click());
        onView(withId(R.id.search_checkBox_travel)).perform(click());

        String checkboxesToString = mActivity
                .getNewDeskValuesAndAdaptForUrl(mActivity.getListOfSections());

        //If the String has no spaces between the words then it's suitable for the Url
        assertTrue(!checkboxesToString.contains(" "));

        //We uncheck all the checkboxes
        if (cb_arts.isChecked()) onView(withId(R.id.search_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.search_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.search_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.search_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.search_checkBox_travel)).perform(click());

    }

    @Test
    public void testMainActivityIsLoaded() {

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
    public void testDisplaySearchArticlesActivityIsLoaded () {

        //Clearing the TextInputEditText and unchecking the checkboxes
        onView(withId(R.id.search_text_input_edit_text)).perform(clearText());

        if (cb_arts.isChecked()) onView(withId(R.id.search_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.search_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.search_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.search_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.search_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.search_checkBox_travel)).perform(click());

        //Checking one checkbox
        onView(withId(R.id.search_checkBox_politics)).perform(click());

        //Clicking Search button
        onView(withId(R.id.search_button)).perform(click());

        Activity displaySearchArticles = getInstrumentation().waitForMonitorWithTimeout(displaySearchArticlesActivityMonitor, 5000);

        assertNotNull(displaySearchArticles);

        displaySearchArticles.finish();

    }

    /** Use UiAutomator */
    public void testProgressBarIsDisplayed() {

    }




    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }

}






