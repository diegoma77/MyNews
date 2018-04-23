package com.example.android.mynews.asynctaskloaders.atl.atlrequest;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import java.util.List;

/**
 * Created by Diego Fajardo on 23/04/2018.
 */

public class ATLSearchArticlesAPIRequest extends AsyncTaskLoader<List<ArticlesSearchAPIObject>> {


    public ATLSearchArticlesAPIRequest(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ArticlesSearchAPIObject> loadInBackground() {
        return null;
    }
}
