package com.example.android.mynews.asynctaskloaders.atl;

import android.content.Context;
import android.util.Log;

import com.example.android.mynews.apirequesters.APITopStoriesRequester;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.Url;
import com.example.android.mynews.pojo.TopStoriesAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/04/2018.
 */

/** This ATL is called when the user launches the Main Activity.
 * It returns a listOfTopStoriesAPI Objects that are used to display
 * information in a recycler view */
public class ATLTopStoriesAPIRequest extends android.support.v4.content.AsyncTaskLoader <List<TopStoriesAPIObject>>{

    private static final String TAG = "ATLTopStoriesAPIRequest";

    public ATLTopStoriesAPIRequest(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.i(TAG, "onStartLoading: called +++");
        forceLoad();
    }

    @Override
    public List<TopStoriesAPIObject> loadInBackground() {
        Log.i(TAG, "loadInBackground: called +++");

        APITopStoriesRequester requester = new APITopStoriesRequester(getContext());
        requester.startJSONRequestTopStoriesAPI(Url.TopStoriesUrl.TS_FINAL_URL, getContext());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return requester.getListOfTopStoriesObjects();
    }
}
