package com.example.android.mynews.activities.tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.test.rule.ActivityTestRule;
import android.widget.CheckBox;
import android.widget.Switch;

import com.example.android.mynews.R;
import com.example.android.mynews.activities.DisplayNotificationsActivity;
import com.example.android.mynews.activities.MainActivity;
import com.example.android.mynews.activities.NotificationsActivity;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Diego Fajardo on 12/04/2018.
 */

// TODO: 30/04/2018 Test a notification has been created
// TODO: 30/04/2018 Test DisplayNotificationsActivity Button
// TODO: 01/05/2018 Test MainActivityIsLaunched

public class NotificationsActivityTest {

    /**
     * This RULE specifies that this activity is launched
     */
    //Always make this public
    @Rule
    public ActivityTestRule<NotificationsActivity> notificationsActivityActivityTestRule =
            new ActivityTestRule<NotificationsActivity>(NotificationsActivity.class);

    private NotificationsActivity mActivity = null;

    Instrumentation.ActivityMonitor mainActivityMonitor =
            getInstrumentation().addMonitor(
                    MainActivity.class.getName(),
                    null,
                    false);

    Instrumentation.ActivityMonitor displayNotificationsActivityMonitor =
            getInstrumentation().addMonitor(
                    DisplayNotificationsActivity.class.getName(),
                    null,
                    false);

    private CheckBox cb_arts;
    private CheckBox cb_business;
    private CheckBox cb_entrepreneurs;
    private CheckBox cb_politics;
    private CheckBox cb_sports;
    private CheckBox cb_travel;

    private Switch notif_switch;

    private List<String> listOfQueryAndSections;

    private TextInputEditText mTextInputEditText;

    DatabaseHelper dbH;
    Cursor mCursorQueryOrSection;
    Cursor mCursorSwitch;
    Cursor mCursorArticles;

    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = notificationsActivityActivityTestRule.getActivity();

        //Checkboxes
        cb_arts = (CheckBox) mActivity.findViewById(R.id.notif_checkBox_arts);
        cb_business = (CheckBox) mActivity.findViewById(R.id.notif_checkBox_business);
        cb_entrepreneurs = (CheckBox) mActivity.findViewById(R.id.notif_checkBox_entrepeneurs);
        cb_politics = (CheckBox) mActivity.findViewById(R.id.notif_checkBox_politics);
        cb_sports = (CheckBox) mActivity.findViewById(R.id.notif_checkBox_sports);
        cb_travel = (CheckBox) mActivity.findViewById(R.id.notif_checkBox_travel);

        //Switch
        notif_switch = (Switch) mActivity.findViewById(R.id.notif_switch);

        //We get the list of Query And Sections
        listOfQueryAndSections = mActivity.getListOfQueryAndSections();

        //TextInputEditText
        mTextInputEditText = (TextInputEditText) mActivity.findViewById(R.id.notif_text_input_edit_text);

