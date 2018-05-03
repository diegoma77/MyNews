package com.example.android.mynews.asynctaskloaders.atlnotif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

/**
 * Created by Diego Fajardo on 03/05/2018.
 */

public class ATLNotifCheckIfArticlesForNotificationsIsNotEmpty extends AsyncTaskLoader<Boolean> {

    DatabaseHelper dbH;

    public ATLNotifCheckIfArticlesForNotificationsIsNotEmpty(@NonNull Context context) {
        super(context);
        dbH = new DatabaseHelper(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Boolean loadInBackground() {

        if (!dbH.isTableEmpty(DatabaseContract.Database.ARTICLES_FOR_NOTIFICATION_TABLE_NAME)) {
            return true;
        } else {
            return false;
        }

    }
}
