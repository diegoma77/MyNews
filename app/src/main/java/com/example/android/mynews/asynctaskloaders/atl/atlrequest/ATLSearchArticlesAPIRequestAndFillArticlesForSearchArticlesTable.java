package com.example.android.mynews.asynctaskloaders.atl.atlrequest;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mynews.apirequesters.APIMostPopularRequester;
import com.example.android.mynews.apirequesters.APISearchArticlesRequester;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.interfaceswithconstants.Url;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;
import com.example.android.mynews.pojo.MostPopularAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 23/04/2018.
 */

/** This ATL is called when the user does a Search in SearchArticles activity
 * or when the user launches the app using a notification.
 * It returns a listOfArticlesSearchAPI Objects that are used to display
 * information in a recycler view */
public class ATLSearchArticlesAPIRequestAndFillArticlesForSearchArticlesTable extends AsyncTaskLoader<List<ArticlesSearchAPIObject>> {

    private List<String> listOfUrls;

    private DatabaseHelper dbH;

    public ATLSearchArticlesAPIRequestAndFillArticlesForSearchArticlesTable(Context context, List<String> listOfUrls) {
        super(context);
        this.listOfUrls = new ArrayList<>();
        this.listOfUrls = listOfUrls;
        this.dbH = new DatabaseHelper(context);
        if (!dbH.isTableEmpty(DatabaseContract.Database.ARTICLES_FOR_SEARCH_ARTICLES_TABLE_NAME)){
            dbH.deleteAllRowsFromTableName(DatabaseContract.Database.ARTICLES_FOR_SEARCH_ARTICLES_TABLE_NAME);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ArticlesSearchAPIObject> loadInBackground() {

        APISearchArticlesRequester requester = new APISearchArticlesRequester(getContext());

        for (int i = 0; i < listOfUrls.size(); i++) {
            try {
                requester.startJSONRequestArticlesSearchAPI(listOfUrls.get(i));
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List <ArticlesSearchAPIObject> listOfObjects;
        listOfObjects = requester.getListOfArticlesSearchObjects();

        for (int i = 0; i < listOfObjects.size(); i++) {

            dbH.insertDataToArticlesForSearchArticlesTable(listOfObjects.get(i));

        }

        return requester.getListOfArticlesSearchObjects();

    }
}
