package com.example.android.mynews.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.mynews.R;
import com.example.android.mynews.broadcastreceiver.NotificationReceiver;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.Keys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Diego Fajardo on 26/02/2018.
 */

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = "NotificationsActivity";
    private static final String C_TAG = "CursorErrorTag";

    //Needed for getApplicationContext() to work
    private Context context;

    //TextInput
    private TextInputEditText mTextInputEditText;

    //Switch
    private Switch mSwitch;

    //Checkboxes variables
    private CheckBox cb_arts;
    private CheckBox cb_business;
    private CheckBox cb_entrepreneurs;
    private CheckBox cb_politics;
    private CheckBox cb_sports;
    private CheckBox cb_travel;

    //Database variables
    DatabaseHelper dbH;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_layout);

        //Needed for getApplicationContext() to work
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.notif_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbH = new DatabaseHelper (this);

        //TextInputEditText
        mTextInputEditText = (TextInputEditText) findViewById(R.id.notif_text_input_edit_text);

        //Checkboxes
        cb_arts = (CheckBox) findViewById(R.id.notif_checkBox_arts);
        cb_business = (CheckBox) findViewById(R.id.notif_checkBox_business);
        cb_entrepreneurs = (CheckBox) findViewById(R.id.notif_checkBox_entrepeneurs);
        cb_politics = (CheckBox) findViewById(R.id.notif_checkBox_politics);
        cb_sports = (CheckBox) findViewById(R.id.notif_checkBox_sports);
        cb_travel = (CheckBox) findViewById(R.id.notif_checkBox_travel);

        //Switch
        mSwitch = (Switch) findViewById(R.id.notif_switch);

        /** Called the first time the Activity is created (and only this time).
         * Inserts data into the Notifications' Switch table.
         * Sets the switch in the database to false.
         * Inserts "" strings into all ids in Query or Search table
         *  */
        if (dbH.isTableEmpty(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME)) {
            dbH.insertDataToSwitchTable(0);
        }
        if (dbH.isTableEmpty(DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME)){

            for (int i = 0; i < 7; i++) {
                dbH.insertDataToSearchQueryTable("");
            }
        }

        /** This method updates the user interface according to the information that can be found
         * in the database.
         * It shows in the Search Query the last search
         * */
        if (!dbH.isTableEmpty(DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME)) {
            mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME);
            mCursor.moveToFirst();
            mTextInputEditText.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION)).toUpperCase());
        }

        /** This method updates the user interface according to the information that can be found
         * in the database. If the user left any checkBox checked, this information will be at the
         * the database and this code will update the UI according to the information
         * */
        if (!dbH.isTableEmpty(DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME)) {
            checkAllCheckboxesIfInTable();
        }

        /** This method updates the switch state according to the information in the database. */
        if (!dbH.isTableEmpty(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME)) {
            mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);
            mCursor.moveToFirst();

            if (mCursor.getInt(mCursor.getColumnIndex(DatabaseContract.Database.SWITCH_STATE)) == 0) {
                mSwitch.setChecked(false);
                disableSwitchIfNeeded();
            }
            else {
                mSwitch.setChecked(true);
            }
        }

        /** Switch listener */

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    /** First, the state of the switch is updated in the database (ON) */
                    mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);
                    dbH.setSwitchOnInDatabase();

                    /** Second, Search Query table is updated with the information from the Text Input */
                    dbH.updateSearchQueryOrSection(mTextInputEditText.getText().toString(), 1);

                    /** Third, we create the alarm manager, which will
                     * call the DisplayNotificationsActivity*/
                    Toast.makeText(NotificationsActivity.this, "Notification Created", Toast.LENGTH_SHORT).show();
                    createAlarm();

                }

                else {
                    //We set the switch to off in the database
                    dbH.setSwitchOffInDatabase();

                    //We cancel the alarm
                    cancelAlarm();

                }
            }
        });
    }

    /** Used to update the String put in the TextInput */
    @Override
    protected void onPause() {
        dbH.updateSearchQueryOrSection(mTextInputEditText.getText().toString().toLowerCase(), 1);
        super.onPause();
    }

    /** Menu listeners */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(NotificationsActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Method to add and delete sections as well as disable the switch if needed.
     * When a checkbox is checked, add a String with the name of the checkbox to the list if it doesn't exist.
     * When unchecked, check if there is an element with the name of the checkbox in the array. If there is any,
     * remove it from the list. Additionally, set the switch to unchecked */
    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME);

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.notif_checkBox_arts:
                if (checked) {
                    dbH.updateSearchQueryOrSection(Keys.CheckboxFields.CB_ARTS, 2);
                    mSwitch.setEnabled(true);
                }
                else {
                    dbH.updateSearchQueryOrSection("", 2);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_business:
                if (checked) {
                    dbH.updateSearchQueryOrSection(Keys.CheckboxFields.CB_BUSINESS, 3);
                    mSwitch.setEnabled(true);
                }
                else {
                    dbH.updateSearchQueryOrSection("", 3);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_entrepeneurs:
                if (checked) {
                    dbH.updateSearchQueryOrSection(Keys.CheckboxFields.CB_ENTREPRENEURS, 4);
                    mSwitch.setEnabled(true);
                }
                else {
                    dbH.updateSearchQueryOrSection("", 4);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_politics:
                if (checked) {
                    dbH.updateSearchQueryOrSection(Keys.CheckboxFields.CB_POLITICS, 5);
                    mSwitch.setEnabled(true);
                }
                else {
                    dbH.updateSearchQueryOrSection("", 5);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_sports:
                if (checked) {
                    dbH.updateSearchQueryOrSection(Keys.CheckboxFields.CB_SPORTS, 6);
                    mSwitch.setEnabled(true);
                }
                else {
                    dbH.updateSearchQueryOrSection("", 6);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_travel:
                if (checked) {
                    dbH.updateSearchQueryOrSection(Keys.CheckboxFields.CB_TRAVEL, 7);
                    mSwitch.setEnabled(true);
                }
                else {
                    dbH.updateSearchQueryOrSection("", 7);
                    disableSwitchIfNeeded();
                }
                break;
        }
    }

    /**
     * This method is called when the activity is created
     * to update the UI according to the information
     * already present in the database. It checks
     * the checkbox if the section is already in the table
     * */
    private void checkAllCheckboxesIfInTable () {

        dbH = new DatabaseHelper(this);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME);
        mCursor.moveToFirst();

        for (int i = 0; i < mCursor.getCount()-1 ; i++) {

            mCursor.moveToNext();

            if (mCursor.getString(
                    mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION))
                    .equals(Keys.CheckboxFields.CB_ARTS)) {
                cb_arts.setChecked(true);
            }

            if (mCursor.getString(
                    mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION))
                    .equals(Keys.CheckboxFields.CB_BUSINESS)) {
                cb_business.setChecked(true);
            }

            if (mCursor.getString(
                    mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION))
                    .equals(Keys.CheckboxFields.CB_ENTREPRENEURS)) {
                cb_entrepreneurs.setChecked(true);
            }

            if (mCursor.getString(
                    mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION))
                    .equals(Keys.CheckboxFields.CB_POLITICS)) {
                cb_politics.setChecked(true);
            }

            if (mCursor.getString(
                    mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION))
                    .equals(Keys.CheckboxFields.CB_SPORTS)) {
                cb_sports.setChecked(true);
            }

            if (mCursor.getString(
                    mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION))
                    .equals(Keys.CheckboxFields.CB_TRAVEL)) {
                cb_travel.setChecked(true);
            }
        }
    }

    /** This method disables the switch to prevent the user to call
     * the alarm manager if no section has been selected.
     * It is called when the last checked checkbox turns unchecked
     * */
    private void disableSwitchIfNeeded () {

        int counter = 0;
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME);
        mCursor.moveToFirst();

        for (int i = 0; i < mCursor.getCount()-1 ; i++) {
            mCursor.moveToNext();
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION)).equals("")) {
                counter++;
            }
        }
        //If counter is 6, then no checkbox is checked and mSwitch must be disabled
        if (counter == mCursor.getCount()-1) {
            mSwitch.setChecked(false);
            mSwitch.setEnabled(false);
        }
    }

    /** This method creates the alarm for the notification */
    private void createAlarm () {
        //We create an instance of a calendar class to set
        // the time when the notification will appear
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //Calls the broadcast receiver to set the alarm
        Intent notification_intent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                100,
                notification_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }

    }

    /** This method cancels the alarm */
    private void cancelAlarm () {

        Intent notification_intent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                100,
                notification_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }

}
