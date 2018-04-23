package com.example.android.mynews.asynctaskloaders.atl.atldatabase;

import android.content.Context;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

/**
 * Created by Diego Fajardo on 22/04/2018.
 */

/** This ATL is called when the user reaches Main Activity.
 * It creates the database and fills the tables with the necessary
 * information if needed */
public class ATLMainActCreateDatabase extends android.support.v4.content.AsyncTaskLoader<Boolean> {

    private DatabaseHelper dbH;

    public ATLMainActCreateDatabase(Context context) {
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

        if (dbH.isTableEmpty(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME)){
            for (int i = 0; i < 7; i++) {
                dbH.insertDataToSearchQueryTable("");
            }
        }

        if (dbH.isTableEmpty(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME)) {
            dbH.insertDataToSwitchTable(0);
        }

        return true;
    }
}
