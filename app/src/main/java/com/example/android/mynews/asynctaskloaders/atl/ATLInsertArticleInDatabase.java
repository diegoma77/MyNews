package com.example.android.mynews.asynctaskloaders.atl;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mynews.data.DatabaseHelper;

/**
 * Created by Diego Fajardo on 22/04/2018.
 */

public class ATLInsertArticleInDatabase extends AsyncTaskLoader<String> {

    private DatabaseHelper dbH;
    private String url;

    public ATLInsertArticleInDatabase(Context context, String url) {
        super(context);
        this.dbH = new DatabaseHelper(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        dbH.insertDataToAlreadyReadArticlesTable(url);
        return null;
    }
}
