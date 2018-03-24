package com.example.android.mynews.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.mynews.R;
import com.example.android.mynews.broadcastreceiver.NotificationReceiver;
import com.example.android.mynews.extras.Keys;

import java.util.ArrayList;
import java.util.Calendar;
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

    // TODO: 23/03/2018 Delete this button
    //Test Button
    Button testButton;

    //List for sections
    private List<String> listOfSections;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.notif_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //List
        listOfSections = new ArrayList<>();

        //TextInputEditText
        mTextInputEditText = (TextInputEditText) findViewById(R.id.notif_text_input_edit_text);

        //Switch
        mSwitch = (Switch) findViewById(R.id.notif_switch);

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

        /**
        //Code for the button
        PendingIntent p_button_intent = PendingIntent.getBroadcast(context, 123, button_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.custom_notif_button, p_button_intent);
        //Request code can be any number
         */

        //Code for the button of the notification layout
        findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Notification is set", Toast.LENGTH_SHORT).show();

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, 19);
                calendar.set(Calendar.MINUTE, 23);
                calendar.set(Calendar.SECOND, 0);

                //Calls the broadcast receiver to set the alarm
                Intent notification_intent = new Intent(context, NotificationReceiver.class);

                /** The intent will have several extras:
                 * 1: size of the arrayList
                 * 2: each section */
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
            }
        });

        //Code for the switch
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cb_arts.isChecked()
                        && !cb_business.isChecked()
                        && !cb_entrepreneurs.isChecked()
                        && !cb_politics.isChecked()
                        && !cb_sports.isChecked()
                        && !cb_travel.isChecked()) {
                    // TODO: 22/03/2018 Avoid the switch to be on if at least one checkbox isn't clicked
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

    // TODO: 21/03/2018 At least one checkbox must be selected (not clear)
    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.search_checkBox_arts:
                ///When checked, add a String with the name of the checkbox to the list
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_ARTS); }
                //When unchecked, check if there is an element with the name of the Checkbox in the array
                //if there is one, remove it from the list
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_ARTS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_ARTS));
                }
                break;

            case R.id.search_checkBox_business:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_BUSINESS); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_BUSINESS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_BUSINESS));
                }
                break;

            case R.id.search_checkBox_entrepeneurs:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_ENTREPRENEURS); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_ENTREPRENEURS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_ENTREPRENEURS));
                }
                break;

            case R.id.search_checkBox_politics:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_POLITICS); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_POLITICS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_POLITICS));
                }
                break;

            case R.id.search_checkBox_sports:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_SPORTS); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_SPORTS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_SPORTS));
                }
                break;

            case R.id.search_checkBox_travel:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_TRAVEL); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_TRAVEL))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_TRAVEL));
                }
                break;
        }
    }
}
