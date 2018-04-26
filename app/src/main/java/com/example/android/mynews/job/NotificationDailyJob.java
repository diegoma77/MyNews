package com.example.android.mynews.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;
import com.example.android.mynews.R;
import com.example.android.mynews.activities.DisplayNotificationsActivity;
import com.example.android.mynews.apirequesters.APISearchArticlesRequester;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

import java.util.concurrent.TimeUnit;

/**
 * Created by Diego Fajardo on 26/04/2018.
 */

/**
* Evernote Android Job Library: DAILY JOB. Used to run a desired piece of work. In this case,
 * it does a Search Articles API request, checks if there are new articles
 * to read and, if there are, it sends a notification between 9 and 10
 */
public class NotificationDailyJob extends DailyJob {

    private DatabaseHelper dbH;
    private Cursor mCursor;

    private static final int PENDING_INTENT_ID = 3147;

    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";

    public static final String TAG = "NotificationDailyJob";

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {

        dbH = new DatabaseHelper(getContext());
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.URLS_FOR_NOTIFICATIONS_TABLE_NAME);

        new Thread() {
            @Override
            public void run() {

                APISearchArticlesRequester requester = new APISearchArticlesRequester(getContext());

                /** The requests has two lists as fields:
                 * 1. A list of strings: listOfUrls
                 * 2. A list of SearchArticlesAPIObjects: listOfArticlesSearchObjects */

                /** We add the urls to the requester so it can do the requests to those urls */
                mCursor.moveToFirst();
                for (int i = 0; i < mCursor.getCount(); i++) {

                    requester.addUrl(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.URL)));

                    if (i != mCursor.getCount()) {
                        mCursor.moveToNext();
                    }
                }

                /** We do the requests. Each one fills one list of the request
                 * with ArticlesSearchAPIObjects */
                for (int i = 0; i < requester.getlistOfUrlsSize(); i++) {
                    requester.startJSONRequestArticlesSearchAPI(requester.getUrl(i));
                }

                /** We insert data that will be afterwards displayed in
                 * DisplayNotificationsActivity.
                 * If there are objects in the list after the API Request, we insert them in
                 * the database. If there aren't we do nothing */
                if (requester.getListOfArticlesSearchObjects() != null) {

                    for (int i = 0; i < requester.getListOfArticlesSearchObjects().size(); i++) {
                        dbH.insertDataToArticlesForNotificationsTable(
                                requester.getListOfArticlesSearchObjects().get(i));
                    }

                    /** We create
                     * the notification */
                    createNotification();

                } else {
                    return; //do nothing because list is null }
                }
            }
        }.start();

        return DailyJobResult.SUCCESS;

    }

    /*******************************
     * STATIC METHOD TO SCHEDULE ***
     * THE NOTIFICATION ************
     * ****************************/

    public static void scheduleNotificationJob (int id) {

        int JobId = id;

        JobId = DailyJob.schedule(new JobRequest.Builder(TAG),
                TimeUnit.HOURS.toMillis(9),
                TimeUnit.HOURS.toMillis(10));
    }


    /*******************************
     * METHOD TO CREATE ************
     * THE NOTIFICATIONS ***********
     * ****************************/

    /** Method to create
     * the Pending Intent
     */
    private PendingIntent contentIntent (Context context) {

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

    /** Method to create
     * the Notification
     */
    private void createNotification() {

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(
                Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    getContext().getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        //The request code must be the same as the same we pass to .notify later
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID)
                        .setContentIntent(contentIntent(getContext()))
                        .setSmallIcon(android.R.drawable.ic_menu_sort_by_size)
                        .setContentTitle(getContext().getResources().getString(R.string.notification_title))
                        .setContentText(getContext().getResources().getString(R.string.notification_message))
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
}

