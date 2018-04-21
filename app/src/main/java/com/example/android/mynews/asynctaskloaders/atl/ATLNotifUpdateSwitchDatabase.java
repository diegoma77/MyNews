package com.example.android.mynews.asynctaskloaders.atl;

import android.content.Context;

import com.example.android.mynews.data.DatabaseHelper;

import java.util.List;

/**
 * Created by Diego Fajardo on 21/04/2018.
 */

/** This ATL is called when the user leaves the Notifications Activity.
 * It updates the database with the information from the mSwitch variable */
public class ATLNotifUpdateSwitchDatabase extends android.support.v4.content.AsyncTaskLoader<Boolean> {

    private DatabaseHelper dbH;

    private boolean switchState;

    public ATLNotifUpdateSwitchDatabase(Context context, boolean switchState) {
        super(context);
        dbH = new DatabaseHelper(context);
        this.switchState = switchState;

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Boolean loadInBackground() {

        /** We update the database with the information from the activity (list) */

        if (switchState) {
            dbH.setSwitchOnInDatabase();
            return true;
        } else {
            dbH.setSwitchOffInDatabase();
            return false;
        }
    }
}
