package com.example.android.mynews.asynctaskloaders.atlnotif;

import android.content.Context;

import com.example.android.mynews.data.DatabaseHelper;

import java.util.List;

/**
 * Created by Diego Fajardo on 21/04/2018.
 */

/** This ATL is called when the user leaves the Notifications Activity.
 * It updates the database with information from the listOfQueryAndSections */
public class ATLNotifUpdateQueryAndSectionsTable extends android.support.v4.content.AsyncTaskLoader<Boolean> {

    private List<String> list;

    private DatabaseHelper dbH;

    public ATLNotifUpdateQueryAndSectionsTable(Context context, List<String> list) {
        super(context);
        dbH = new DatabaseHelper(context);
        this.list = list;

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Boolean loadInBackground() {

        /** We update the database with the information from the activity (list) */
        if (list.size() != 0){
            for (int i = 0; i < list.size(); i++) {
                dbH.updateSearchQueryOrSection(list.get(i), i+1);
            }
            return true;
        } else return false;

    }
}

