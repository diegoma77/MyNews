package com.example.android.mynews.asynctaskloaders.atlHelper;

import android.content.Context;
import android.support.v4.content.Loader;

import com.example.android.mynews.asynctaskloaders.atl.ATLNotifUpdateDatabase;
import com.example.android.mynews.asynctaskloaders.atl.ATLNotifUpdateList;

import java.util.List;

/**
 * Created by Diego Fajardo on 21/04/2018.
 */

/** This class has static methods
 * that create AsyncTaskLoaders for specific reasons */

public class AsyncTaskLoaderHelper {

    /** Used in "onPause" and "onDestroy" in Notifications Activity
     * to update the database with the information of NotificationsActivity */
    public static Loader<Boolean> updateDatabase (Context context, List<String> list) {
        return new ATLNotifUpdateDatabase(context, list);
    }

    /** Used in "onResume" in Notifications Activity
     * to update the listOfQueryAndSections with the information from the Database */
    public static Loader<List<String>> updateList (Context context) {
        return new ATLNotifUpdateList(context);
    }




}

