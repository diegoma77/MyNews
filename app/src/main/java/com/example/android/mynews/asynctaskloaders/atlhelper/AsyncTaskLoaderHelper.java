package com.example.android.mynews.asynctaskloaders.atlhelper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.mynews.asynctaskloaders.atl.ATLDoNothing;
import com.example.android.mynews.asynctaskloaders.atl.ATLFillListWithArticlesForNotifications;
import com.example.android.mynews.asynctaskloaders.atl.ATLFillListWithArticlesForSearchArticles;
import com.example.android.mynews.asynctaskloaders.atl.atlwebview.ATLSendIntentBack;
import com.example.android.mynews.asynctaskloaders.atl.atldatabase.ATLFillListWithReadArticles;
import com.example.android.mynews.asynctaskloaders.atl.atldatabase.ATLInsertArticleInDatabase;
import com.example.android.mynews.asynctaskloaders.atl.atldatabase.ATLMainActCreateDatabase;
import com.example.android.mynews.asynctaskloaders.atl.atlnotif.ATLNotifUpdateDatabase;
import com.example.android.mynews.asynctaskloaders.atl.atlnotif.ATLNotifUpdateList;
import com.example.android.mynews.asynctaskloaders.atl.atlnotif.ATLNotifUpdateSwitchDatabase;
import com.example.android.mynews.asynctaskloaders.atl.atlnotif.ATLNotifUpdateSwitchVariable;
import com.example.android.mynews.asynctaskloaders.atl.atlrequest.ATLRequestMostPopularAPI;
import com.example.android.mynews.asynctaskloaders.atl.atlrequest.ATLSearchArticlesAPIRequestAndFillArticlesForSearchArticlesTable;
import com.example.android.mynews.asynctaskloaders.atl.atlrequest.ATLRequestTopStoriesAPI;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;
import com.example.android.mynews.pojo.MostPopularAPIObject;
import com.example.android.mynews.pojo.TopStoriesAPIObject;

import java.util.List;

/**
 * Created by Diego Fajardo on 21/04/2018.
 */

/** This class has static methods
 * that create AsyncTaskLoaders for specific reasons */

public class AsyncTaskLoaderHelper {

    private static final String TAG = "AsyncTaskLoaderHelper";

    /** Used to do sth in the background
     * but only on the Loader Callback*/
    public static Loader<Void> doNothing(Context context) {
        return new ATLDoNothing(context);
    }

    /************************
     * REQUESTS TO API ******
     ***********************/

    /** Used to do requests
     * to TopStories API*/
    public static Loader<List<TopStoriesAPIObject>> topStoriesAPIRequest(Context context, int flag) {
        return new ATLRequestTopStoriesAPI(context, flag);
    }

    /** Used to do requests
     * to MostPopular API*/
    public static Loader<List<MostPopularAPIObject>> mostPopularAPIRequest(Context context) {
        return new ATLRequestMostPopularAPI(context);
    }

    /** Used to do requests
     * to ArticlesSearch API*/
    public static Loader<List<ArticlesSearchAPIObject>> articlesSearchAPIRequest(Context context, List <String> listOfUrls) {
        return new ATLSearchArticlesAPIRequestAndFillArticlesForSearchArticlesTable(context, listOfUrls);
    }


    /*****************************
     * DATABASE METHODS, GENERAL **
     ****************************/

    /** Used to get an ArticlesSearchAPI objects list
     * from the Database  */
    public static Loader<List<ArticlesSearchAPIObject>> getListFromDatabaseArticlesForSearchArticles(Context context){
        return new ATLFillListWithArticlesForSearchArticles(context);
    }

    public static Loader<List<ArticlesSearchAPIObject>> getListFromDatabaseArticlesForNotifications(Context context){
        return new ATLFillListWithArticlesForNotifications(context);
    }

    /** Used in MainActivity to create
     * the databases and fill the tables */
    public static Loader<Boolean> createDatabaseIfDoesntExist(Context context) {
        return new ATLMainActCreateDatabase(context);
    }

    /** Used to fill a list with all
     * the urls of the articles read */
    public static Loader<List<String>> getArticlesReadFromDatabase(Context context) {
        return new ATLFillListWithReadArticles(context);
    }

    /** Used to insert an article url in the database when the user clicks
     * the article in the recycler view. If the article already exists in the database
     * nothing is added */
    public static Loader<String> insertArticleUrlInDatabase(Context context, String url) {
        return new ATLInsertArticleInDatabase(context, url);
    }


    /*****************************
     * USED IN WEBVIEW **
     ****************************/

    public static Loader<Intent> sendIntentAndBringBack(Context context, Intent intent) {
        return new ATLSendIntentBack(context, intent);
    }


    /***************************************
     * USED IN NOTIFICATIONS ACTIVITY ******
     ***************************************/

    /** Used in "onPause" and "onDestroy" in Notifications Activity
     * to update the database with the information of NotificationsActivity */
    public static Loader<Boolean> updateQueryAndSectionsTable(Context context, List<String> list) {
        Log.i(TAG, "updateQueryAndSectionsTable: +++");
        return new ATLNotifUpdateDatabase(context, list);
    }

    public static Loader<Boolean> updateSwitchTable (Context context, boolean switchState) {
        Log.i(TAG, "updateSwitchTable: +++");
        return new ATLNotifUpdateSwitchDatabase(context, switchState);
    }

    /** Used in "onResume" in Notifications Activity
     * to update the listOfQueryAndSections with the information from the Database */
    public static Loader<List<String>> updateListOfQueryAndSections(Context context) {
        Log.i(TAG, "updateListOfQueryAndSections: +++");
        return new ATLNotifUpdateList(context);
    }

    public static Loader<Boolean> updateSwitchVariable(Context context) {
        Log.i(TAG, "updateSwitchVariable: +++");
        return new ATLNotifUpdateSwitchVariable(context);
    }


}

