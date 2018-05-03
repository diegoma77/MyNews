package com.example.android.mynews.asynctaskloaders.atlnotif;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.helperclasses.UrlConverter;
import com.example.android.mynews.extras.interfaceswithconstants.Url;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Diego Fajardo on 02/05/2018.
 */

public class ATLNotifFillURLsTable extends android.support.v4.content.AsyncTaskLoader<List<String>> {

    private static final String TAG = "ATLNotifFillURLsTable";

    private DatabaseHelper dbH;
    private Cursor mCursor;

    private List<String> listOfUrls;

    public ATLNotifFillURLsTable(Context context) {
        super(context);
        dbH = new DatabaseHelper(context);
        dbH.deleteAllRowsFromTableName(DatabaseContract.Database.URLS_FOR_NOTIFICATIONS_TABLE_NAME);
        dbH.resetAutoIncrement(DatabaseContract.Database.URLS_FOR_NOTIFICATIONS_TABLE_NAME);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);
        listOfUrls = new ArrayList<>();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<String> loadInBackground() {

        mCursor.moveToFirst();

        //We store the query value in a variable
        String query = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION));

        //We create a list to store the Sections
        List <String> listOfSections = new ArrayList<>();

        //We fill the list with the info from the database
        mCursor.moveToNext();
        for (int i = 1; i < mCursor.getCount(); i++) {

            listOfSections.add(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION)));

            if (i != mCursor.getCount()) {
                mCursor.moveToNext();
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

        //We insert the URLs in the database
        dbH.insertDataToUrlsForNotificationsTable(url1);
        dbH.insertDataToUrlsForNotificationsTable(url2);
        dbH.insertDataToUrlsForNotificationsTable(url3);

        Log.e(TAG, "loadInBackground: THIS IS REACHED(4)");

        listOfUrls.add(url1);
        listOfUrls.add(url2);
        listOfUrls.add(url3);


        return listOfUrls;
    }

}
