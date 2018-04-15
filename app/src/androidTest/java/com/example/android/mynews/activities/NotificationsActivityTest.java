package com.example.android.mynews.activities;

import android.app.Instrumentation;
import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.test.rule.ActivityTestRule;
import android.widget.CheckBox;

import com.example.android.mynews.R;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;

/**
 * Created by Diego Fajardo on 12/04/2018.
 */

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

    private TextInputEditText mTextInputEditText;

    DatabaseHelper dbH;
    Cursor mCursorQueryOrSection;
    Cursor mCursorSwitch;

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

        mTextInputEditText = (TextInputEditText) mActivity.findViewById(R.id.notif_text_input_edit_text);

        dbH = new DatabaseHelper(mActivity);
        mCursorQueryOrSection = dbH.getAllDataFromTableName(
                DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME);
        mCursorSwitch = dbH.getAllDataFromTableName(
                DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);

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

        onView(withId(R.id.notif_switch)).check(matches(not(isEnabled())));

        //We perform a click on a checkbox to enable the switch
        onView(withId(R.id.notif_checkBox_arts)).perform(click());
        onView(withId(R.id.notif_switch)).check(matches((isEnabled())));

        //We leave all the checkboxes unchecked
        onView(withId(R.id.notif_checkBox_arts)).perform(click());

    }

    @Test
    public void testDatabaseUploadedWhenCheckboxCheckedAndUnchecked () {

        //First we check all checkboxes if they are not checked
        if (!cb_arts.isChecked()) onView(withId(R.id.notif_checkBox_arts)).perform(click());
        if (!cb_business.isChecked()) onView(withId(R.id.notif_checkBox_business)).perform(click());
        if (!cb_entrepreneurs.isChecked()) onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        if (!cb_politics.isChecked()) onView(withId(R.id.notif_checkBox_politics)).perform(click());
        if (!cb_sports.isChecked()) onView(withId(R.id.notif_checkBox_sports)).perform(click());
        if (!cb_travel.isChecked()) onView(withId(R.id.notif_checkBox_travel)).perform(click());

        //We check that all the data is in the database
        mCursorQueryOrSection.moveToFirst();
        for (int i = 0; i < mCursorQueryOrSection.getCount(); i++) {

            //First row is for the query and we are testing the checkboxes, so we
            //jump the first row
            if (i != 0) {
                assertTrue(!mCursorQueryOrSection.getString(
                        mCursorQueryOrSection.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION)).equals(""));
            }

            if (i != mCursorQueryOrSection.getCount())
                mCursorQueryOrSection.moveToNext();
        }

        //We uncheck the checkboxes and check that the data is deleted
        if (cb_arts.isChecked()) onView(withId(R.id.notif_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.notif_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.notif_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.notif_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.notif_checkBox_travel)).perform(click());

        for (int i = 1; i < mCursorQueryOrSection.getCount()+1; i++) {
            dbH.updateSearchQueryOrSection("", i);
        }

    }

    @Test
    public void testSwitchCanOnlyBeActiveWhenCheckboxClicked () {

        mCursorSwitch.moveToFirst();

        //We uncheck the checkboxes
        if (cb_arts.isChecked()) onView(withId(R.id.notif_checkBox_arts)).perform(click());
        if (cb_business.isChecked()) onView(withId(R.id.notif_checkBox_business)).perform(click());
        if (cb_entrepreneurs.isChecked()) onView(withId(R.id.notif_checkBox_entrepeneurs)).perform(click());
        if (cb_politics.isChecked()) onView(withId(R.id.notif_checkBox_politics)).perform(click());
        if (cb_sports.isChecked()) onView(withId(R.id.notif_checkBox_sports)).perform(click());
        if (cb_travel.isChecked()) onView(withId(R.id.notif_checkBox_travel)).perform(click());

        //Now the switch must not be active
        onView(withId(R.id.notif_switch)).check(matches(not(isEnabled())));

        //Now we check it and check that the database is updated
        onView(withId(R.id.notif_checkBox_arts)).perform(click());
        onView(withId(R.id.notif_switch)).check(matches(isEnabled()));
        onView(withId(R.id.notif_switch)).perform(click());
        onView(withId(R.id.notif_switch)).check(matches(isChecked()));

        assertTrue(mCursorSwitch.getInt(mCursorSwitch.getColumnIndex(
                DatabaseContract.Database.SWITCH_STATE)) == 1);

        //Now we uncheck it
        onView(withId(R.id.notif_switch)).perform(click());
        onView(withId(R.id.notif_switch)).check(matches(not(isChecked())));

        onView(withId(R.id.notif_checkBox_arts)).perform(click());

        //Now the switch must not be active again
        onView(withId(R.id.notif_switch)).check(matches(not(isEnabled())));

        //We check that the database is updated
        assertTrue(mCursorSwitch.getInt(mCursorSwitch.getColumnIndex(
                DatabaseContract.Database.SWITCH_STATE)) == 0);

    }

    @Test
    public void testSwitchDatabaseHelperMethodsUpdateDatabase () {

        // TODO: 12/04/2018 Not passing the test

        dbH.setSwitchOffInDatabase();
        mCursorSwitch.moveToFirst();

        assertTrue(mCursorSwitch.getInt(
                mCursorSwitch.getColumnIndex(DatabaseContract.Database.SWITCH_STATE)) == 0);

        dbH.setSwitchOnInDatabase();
        assertTrue(mCursorSwitch.getInt(
                mCursorSwitch.getColumnIndex(DatabaseContract.Database.SWITCH_STATE)) == 1);
        /**
        dbH.setSwitchOffInDatabase();
        assertTrue(mCursorSwitch.getInt(
                mCursorSwitch.getColumnIndex(DatabaseContract.Database.SWITCH_STATE)) == 0);
        */
    }

    @Test
    public void testSearchQueryIsWritableAndUpdatesDatabase () {

        onView(withId(R.id.notif_text_input_edit_text))
                .perform(typeText("This is a test"));

        //We click another thing in the screen to update the database
        onView(withId(R.id.notif_checkBox_arts)).perform(click());
        onView(withId(R.id.notif_checkBox_arts)).perform(click());

        // TODO: 12/04/2018 Continue

        //We clean the textInput
        dbH.updateSearchQueryOrSection("",1);

        onView(withId(R.id.notif_text_input_edit_text))
                .perform(clearText());

    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;

        dbH = null;
        mCursorQueryOrSection = null;
        mCursorSwitch = null;


    }



}
