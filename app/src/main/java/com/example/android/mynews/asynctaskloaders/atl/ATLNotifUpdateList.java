package com.example.android.mynews.asynctaskloaders.atl;

import android.content.Context;
import android.database.Cursor;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 21/04/2018.
 */

public class ATLNotifUpdateList extends android.support.v4.content.AsyncTaskLoader<List<String>> {

    DatabaseHelper dbH;

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

        /** We create a list with the information from the database (query and section) */
        List<String> list = new ArrayList<>();
        Cursor mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_OR_SECTION_TABLE_NAME);

        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount(); i++) {
            list.add(mCursor.getColumnIndex(mCursor.getString(DatabaseContract.Database.QUERY_OR_SECTION)));

        }





        if (list.size() != 0){
            for (int i = 0; i < list.size(); i++) {
                dbH.updateSearchQueryOrSection(list.get(i), i+1);
            }
            return true;
        } else return false;

    }
}