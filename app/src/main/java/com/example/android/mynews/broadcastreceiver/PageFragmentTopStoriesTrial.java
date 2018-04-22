package com.example.android.mynews.broadcastreceiver;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.mynews.R;
import com.example.android.mynews.asynctaskloaders.atlhelper.AsyncTaskLoaderHelper;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.helperclasses.ShowHelper;
import com.example.android.mynews.pojo.TopStoriesAPIObject;
import com.example.android.mynews.rvadapters.RvAdapterTopStories;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentTopStoriesTrial extends android.support.v4.app.Fragment {

    //Logs
    private static final String TAG = "PageFragmentTopStories";

    //Loader ID
    private static final int LOADER_TOP_STORIES_API_REQUEST = 10;

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private List<TopStoriesAPIObject> topStoriesObjectList;

    //Variables to store views related to the articles upload
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    //RecyclerView and RecyclerViewAdapter
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rv_fragments_layout, container, false);

        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        topStoriesObjectList = new ArrayList<>();

        ShowHelper.showProgressBar(mProgressBar,mErrorMessageDisplay,recyclerView);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        loadLoaderTopStoriesAPIRequest(LOADER_TOP_STORIES_API_REQUEST);

        return view;
        
    }

    /**************************
     *** LOADERS **************
     **************************/

    private void loadLoaderTopStoriesAPIRequest(int id) {

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<List<TopStoriesAPIObject>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderUpdateList: ");
            loaderManager.initLoader(id, null, loaderTopStoriesAPIRequest);
        } else {
            Log.i(TAG, "loadLoaderUpdateList: ");
            loaderManager.restartLoader(id, null, loaderTopStoriesAPIRequest);
        }
    }


    /**************************
     *** LOADER CALLBACKS *****
     **************************/

    private LoaderManager.LoaderCallbacks<List<TopStoriesAPIObject>> loaderTopStoriesAPIRequest =
            new LoaderManager.LoaderCallbacks<List<TopStoriesAPIObject>>() {

                @Override
                public Loader<List<TopStoriesAPIObject>> onCreateLoader(int id, Bundle args) {
                    Log.i(TAG, "onCreateLoader: called! +++");
                    return AsyncTaskLoaderHelper.topStoriesAPIRequest(getActivity());
                }

                @Override
                public void onLoadFinished(Loader<List<TopStoriesAPIObject>> loader, List<TopStoriesAPIObject> data) {
                    Log.i(TAG, "onLoadFinished: called! +++");
                    topStoriesObjectList.addAll(data);

                    if (topStoriesObjectList.size() != 0) {
                        ShowHelper.showRecyclerView(mProgressBar,mErrorMessageDisplay,recyclerView);
                        RvAdapterTopStoriesTrial adapterTopStories = new RvAdapterTopStoriesTrial (
                                getContext(),
                                topStoriesObjectList, this);
                    } else {
                        ShowHelper.showErrorMessage(mProgressBar,mErrorMessageDisplay,recyclerView);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<TopStoriesAPIObject>> loader) {

                }
            };

}



