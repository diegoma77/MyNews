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
import android.util.Log;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;
import com.example.android.mynews.R;
import com.example.android.mynews.apirequesters.APISearchArticlesRequester;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.activities.DisplayNotificationsActivity;
import com.example.android.mynews.extras.helperclasses.UrlConverter;
import com.example.android.mynews.extras.interfaceswithconstants.Url;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    private Cursor cursorQueryOfSections;
    private Cursor cursorReadArticles;

    private List<String> listOfUrls;
    private List <ArticlesSearchAPIObject> listOfObjects;
    private List<String> listOfUrlsOfReadArticles;

    private static final int PENDING_INTENT_ID = 3147;

    private static final String NOTIFICATION_CHANNEL_ID = "notification_channel";

    public static final String TAG = "NotificationDailyJob";

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {

        dbH = new DatabaseHelper(getContext());
        cursorQueryOfSections = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);
        cursorReadArticles = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

        listOfUrls = new ArrayList<>();
        listOfObjects = new ArrayList<>();
        listOfUrlsOfReadArticles = new ArrayList<>();

        new Thread() {
            @Override
            public void run() {


                /********************************************************************************
                 ** 1. CREATE URLS WITH THE INFORMATION FROM SEARCH QUERY AND SECTIONS TABLE ****
                 *******************************************************************************/

                cursorQueryOfSections.moveToFirst();

                //We store the query value in a variable
                String query = cursorQueryOfSections.getString(cursorQueryOfSections.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION));

                //We create a list to store the Sections
                List<String> listOfSections = new ArrayList<>();

                //We fill the list with the info from the database
                cursorQueryOfSections.moveToNext();
                for (int i = 1; i < cursorQueryOfSections.getCount(); i++) {

                    listOfSections.add(cursorQueryOfSections.getString(cursorQueryOfSections.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION)));

                    if (i != cursorQueryOfSections.getCount()) {
                        cursorQueryOfSections.moveToNext();
                    }

                }

                Log.e(TAG, "loadInBackground: THIS IS REACHED(1)");

                //We create the dates
                Calendar calendarBeginDate = Calendar.getInstance();
                calendarBeginDate.add(Calendar.DATE, -3);
                Calendar calendarEndDate = Calendar.getInstance();

                String beginDate;
                String endDate;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                beginDate = sdf.format(calendarBeginDate.getTime());
                endDate = sdf.format(calendarEndDate.getTime());

                Log.e(TAG, "loadInBackground: THIS IS REACHED(2)");

                //We store the sections (newDesk) value in a variable
                String sections = UrlConverter.getSectionsAndAdaptForUrl(listOfSections);

                Log.e(TAG, "loadInBackground: THIS IS REACHED(3)");

                //We create the Urls
                String url1 = UrlConverter.getSearchArticlesUrl(
                        query,
                        sections,
                        beginDate,
                        endDate,
                        Url.ArticleSearchUrl.PAGE_ONE);

                String url2 = UrlConverter.getSearchArticlesUrl(
                        query,
                        sections,
                        beginDate,
                        endDate,
                        Url.ArticleSearchUrl.PAGE_TWO);

                String url3 = UrlConverter.getSearchArticlesUrl(
                        query,
                        sections,
                        beginDate,
                        endDate,
                        Url.ArticleSearchUrl.PAGE_THREE);

                Log.i(TAG, "loadInBackground: " + url1);
                Log.i(TAG, "loadInBackground: " + url2);
                Log.i(TAG, "loadInBackground: " + url3);

                Log.e(TAG, "loadInBackground: THIS IS REACHED(4)");

                listOfUrls.add(url1);
                listOfUrls.add(url2);
                listOfUrls.add(url3);


                /***************************************************************
                 ** 2. DO THE REQUEST TO ARTICLES SEARCH API. GET A LIST ****
                 **************************************************************/

                /** We do a Request to Articles Search API
                 * */

                APISearchArticlesRequester requester = new APISearchArticlesRequester(getContext());

                for (int i = 0; i < listOfUrls.size(); i++) {
                    try {
                        requester.startJSONRequestArticlesSearchAPI(listOfUrls.get(i));
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                listOfObjects = requester.getListOfArticlesSearchObjects();

                /** We get the information from Already Read Articles
                 * */

                if (cursorReadArticles != null) {
                    cursorReadArticles.moveToFirst();

                    for (int i = 0; i < cursorReadArticles.getCount(); i++) {

                        listOfUrlsOfReadArticles.add
                                (cursorReadArticles.getString(
                                        cursorReadArticles.getColumnIndex(DatabaseContract.Database.ARTICLE_URL)));

                        if(i != cursorReadArticles.getCount()){
                            cursorReadArticles.moveToNext();
                        }

                    }

                }

                /******************************************************
                 ** 3. MATCH THE ARTICLES TO SEE IF THEY WERE READ ****
                 *****************************************************/

                /** We remove from the list of Objects those articles that has been read
                 * */

                for (int i = 0; i < listOfObjects.size() ; i++) {

                    for (int j = 0; j < listOfUrlsOfReadArticles.size(); j++) {

                        if (listOfObjects.get(i).getWebUrl().equals(listOfUrlsOfReadArticles.get(j))){
                            listOfObjects.remove(i);
                        }
                    }
                }

                /******************************************************
                 ** 3. INSERT THE REMAINING LIST IN THE DATABASE ******
                 *****************************************************/

                for (int i = 0; i < listOfObjects.size(); i++) {
                    dbH.insertDataToArticlesForNotificationsTable(listOfObjects.get(i));
                }

                if (listOfObjects.size() > 0) {

                    /** We create
                     * the notification */
                    createNotification();

                } else {
                    return; //do nothing because list size is 0 }
                }
            }
        }.start();

        return DailyJobResult.SUCCESS;

    }

    /*******************************
     * STATIC METHOD TO SCHEDULE ***
     * THE NOTIFICATION ************
     * ****************************/

    public static int scheduleNotificationJob () {

        return DailyJob.schedule(new JobRequest.Builder(TAG),
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