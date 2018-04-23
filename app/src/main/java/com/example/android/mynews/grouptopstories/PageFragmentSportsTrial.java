package com.example.android.mynews.grouptopstories;

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
import com.example.android.mynews.extras.helperclasses.ShowHelper;
import com.example.android.mynews.pojo.TopStoriesAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentSportsTrial extends android.support.v4.app.Fragment {

    //Logs
    private static final String TAG = "PageFragmentSportsTrial";

    //Loader ID
    private static final int LOADER_TOP_STORIES_API_REQUEST = 60;
    private static final int LOADER_READ_ARTICLES_DATABASE = 61;

    //Flag to specify the request to APITopStoriesRequester
    private static final int FLAG = 3;

    //List that will store the TopStoriesObject object to display in the RecyclerView
    private List<TopStoriesAPIObject> topStoriesObjectList;

    //List that will store the objects in the database
    private List<String> listOfUrls;

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
        listOfUrls = new ArrayList<>();

        ShowHelper.showProgressBar(mProgressBar,mErrorMessageDisplay,recyclerView);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        loadLoaderGetReadArticlesFromDatabase(LOADER_READ_ARTICLES_DATABASE);
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

    private void loadLoaderGetReadArticlesFromDatabase(int id) {

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<List<String>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: ");
            loaderManager.initLoader(id, null, loaderGetReadArticlesFromDatabase);
        } else {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: ");
            loaderManager.restartLoader(id, null, loaderGetReadArticlesFromDatabase);
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
                    return AsyncTaskLoaderHelper.topStoriesAPIRequest(getActivity(), FLAG);
                }

                @Override
                public void onLoadFinished(Loader<List<TopStoriesAPIObject>> loader, List<TopStoriesAPIObject> data) {
                    Log.i(TAG, "onLoadFinished: called! +++");
                    topStoriesObjectList.addAll(data);

                    if (topStoriesObjectList.size() != 0) {
                        ShowHelper.showRecyclerView(mProgressBar,mErrorMessageDisplay,recyclerView);
                        RvAdapterTopStoriesTrial adapterTopStories = new RvAdapterTopStoriesTrial (
                                getContext(),
                                topStoriesObjectList,
                                listOfUrls);
                        recyclerView.setAdapter(adapterTopStories);

                    } else {
                        ShowHelper.showErrorMessage(mProgressBar,mErrorMessageDisplay,recyclerView);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<TopStoriesAPIObject>> loader) {

                }
            };

    private LoaderManager.LoaderCallbacks<List<String>> loaderGetReadArticlesFromDatabase =
            new LoaderManager.LoaderCallbacks<List<String>>() {

                @Override
                public Loader<List<String>> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.getArticlesReadFromDatabase(getContext());
                }

                @Override
                public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
                    listOfUrls.addAll(data);
                }

                @Override
                public void onLoaderReset(Loader<List<String>> loader) {

                }
            };

}



