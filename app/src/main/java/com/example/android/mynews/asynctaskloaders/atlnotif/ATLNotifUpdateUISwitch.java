package com.example.android.mynews.asynctaskloaders.atlnotif;

import android.content.Context;
import android.database.Cursor;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

/**
 * Created by Diego Fajardo on 21/04/2018.
 */

/** This ATL is called when the user reaches the Notifications Activity.
 * It returns a boolean variable to update the mSwitch variable
 * with the information from the database */
public class ATLNotifUpdateUISwitch extends android.support.v4.content.AsyncTaskLoader<Boolean> {

    private DatabaseHelper dbH;

    public ATLNotifUpdateUISwitch(Context context) {
        super(context);
        dbH = new DatabaseHelper(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Boolean loadInBackground() {

        /** We return the state of the switch in the database
         * to modify the cursor state in the activity */

        Cursor mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);
        mCursor.moveToFirst();
        return (mCursor.getInt(mCursor.getColumnIndex(DatabaseContract.Database.SWITCH_STATE)) != 0);
    }
}
