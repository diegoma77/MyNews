package com.example.android.mynews.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.mynews.R;
import com.example.android.mynews.activities.DisplayNotificationsActivity;
import com.example.android.mynews.activities.DisplaySearchArticlesActivity;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 24/03/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    /** This class is used for displaying the notifications */

    private static final String TAG = "NotificationReceiver";

    private static final int PENDING_INTENT_ID = 3147;

    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        //The request code must be the same as the same we pass to .notify later
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setContentIntent(contentIntent(context))
                        .setSmallIcon(android.R.drawable.ic_menu_sort_by_size)
                        .setContentTitle(context.getResources().getString(R.string.notification_title))
                        .setContentText(context.getResources().getString(R.string.notification_message))
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true);
        //SetAutoCancel(true) makes the notification dismissible when the user swipes it away

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        //Request code must be the same as the pending intent
        if (notificationManager != null) {
            notificationManager.notify(100, notificationBuilder.build());
        }

    }

    /** Method to create the Pending Intent */
    private static PendingIntent contentIntent (Context context) {

        //When the user clicks the notification the app will be redirected to the activity
        Intent intent = new Intent(context, DisplayNotificationsActivity.class);

        //We ensure that the activity will be replaced if needed, although the activity
        // was already open or running in the background
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
