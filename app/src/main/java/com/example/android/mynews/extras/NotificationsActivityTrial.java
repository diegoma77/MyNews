package com.example.android.mynews.extras;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mynews.R;
import com.example.android.mynews.asynctaskloaders.atlhelper.AsyncTaskLoaderHelper;
import com.example.android.mynews.broadcastreceiver.NotificationReceiver;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Diego Fajardo on 26/02/2018.
 */

public class NotificationsActivityTrial extends AppCompatActivity {

    private static final String TAG = "NotificationsActivity";

    private static final int LOADER_UPDATE_DATABASE_QUERY_AND_SECTIONS_ID = 1;
    private static final int LOADER_UPDATE_LIST_ID = 2;
    private static final int LOADER_UPDATE_DATABASE_SWITCH_ID = 3;
    private static final int LOADER_UPDATE_SWITCH_ID = 4;

    //Needed for getApplicationContext() to work
    private Context context;

    //List that stores the information of QueryOrSection Table for the activity
    private List<String> listOfQueryAndSections;

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


    // TODO: 22/04/2018 Delete this

    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;

    //Database variables
    DatabaseHelper dbH;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_layout);

        Log.i(TAG, "+++ onCreate: called! +++");

        Toolbar toolbar = (Toolbar) findViewById(R.id.notif_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // TODO: 22/04/2018 Delete this
        tv0 = findViewById(R.id.tv0);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);

        //Needed for getApplicationContext() to work
        context = this;

        //Instantiation of the list
        listOfQueryAndSections = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            listOfQueryAndSections.add("");
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

        //We get the reference for mSwitch and set the switchListener
        mSwitch = findViewById(R.id.notif_switch);
        mSwitch.setOnCheckedChangeListener(switchListener);

        //Instantiation of the DatabaseHelper
        dbH = new DatabaseHelper (this);

        /** We fill the list with the information from the database (Query and Sections table)
         * and also check or uncheck the switch according to the database */
        loadLoaderUpdateList(LOADER_UPDATE_LIST_ID);
        loadLoaderUpdateSwitchVariable(LOADER_UPDATE_SWITCH_ID);







        // TODO: 21/04/2018 Do this using an AsyncTaskLoader and in MainActivity
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

    }

    /*******************/
    /** CLASS METHODS **/
    /*******************/

    /** Method used to show on the user interface the information of the database (that is
     * already in the list) */
    private void updateSearchQueryAndCheckboxes() {

        mTextInputEditText.setText(listOfQueryAndSections.get(0));

        if (listOfQueryAndSections.get(1).equals(Keys.CheckboxFields.CB_ARTS)) {
            cb_arts.setChecked(true);
        } else { cb_arts.setChecked(false); }

        if (listOfQueryAndSections.get(2).equals(Keys.CheckboxFields.CB_BUSINESS)) {
            cb_business.setChecked(true);
        } else { cb_business.setChecked(false); }

        if (listOfQueryAndSections.get(3).equals(Keys.CheckboxFields.CB_ENTREPRENEURS)) {
            cb_entrepreneurs.setChecked(true);
        } else { cb_entrepreneurs.setChecked(false); }

        if (listOfQueryAndSections.get(4).equals(Keys.CheckboxFields.CB_POLITICS)) {
            cb_politics.setChecked(true);
        } else { cb_politics.setChecked(false); }

        if (listOfQueryAndSections.get(5).equals(Keys.CheckboxFields.CB_SPORTS)) {
            cb_sports.setChecked(true);
        } else { cb_sports.setChecked(false); }

        if (listOfQueryAndSections.get(6).equals(Keys.CheckboxFields.CB_TRAVEL)) {
            cb_travel.setChecked(true);
        } else { cb_travel.setChecked(false); }

    }

    /** This method updates the item in the listOfQueryAndSections related
     * to the quer y*/
    private void updateQueryInTheList () {
        listOfQueryAndSections.set(0, mTextInputEditText.getText().toString());
    }

    /** This method disables the switch to prevent the user to call
     * the alarm manager if no section has been selected.
     * It is disabled when the last checked checkbox turns unchecked
     * */
    private void disableSwitchIfNeeded () {

        if (!cb_arts.isChecked() &&
                !cb_business.isChecked() &&
                !cb_entrepreneurs.isChecked() &&
                !cb_politics.isChecked() &&
                !cb_sports.isChecked() &&
                !cb_travel.isChecked()) {

            mSwitch.setChecked(false);
            mSwitch.setEnabled(false);
        } else{
            mSwitch.setEnabled(true);
        }
    }

    /************************/
    /** ACTIVITY LIFECYCLE **/
    /************************/

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "+++ onResume: called! +++");

    }

    /** We update the database in
     * onPause() and onDestroy() */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "+++ onPause: called! +++");

        updateQueryInTheList();
        loadLoaderUpdateQueryAndSectionsTable(LOADER_UPDATE_DATABASE_QUERY_AND_SECTIONS_ID);
        loadLoaderUpdateSwitchTable(LOADER_UPDATE_DATABASE_SWITCH_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "+++ onDestroy: called! +++");

        updateQueryInTheList();
        loadLoaderUpdateQueryAndSectionsTable(LOADER_UPDATE_DATABASE_QUERY_AND_SECTIONS_ID);
        loadLoaderUpdateSwitchTable(LOADER_UPDATE_DATABASE_SWITCH_ID);
    }


    /****************/
    /** LISTENERS **/
    /***************/

    /** Switch
     * Listener*/

    CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {

                updateQueryInTheList();
                loadLoaderUpdateQueryAndSectionsTable(LOADER_UPDATE_DATABASE_QUERY_AND_SECTIONS_ID);

                Toast.makeText(NotificationsActivityTrial.this, getResources().getString(R.string.notification_is_created), Toast.LENGTH_SHORT).show();

                createAlarm();

            } else {
                //We set the switch to off in the database
                dbH.setSwitchOffInDatabase();

                //We cancel the alarm
                cancelAlarm();
            }
        }
    };

    /** Method to add and delete sections as well as disable the switch if needed.
     * When a checkbox is checked, add a String with the name of the checkbox to the list if it doesn't exist.
     * When unchecked, check if there is an element with the name of the checkbox in the array. If there is any,
     * remove it from the list. Additionally, set the switch to unchecked */
    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.notif_checkBox_arts:
                if (checked) {
                    listOfQueryAndSections.set(1, Keys.CheckboxFields.CB_ARTS);
                    updateQueryInTheList();
                    mSwitch.setEnabled(true);
                    listDetector();
                }
                else {
                    listOfQueryAndSections.set(1, "");
                    updateQueryInTheList();
                    disableSwitchIfNeeded();
                    listDetector();
                }
                break;

            case R.id.notif_checkBox_business:
                if (checked) {
                    listOfQueryAndSections.set(2, Keys.CheckboxFields.CB_BUSINESS);
                    updateQueryInTheList();
                    mSwitch.setEnabled(true);
                    listDetector();
                }
                else {
                    listOfQueryAndSections.set(2, "");
                    updateQueryInTheList();
                    disableSwitchIfNeeded();
                    listDetector();
                }
                break;

            case R.id.notif_checkBox_entrepeneurs:
                if (checked) {
                    listOfQueryAndSections.set(3, Keys.CheckboxFields.CB_ENTREPRENEURS);
                    updateQueryInTheList();
                    mSwitch.setEnabled(true);
                    listDetector();
                }
                else {
                    listOfQueryAndSections.set(3, "");
                    updateQueryInTheList();
                    disableSwitchIfNeeded();
                    listDetector();
                }
                break;

            case R.id.notif_checkBox_politics:
                if (checked) {
                    listOfQueryAndSections.set(4, Keys.CheckboxFields.CB_POLITICS);
                    updateQueryInTheList();
                    mSwitch.setEnabled(true);
                    listDetector();
                }
                else {
                    listOfQueryAndSections.set(4, "");
                    updateQueryInTheList();
                    disableSwitchIfNeeded();
                    listDetector();
                }
                break;

            case R.id.notif_checkBox_sports:
                if (checked) {
                    listOfQueryAndSections.set(5, Keys.CheckboxFields.CB_SPORTS);
                    updateQueryInTheList();
                    mSwitch.setEnabled(true);
                    listDetector();
                }
                else {
                    listOfQueryAndSections.set(5, "");
                    updateQueryInTheList();
                    disableSwitchIfNeeded();
                    listDetector();
                }
                break;

            case R.id.notif_checkBox_travel:
                if (checked) {
                    listOfQueryAndSections.set(6, Keys.CheckboxFields.CB_TRAVEL);
                    updateQueryInTheList();
                    mSwitch.setEnabled(true);
                    listDetector();
                }
                else {
                    listOfQueryAndSections.set(6, "");
                    updateQueryInTheList();
                    disableSwitchIfNeeded();
                    listDetector();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(NotificationsActivityTrial.this, NotificationsActivityTrial.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /** This method creates the alarm
     * for the notification */
    private void createAlarm () {

        // TODO: 21/04/2018 Use JobScheduler
        // TODO: 17/04/2018 Check that when we add one to the day of the month, it is not the last day or
        // it might crash!!!!!


        //We create an instance of a calendar class to set
        // the time when the notification will appear
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH+1));
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

    // TODO: 21/04/2018 Use JobScheduler
    /** This method cancels the alarm
     * for the notification */
    private void cancelAlarm () {

        Intent notification_intent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                100,
                notification_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

    }

    // TODO: 22/04/2018 Delete this method

    public void listDetector () {
        tv0.setText(listOfQueryAndSections.get(0));
        tv1.setText(listOfQueryAndSections.get(1));
        tv2.setText(listOfQueryAndSections.get(2));
        tv3.setText(listOfQueryAndSections.get(3));
        tv4.setText(listOfQueryAndSections.get(4));
        tv5.setText(listOfQueryAndSections.get(5));
        tv6.setText(listOfQueryAndSections.get(6));
    }



    /*****************************/
    /** METHODS TO INIT LOADERS **/
    /*****************************/

    private void loadLoaderUpdateList(int id) {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<String>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            loaderManager.initLoader(id, null, loaderUpdateListOfQueryAndSections);
        } else {
            loaderManager.restartLoader(id, null, loaderUpdateListOfQueryAndSections);
        }
    }

    private void loadLoaderUpdateQueryAndSectionsTable(int id) {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Boolean> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "onResume: loader==null");
            loaderManager.initLoader(id, null, loaderUpdateQueryAndSectionsTable);
        } else {
            Log.i(TAG, "onResume: loader!=null");
            loaderManager.restartLoader(id, null, loaderUpdateQueryAndSectionsTable);
        }
    }

    private void loadLoaderUpdateSwitchVariable(int id) {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Boolean> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "onResume: loader==null");
            loaderManager.initLoader(id, null, loaderUpdateSwitchVariable);
        } else {
            Log.i(TAG, "onResume: loader!=null");
            loaderManager.restartLoader(id, null, loaderUpdateSwitchVariable);
        }

    }

    private void loadLoaderUpdateSwitchTable(int id) {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Boolean> loader = loaderManager.getLoader(LOADER_UPDATE_DATABASE_SWITCH_ID);

        if (loader == null) {
            Log.i(TAG, "onResume: loader==null");
            loaderManager.initLoader(id, null, loaderUpdateSwitchTable);
        } else {
            Log.i(TAG, "onResume: loader!=null");
            loaderManager.restartLoader(id, null, loaderUpdateSwitchTable);
        }
    }


    /**********************/
    /** LOADER CALLBACKS **/
    /**********************/

    /** The loader callbacks are used to instantiate
     * AsyncTaskLoaders (which do a specific function:
     * eg. update the database with the information from the listOfSectionsAndQuery
     * eg. update the listOfSectionsAndQuery with the information from the database) */

    private LoaderManager.LoaderCallbacks<List<String>> loaderUpdateListOfQueryAndSections =
            new LoaderManager.LoaderCallbacks<List<String>>() {

                @Override
                public Loader<List<String>> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.updateListOfQueryAndSections(NotificationsActivityTrial.this);
                }

                @Override
                public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
                    Log.i(TAG, "onLoadFinished: listOfQueryAndSections +++");

                    for (int i = 0; i < data.size(); i++) {
                        listOfQueryAndSections.set(i, data.get(i));
                    }
                    updateSearchQueryAndCheckboxes();
                    disableSwitchIfNeeded();
                    listDetector();

                    for (int i = 0; i < listOfQueryAndSections.size(); i++) {
                        Log.i(TAG, "onLoadFinished: " + listOfQueryAndSections.get(i));

                    }

                }

                @Override
                public void onLoaderReset(Loader<List<String>> loader) {

                }
            };


    private LoaderManager.LoaderCallbacks<Boolean> loaderUpdateQueryAndSectionsTable =
            new LoaderManager.LoaderCallbacks<Boolean>() {

                @Override
                public Loader<Boolean> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.updateQueryAndSectionsTable(
                            NotificationsActivityTrial.this,
                            listOfQueryAndSections);
                }

                @Override
                public void onLoadFinished(Loader<Boolean> loader, Boolean data) {

                }

                @Override
                public void onLoaderReset(Loader<Boolean> loader) {

                }
            };


    private LoaderManager.LoaderCallbacks<Boolean> loaderUpdateSwitchTable =
            new LoaderManager.LoaderCallbacks<Boolean>() {

                @Override
                public Loader<Boolean> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.updateSwitchTable(
                            NotificationsActivityTrial.this,
                            mSwitch.isChecked());
                }

                @Override
                public void onLoadFinished(Loader<Boolean> loader, Boolean data) {

                }

                @Override
                public void onLoaderReset(Loader<Boolean> loader) {

                }
            };


    private LoaderManager.LoaderCallbacks<Boolean> loaderUpdateSwitchVariable =
            new LoaderManager.LoaderCallbacks<Boolean>() {

                @Override
                public Loader<Boolean> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.updateSwitchVariable(NotificationsActivityTrial.this);
                }

                @Override
                public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
                    if (data) {
                        mSwitch.setChecked(true);
                    } else {
                        mSwitch.setChecked(false);
                    }
                }

                @Override
                public void onLoaderReset(Loader<Boolean> loader) {

                }
            };


}
