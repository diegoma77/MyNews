package com.example.android.mynews.asynctaskloaders.atl.atldatabase;

import android.content.Context;
import android.database.Cursor;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/04/2018.
 */

/** This ATL is called when the user uses an Adapter for the RecyclerView.
 * It returns a list of read articles (the urls) so the app can show
 * differently those articles that have been read */
public class ATLFillListWithReadArticles extends android.support.v4.content.AsyncTaskLoader<List<String>> {

    private DatabaseHelper dbH;
    private Cursor mCursor;

    public ATLFillListWithReadArticles(Context context) {
        super(context);
        this.dbH = new DatabaseHelper(context);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<String> loadInBackground() {

        List<String> list = new ArrayList<>();

        mCursor.moveToFirst();

        for (int i = 0; i < mCursor.getCount(); i++) {
            list.add(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.ARTICLE_URL)));

            if (i != mCursor.getCount()){
                mCursor.moveToNext();
            }
        }

        return list;
    }
}
