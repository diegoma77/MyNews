package com.example.android.mynews.asynctaskloaders.atl;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Diego Fajardo on 24/04/2018.
 */

public class ATLDoNothing extends AsyncTaskLoader<Void> {

    public ATLDoNothing(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Void loadInBackground() {
        return null;
    }

}
