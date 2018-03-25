package com.example.android.mynews.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
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
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.Keys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 26/02/2018.
 */

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = "NotificationsActivity";

    //Variables for notifications
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;

    //TextInput
    private TextInputEditText mTextInputEditText;

    // TODO: 24/03/2018 Delete
    //Test Button
    Button button_test;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.notif_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbH = new DatabaseHelper (this);

        /** This method updates the user interface according to the information that can be found
         * in the database
         * */
        if (!dbH.isTableEmpty(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME)) {
            checkAllCheckboxesIfInTable();
        }

        // TODO: 24/03/2018 Delete
        button_test = (Button) findViewById(R.id.test_button);

        //Switch
        mSwitch = (Switch) findViewById(R.id.notif_switch);

        //If the notifications_section table is not empty, then Switch must be on. If its empty, must be disabled
        if (dbH.isTableEmpty(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME)) {
            mSwitch.setEnabled(false);
            Toast.makeText(NotificationsActivity.this,
                    "Please, choose at least one category",
                    Toast.LENGTH_LONG).show();
        }
        else {
            mSwitch.setChecked(true);
        }

        //TextInputEditText
        mTextInputEditText = (TextInputEditText) findViewById(R.id.notif_text_input_edit_text);

        //Checkboxes
        cb_arts = (CheckBox) findViewById(R.id.notif_checkBox_arts);
        cb_business = (CheckBox) findViewById(R.id.notif_checkBox_business);
        cb_entrepreneurs = (CheckBox) findViewById(R.id.notif_checkBox_entrepeneurs);
        cb_politics = (CheckBox) findViewById(R.id.notif_checkBox_politics);
        cb_sports = (CheckBox) findViewById(R.id.notif_checkBox_sports);
        cb_travel = (CheckBox) findViewById(R.id.notif_checkBox_travel);


        /** Code for the notifications */

        //We initialize the context so as not to call it several times with getContext()
        context = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);

        /**
        //Allows to change the views inside the notification layout
        remoteViews.setImageViewResource(R.id.custom_notif_icon, R.mipmap.ic_launcher );
        remoteViews.setTextViewText(R.id.custom_notif_text, "TEXT");
         */

        //Intent for the broadcast receiver
        //Allows to get a unique id (uses the current time, thats why it will always be unique)
        notification_id = (int) System.currentTimeMillis();
        final Intent button_intent = new Intent("button_clicked");
        button_intent.putExtra("id", notification_id);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    Toast.makeText(context, "Switch is checked", Toast.LENGTH_SHORT).show();

                    mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME);
                    mCursor.moveToFirst();

                    for (int i = 0; i < mCursor.getCount(); i++) {
                        if (mCursor.getPosition() == 0) {
                            Log.i("mCursor", mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)));
                            mCursor.moveToNext();
                        } else {
                            Log.i("mCursor", mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)));
                            mCursor.moveToNext();
                        }
                    }
                }

                else {
                    Toast.makeText(context, "Switch is NOT checked", Toast.LENGTH_SHORT).show();
                    dbH.deleteAllRowsFromTableName(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME);
                    setAllCheckboxesToUnChecked();
                }

            }
        });

        /**
         //Code for the button
         PendingIntent p_button_intent = PendingIntent.getBroadcast(context, 123, button_intent, PendingIntent.FLAG_UPDATE_CURRENT);
         remoteViews.setOnClickPendingIntent(R.id.custom_notif_button, p_button_intent);
         //Request code can be any number
         */
        //Code for the button of the notification layout
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Test Button is clicked", Toast.LENGTH_SHORT).show();

                dbH = new DatabaseHelper(getApplicationContext());
                mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME);

                Log.i("TEST BUTTON", "mCursor count =" + mCursor.getCount());


                /**
                if (listOfSections.size() != 0) {

                    for (int i = 0; i < listOfSections.size(); i++) {
                        Log.i ( "LIST OF SECTIONS(" + i + ")", listOfSections.get(i));
                    }

                }
                else { Log.i ( "LIST OF SECTIONS", "IS EMPTY"); }

                 */


                /**

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, 19);
                calendar.set(Calendar.MINUTE, 23);
                calendar.set(Calendar.SECOND, 0);

                //Calls the broadcast receiver to set the alarm
                Intent notification_intent = new Intent(context, NotificationReceiver.class);

                 */

                /** The intent will have several extras:
                 * 1: size of the arrayList
                 * 2: each section */

                /**

                if (listOfSections.size() != 0) {

                    Log.i(TAG, "Before putting the size");
                    notification_intent.putExtra("Size", listOfSections.size());
                    Log.i(TAG, "After putting the size");

                    for (int i = 0; i < listOfSections.size(); i++) {
                        notification_intent.putExtra("ListOfSections" + i, listOfSections.get(i));
                    }
                }

                for (int i = 0; i < listOfSections.size() ; i++) {
                    Log.i(TAG, listOfSections.get(i));
                }



                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(),
                        100,
                        notification_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent);

                 */
            }
        });

        //Code for the switch
        // TODO: 25/03/2018 Can be removed
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cb_arts.isChecked()
                        && !cb_business.isChecked()
                        && !cb_entrepreneurs.isChecked()
                        && !cb_politics.isChecked()
                        && !cb_sports.isChecked()
                        && !cb_travel.isChecked()) {
                    
                    Toast.makeText(NotificationsActivity.this, "You have to choose at least one category", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (mSwitch.isChecked()) {
                        Toast.makeText(NotificationsActivity.this, "Now switch is on", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(NotificationsActivity.this, "Now switch is off", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

    /** Method to add and delete sections as well as disable the switch if needed */
    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.notif_checkBox_arts:
                ///When checked, add a String with the name of the checkbox to the list if it doesn't exist
                if (checked) {
                    addSectionToTable(Keys.CheckboxFields.CB_ARTS);
                }
                //When unchecked, check if there is an element with the name of the checkbox in the array
                //if there is one, remove it from the list
                else {
                    deleteSectionFromTable(Keys.CheckboxFields.CB_ARTS);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_business:
                ///When checked, add a String with the name of the checkbox to the list if it doesn't exist
                if (checked) {
                    addSectionToTable(Keys.CheckboxFields.CB_BUSINESS);
                }
                //When unchecked, check if there is an element with the name of the checkbox in the array
                //if there is one, remove it from the list
                else {
                    deleteSectionFromTable(Keys.CheckboxFields.CB_BUSINESS);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_entrepeneurs:
                ///When checked, add a String with the name of the checkbox to the list if it doesn't exist
                if (checked) {
                    addSectionToTable(Keys.CheckboxFields.CB_ENTREPRENEURS);
                }
                //When unchecked, check if there is an element with the name of the checkbox in the array
                //if there is one, remove it from the list
                else {
                    deleteSectionFromTable(Keys.CheckboxFields.CB_ENTREPRENEURS);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_politics:
                ///When checked, add a String with the name of the checkbox to the list if it doesn't exist
                if (checked) {
                    addSectionToTable(Keys.CheckboxFields.CB_POLITICS);
                }
                //When unchecked, check if there is an element with the name of the checkbox in the array
                //if there is one, remove it from the list
                else {
                    deleteSectionFromTable(Keys.CheckboxFields.CB_POLITICS);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_sports:
                ///When checked, add a String with the name of the checkbox to the list if it doesn't exist
                if (checked) {
                    addSectionToTable(Keys.CheckboxFields.CB_SPORTS);
                }
                //When unchecked, check if there is an element with the name of the checkbox in the array
                //if there is one, remove it from the list
                else {
                    deleteSectionFromTable(Keys.CheckboxFields.CB_SPORTS);
                    disableSwitchIfNeeded();
                }
                break;

            case R.id.notif_checkBox_travel:
                ///When checked, add a String with the name of the checkbox to the list if it doesn't exist
                if (checked) {
                    addSectionToTable(Keys.CheckboxFields.CB_TRAVEL);
                }
                //When unchecked, check if there is an element with the name of the checkbox in the array
                //if there is one, remove it from the list
                else {
                    deleteSectionFromTable(Keys.CheckboxFields.CB_TRAVEL);
                    disableSwitchIfNeeded();
                }
                break;
        }
    }

    /** This method is called when the Switch turns unchecked. It unchecks all the checkboxes */
    private void setAllCheckboxesToUnChecked() {

        cb_arts.setChecked(false);
        cb_business.setChecked(false);
        cb_entrepreneurs.setChecked(false);
        cb_politics.setChecked(false);
        cb_sports.setChecked(false);
        cb_travel.setChecked(false);

    }

    /** This method is called when the table doesn't have the section but the checkbox is checked. It
     * adds the section to the table */
    private void addSectionToTable (String section) {

        int counter = 0;
        dbH = new DatabaseHelper(this);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME);
        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount() ; i++) {
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)).equals(section)) {
                counter++;
            }
        }

        if (counter == 0) {
            dbH.insertDataToNotificationsSectionTable(section);
            mSwitch.setEnabled(true);
        }

    }

    /**
     * This method is called when the activity is created to update the UI according to the information
     * already present in the database. It checks the checkbox if the section is already present in the table
     * */
    private void checkAllCheckboxesIfInTable () {

        dbH = new DatabaseHelper(this);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME);
        mCursor.moveToFirst();

        for (int i = 0; i < mCursor.getCount() ; i++) {

            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)) == Keys.CheckboxFields.CB_ARTS) cb_arts.setChecked(true);
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)) == Keys.CheckboxFields.CB_BUSINESS) cb_business.setChecked(true);
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)) == Keys.CheckboxFields.CB_ENTREPRENEURS) cb_entrepreneurs.setChecked(true);
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)) == Keys.CheckboxFields.CB_POLITICS) cb_politics.setChecked(true);
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)) == Keys.CheckboxFields.CB_SPORTS) cb_sports.setChecked(true);
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)) == Keys.CheckboxFields.CB_TRAVEL) cb_travel.setChecked(true);

        }
    }

    /**
     * This method is called when the checkbox is unchecked. It deletes the section from the table
     * */
    private void deleteSectionFromTable (String section) {

        dbH = new DatabaseHelper(this);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME);
        mCursor.moveToFirst();

        for (int i = 0; i < mCursor.getCount() ; i++) {
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)).equals(section)) {
                dbH.deleteSingleRowFromTableName(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME, section);
            }
            mCursor.moveToNext();
        }
    }

    /**This method disables the switch to prevent the user to call the alarm manager if no section
     * has been selected. It is called when the last checked checkbox turns unchecked
     * */
    private void disableSwitchIfNeeded () {

        if (dbH.isTableEmpty(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME)) {
            mSwitch.setChecked(false);
            mSwitch.setEnabled(false);
        }
    }
}
