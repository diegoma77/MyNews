package com.example.android.mynews.fragments;

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
import com.example.android.mynews.pojo.MostPopularAPIObject;
import com.example.android.mynews.pojo.TopStoriesAPIObject;
import com.example.android.mynews.rvadapters.RvAdapterMostPopular;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentMostPopular extends android.support.v4.app.Fragment {

    private static final String TAG = "PageFragmentMostPopular";

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private List<MostPopularAPIObject> mostPopularObjectsList;

    //Loader ID
    private static final int LOADER_MOST_POPULAR_API_REQUEST = 55;
    private static final int LOADER_READ_ARTICLES_DATABASE = 15;

    //List that will store the urls of the read articles in the database
    private List<String> listOfReadArticlesUrls;

    //Variables to store views related to the articles upload
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    //RecyclerView
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rv_fragments_layout, container, false);

        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mostPopularObjectsList = new ArrayList<>();
        listOfReadArticlesUrls = new ArrayList<>();

        ShowHelper.showProgressBar(mProgressBar,mErrorMessageDisplay,recyclerView);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        loadLoaderGetReadArticlesFromDatabase(LOADER_READ_ARTICLES_DATABASE);
        loadLoaderTopStoriesAPIRequest(LOADER_MOST_POPULAR_API_REQUEST);

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
            loaderManager.initLoader(id, null, loaderMostPopularAPIRequest);
        } else {
            Log.i(TAG, "loadLoaderUpdateList: ");
            loaderManager.restartLoader(id, null, loaderMostPopularAPIRequest);
        }
    }

    private void loadLoaderGetReadArticlesFromDatabase(int id) {

        android.support.v4.app.LoaderManager loaderManager = getActivity().getSupportLoaderManager();
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

    private LoaderManager.LoaderCallbacks<List<MostPopularAPIObject>> loaderMostPopularAPIRequest =
            new LoaderManager.LoaderCallbacks<List<MostPopularAPIObject>>() {

                @Override
                public Loader<List<MostPopularAPIObject>> onCreateLoader(int id, Bundle args) {
                    Log.i(TAG, "onCreateLoader: called! +++");
                    return AsyncTaskLoaderHelper.mostPopularAPIRequest(getActivity());
                }

                @Override
                public void onLoadFinished(Loader<List<MostPopularAPIObject>> loader, List<MostPopularAPIObject> data) {
                    Log.i(TAG, "onLoadFinished: called! +++");
                    mostPopularObjectsList.addAll(data);

                    if (mostPopularObjectsList.size() != 0) {
                        ShowHelper.showRecyclerView(mProgressBar,mErrorMessageDisplay,recyclerView);
                        RvAdapterMostPopular adapterTopStories = new RvAdapterMostPopular(
                                getContext(),
                                mostPopularObjectsList,
                                listOfReadArticlesUrls);
                        recyclerView.setAdapter(adapterTopStories);

                    } else {
                        ShowHelper.showErrorMessage(mProgressBar,mErrorMessageDisplay,recyclerView);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<MostPopularAPIObject>> loader) {

                }
            };

    private android.support.v4.app.LoaderManager.LoaderCallbacks<List<String>> loaderGetReadArticlesFromDatabase =
            new android.support.v4.app.LoaderManager.LoaderCallbacks<List<String>>() {

                @Override
                public Loader<List<String>> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.getArticlesReadFromDatabase(getContext());
                }

                @Override
                public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
                    listOfReadArticlesUrls.addAll(data);
                }

                @Override
                public void onLoaderReset(Loader<List<String>> loader) {

                }
            };








}



