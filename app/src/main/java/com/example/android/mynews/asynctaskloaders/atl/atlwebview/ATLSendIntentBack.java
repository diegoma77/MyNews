package com.example.android.mynews.asynctaskloaders.atl.atlwebview;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;

/**
 * Created by Diego Fajardo on 24/04/2018.
 */

/** This ATL is used to send an intent back to DisplaySearchArticlesActivity to fill
 * the list of elements that will displayed in the recycler view (which will prevent the
 * app from crashing).
 * */
public class ATLSendIntentBack extends android.support.v4.content.AsyncTaskLoader<Intent> {

    private Intent intent;

    public ATLSendIntentBack(Context context, Intent intent) {
        super(context);
        this.intent = intent;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Intent loadInBackground() {
        return intent;
    }
}
