package com.example.android.mynews.asynctaskloaders.atl.atldatabase;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

/**
 * Created by Diego Fajardo on 22/04/2018.
 */

/** This ATL is called when the user clicks an article in a recycler view (in the next
 * activity that displays the article) It adds the article url to the database if is needed
 * to show the user those articles that have been read.
 */
public class ATLInsertReadArticleInDatabase extends AsyncTaskLoader<String> {

    private String url;
    private DatabaseHelper dbH;
    private Cursor mCursor;


    public ATLInsertReadArticleInDatabase(Context context, String url) {
        super(context);
        this.dbH = new DatabaseHelper(context);
        this.mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {

        int counter = 0;

        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount() ; i++) {
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.ARTICLE_URL)).equals(url)){
                counter++;
            }
        }
        if (counter == 0) {
            dbH.insertDataToAlreadyReadArticlesTable(url);
        }
        return null;
    }
}
