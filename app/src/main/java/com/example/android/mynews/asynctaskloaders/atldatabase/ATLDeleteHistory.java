package com.example.android.mynews.asynctaskloaders.atldatabase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

/**
 * Created by Diego Fajardo on 03/05/2018.
 */

public class ATLDeleteHistory extends AsyncTaskLoader<Void> {

    DatabaseHelper dbH;

    public ATLDeleteHistory(@NonNull Context context) {
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
    public Void loadInBackground() {

        dbH.deleteAllRowsFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);
        dbH.resetAutoIncrement(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

        return null;
    }
}
