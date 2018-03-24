package com.example.android.mynews.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 24/03/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    private int sizeOfListOfSections;
    private List <String> listOfSections;

    @Override
    public void onReceive(Context context, Intent intent) {

        listOfSections = new ArrayList<>();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        //When the user clicks the notification the app will be redirected to the activity
        Intent repeating_intent = new Intent(context, RepeatingActivity.class);

        if (sizeOfListOfSections != 0){

            Log.i(TAG, "Before getting the size");
            //We get the size of ListOfSections array
            sizeOfListOfSections = intent.getExtras().getInt("Size");
            Log.i(TAG, "After getting the size");

            for (int i = 0; i < sizeOfListOfSections; i++) {
                listOfSections.add(intent.getExtras().getString("ListOfSections" + i));
            }

            //We put the extras into repeating_intent
            repeating_intent.putExtra("Size", sizeOfListOfSections);

            for (int i = 0; i < sizeOfListOfSections ; i++) {
                repeating_intent.putExtra("ListOfSections" + i, listOfSections.get(i));

            }

        }

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Ensures that the activity will
        // be replaced if needed, although the activity was already open or running in the background

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                100,
                repeating_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //The request code must be the same as the same we pass to .notify later

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.ic_menu_sort_by_size)
                .setContentTitle("New Articles Available")
                .setContentText("Tap for reading them!")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        //SetAutoCancel(true) makes the notification dismissible when the user swipes it away

        //Request code must be the same as the pending intent
        notificationManager.notify(100, mBuilder.build());

    }
}
