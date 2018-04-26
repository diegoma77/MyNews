package com.example.android.mynews.asynctaskloaders.atl.atlrequest;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mynews.apirequesters.APIMostPopularRequester;
import com.example.android.mynews.extras.interfaceswithconstants.Url;
import com.example.android.mynews.pojo.MostPopularAPIObject;

import java.util.List;

/**
 * Created by Diego Fajardo on 23/04/2018.
 */


/** This ATL is called when the user launches the Main Activity.
 * It returns a listOfMostPopularAPI Objects that are used to display
 * information in a recycler view */

public class ATLRequestMostPopularAPI extends AsyncTaskLoader  <List<MostPopularAPIObject>>{

    public ATLRequestMostPopularAPI(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<MostPopularAPIObject> loadInBackground() {

        APIMostPopularRequester requester = new APIMostPopularRequester(getContext());
        requester.startJSONRequestMostPopularAPI(Url.MostPopularUrl.MP_FINAL_URL);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return requester.getListOfMostPopularObjects();

    }
}