        //Database
        dbH = new DatabaseHelper(mActivity);
        mCursorQueryOrSection = dbH.getAllDataFromTableName(
                DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);
        mCursorSwitch = dbH.getAllDataFromTableName(
                DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);
        mCursorArticles = dbH.getAllDataFromTableName(
                DatabaseContract.Database.ARTICLES_FOR_NOTIFICATION_TABLE_NAME);

    }

    @Test
    public void testHomeButton() {

        onView(withContentDescription(R.string.go_back)).perform(click());
        Activity searchArticlesActivity = getInstrumentation().waitForMonitorWithTimeout(mainActivityMonitor, 5000);
        assertNotNull(searchArticlesActivity);
        searchArticlesActivity.finish();

    }

    @Test
    public void testNotificationsArticlesButton () {

        if (mCursorArticles.getCount() != 0){

            onView(withId(R.id.menu_notifications_button)).perform(click());
            Activity displayNotificationsActivity = getInstrumentation().waitForMonitorWithTimeout(displayNotificationsActivityMonitor, 5000);
            assertNotNull(displayNotificationsActivity);
            displayNotificationsActivity.finish();

        } else {

            onView(withText(R.string.notification_no_available_articles))
                    .inRoot(withDecorView(not(mActivity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));

        }

    }

    @Test
    public void testAllViewsAreDisplayedAndOrClickable () {

        //We uncheck the checkboxes that might be checked
        if (cb_arts.isChecked()) onView(withId(R.id.notif_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.notif_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.notif_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.notif_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.notif_checkBox_travel)).perform(click());

        //We check all the different views
        onView(withId(R.id.notif_text_input_edit_text)).check(matches(isDisplayed()));

        onView(withId(R.id.notif_checkBox_arts)).check(matches(isClickable()));
        onView(withId(R.id.notif_checkBox_business)).check(matches(isClickable()));
        onView(withId(R.id.notif_checkBox_entrepeneurs)).check(matches(isClickable()));
        onView(withId(R.id.notif_checkBox_politics)).check(matches(isClickable()));
        onView(withId(R.id.notif_checkBox_sports)).check(matches(isClickable()));
        onView(withId(R.id.notif_checkBox_travel)).check(matches(isClickable()));

        //We check the notification switch is not enabled
        onView(withId(R.id.notif_switch)).check(matches(not(isEnabled())));

        //We perform a click on a checkbox to enable the switch
        onView(withId(R.id.notif_checkBox_arts)).perform(click());
        onView(withId(R.id.notif_switch)).check(matches((isEnabled())));

        //We leave all the checkboxes unchecked
        if (cb_arts.isChecked()) onView(withId(R.id.notif_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.notif_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.notif_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.notif_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.notif_checkBox_travel)).perform(click());

        //We leave the notification switch unchecked
        if (notif_switch.isChecked()) onView(withId(R.id.notif_switch)).perform(click());


    }

    @Test
    public void testListSectionsUploadedWhenCheckboxCheckedAndUnchecked () {

        //First we uncheck all checkboxes if they are not checked and checked the list
        //has empty strings from 1 to 6 (Sections)
        if (cb_arts.isChecked()) onView(withId(R.id.notif_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.notif_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.notif_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.notif_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.notif_checkBox_travel)).perform(click());

        for (int i = 1; i < 7; i++) {
            assertTrue(listOfQueryAndSections.get(i).equals(""));
        }

        //We check all the checkboxes and check the list is uploaded
        onView(withId(R.id.notif_checkBox_arts)).perform(click());
        onView(withId(R.id.notif_checkBox_business)).perform(click());
        onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        onView(withId(R.id.notif_checkBox_politics)).perform(click());
        onView(withId(R.id.notif_checkBox_sports)).perform(click());
        onView(withId(R.id.notif_checkBox_travel)).perform(click());

        assertTrue(listOfQueryAndSections.get(1).equals(Keys.CheckboxFields.CB_ARTS));
        assertTrue(listOfQueryAndSections.get(2).equals(Keys.CheckboxFields.CB_BUSINESS));
        assertTrue(listOfQueryAndSections.get(3).equals(Keys.CheckboxFields.CB_ENTREPRENEURS));
        assertTrue(listOfQueryAndSections.get(4).equals(Keys.CheckboxFields.CB_POLITICS));
        assertTrue(listOfQueryAndSections.get(5).equals(Keys.CheckboxFields.CB_SPORTS));
        assertTrue(listOfQueryAndSections.get(6).equals(Keys.CheckboxFields.CB_TRAVEL));

        //We leave all the checkboxes unchecked
        if (cb_arts.isChecked()) onView(withId(R.id.notif_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.notif_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.notif_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.notif_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.notif_checkBox_travel)).perform(click());

    }

    @Test
    public void testTextInputEditTextUpdatesTheList () {

        //First we empty the TextInputEditText
        onView(withId(R.id.notif_text_input_edit_text)).perform(clearText());

        //We check a checkbox to update the query in the list
        onView(withId(R.id.notif_checkBox_business)).perform(click());
        assertTrue(listOfQueryAndSections.get(0).equals(""));

        //We write something, uncheck the checkbox and check that the query is updated in the list
        onView(withId(R.id.notif_text_input_edit_text)).perform(typeText("This is a test"));
        assertTrue(listOfQueryAndSections.get(0).equals(""));
        onView(withId(R.id.notif_checkBox_business)).perform(click());
        assertTrue(listOfQueryAndSections.get(0).equals("This is a test"));

        //We clear the TextInputEditText and update the list again
        onView(withId(R.id.notif_text_input_edit_text)).perform(clearText());
        onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        assertTrue(listOfQueryAndSections.get(0).equals(""));

    }


    @Test
    public void testSwitchCanOnlyBeActiveWhenCheckboxClicked () {

        //We uncheck the checkboxes that might be checked
        if (cb_arts.isChecked()) onView(withId(R.id.notif_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.notif_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.notif_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.notif_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.notif_checkBox_travel)).perform(click());

        //We check switch is not enabled
        onView(withId(R.id.notif_switch)).check(matches(not(isEnabled())));

        //We check a checkbox and check the switch is enabled and checkable
        onView(withId(R.id.notif_checkBox_travel)).perform(click());
        onView(withId(R.id.notif_switch)).check(matches((isEnabled())));
        onView(withId(R.id.notif_switch)).perform(click());
        onView(withId(R.id.notif_switch)).check(matches((isChecked())));

        //We uncheck the checkbox and check the checkbox is disabled
        onView(withId(R.id.notif_checkBox_travel)).perform(click());
        onView(withId(R.id.notif_switch)).check(matches(not(isEnabled())));

    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;

        dbH = null;
        mCursorQueryOrSection = null;
        mCursorSwitch = null;


    }



}
