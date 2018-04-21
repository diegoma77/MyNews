package com.example.android.mynews.asynctaskloaders.atlHelper;

import android.content.Context;
import android.support.v4.content.Loader;

import com.example.android.mynews.asynctaskloaders.atl.ATLNotifUpdateDatabase;
import com.example.android.mynews.asynctaskloaders.atl.ATLNotifUpdateList;

import java.util.List;

/**
 * Created by Diego Fajardo on 21/04/2018.
 */

public class AsyncTaskLoaderHelper {

    /** Used in "onPause" and "onDestroy" to update the database with the information of
     * NotificationsActivity */
    public static Loader<Boolean> updateDatabase (Context context, List<String> list) {
        return new ATLNotifUpdateDatabase(context, list);
    }

    /** Used in "onResume" to update the listOfQueryAndSections with the information from the
     * Database */
    public static Loader<List<String>> updateList (Context context) {
        return new ATLNotifUpdateList(context);
    }





    public static Loader<String> updateTextTwo (Context context) {
        return new AsyncTaskLoaderUpdateTextTwo(context);
    }

    public static Loader<List<String>> createListOfStrings (Context context) {
        return new AsyncTaskLoaderCreateListOfStrings(context);
    }

}

