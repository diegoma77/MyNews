package com.example.android.mynews.asynctaskloaders.atl.atlnotif;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 21/04/2018.
 */

/** This ATL is called when the user reaches the Notifications Activity.
 * It returns a list to fill the listOfQueryAndSections with the information from the database */
public class ATLNotifUpdateList extends android.support.v4.content.AsyncTaskLoader<List<String>> {

    private static final String TAG = "ATLNotifUpdateList";

    private DatabaseHelper dbH;

    public ATLNotifUpdateList(Context context) {
        super(context);
        dbH = new DatabaseHelper(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<String> loadInBackground() {

        /** We create a list */
        List<String> list = new ArrayList<>();
        Cursor mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);

        /** We fill the list with the information from the database (query and section) */
        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount(); i++) {
            list.add(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION)));
            if (i != mCursor.getCount()) { mCursor.moveToNext(); }
        }

        Log.i(TAG, "loadInBackground: list.size = " + list.size());

        if (list.size() != 0) {
            return list;
        } else { return null; }

    }
}