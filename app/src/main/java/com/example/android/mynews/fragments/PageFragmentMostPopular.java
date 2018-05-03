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
import com.example.android.mynews.asynctaskloaders.atlfilllist.ATLFillListWithReadArticles;
import com.example.android.mynews.asynctaskloaders.atlrequest.ATLRequestMostPopularAPI;
import com.example.android.mynews.extras.helperclasses.ShowHelper;
import com.example.android.mynews.pojo.MostPopularAPIObject;
import com.example.android.mynews.rvadapters.RvAdapterMostPopular;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

/** Fragment that displays MostPopular news
 * using RvAdapterMostPopular.
 * This fragment uses two lists:
 * topStoriesObjectList: used to display the news.
 * listOfReadArticlesUrls: filled with the urls of the articles that has been read
 * to display them in a different way (bold style)
 * */
public class PageFragmentMostPopular extends android.support.v4.app.Fragment {

    //Log
    private static final String TAG = "PageFragmentMostPopular";

    //IDs to identify the Loaders
    private static final int LOADER_MOST_POPULAR_API_REQUEST = 5;
    private static final int LOADER_READ_ARTICLES_DATABASE = 6;

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private List<MostPopularAPIObject> mostPopularObjectsList;

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
        loadLoaderMostPopularAPIRequest(LOADER_MOST_POPULAR_API_REQUEST);

        return view;

    }

    /**************************
     *** LOADERS **************
     **************************/

    /** Loader used to send a Request to MostPopular API
     * */
    private void loadLoaderMostPopularAPIRequest(int id) {

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<List<MostPopularAPIObject>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderUpdateList: ");
            loaderManager.initLoader(id, null, loaderMostPopularAPIRequest);
        } else {
            Log.i(TAG, "loadLoaderUpdateList: ");
            loaderManager.restartLoader(id, null, loaderMostPopularAPIRequest);
        }
    }

    /** Loader used to fill a list with the articles that had been read before
     * */
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

    /** This Loader Callback is used to send a request to MostPopular API using an
     * AsyncTaskLoader called "ATLRequestMostPopularAPI"
     * */
    private LoaderManager.LoaderCallbacks<List<MostPopularAPIObject>> loaderMostPopularAPIRequest =
            new LoaderManager.LoaderCallbacks<List<MostPopularAPIObject>>() {

                @Override
                public Loader<List<MostPopularAPIObject>> onCreateLoader(int id, Bundle args) {
                    Log.i(TAG, "onCreateLoader: called! +++");
                    return new ATLRequestMostPopularAPI(getContext());
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
                        loadLoaderMostPopularAPIRequest(LOADER_MOST_POPULAR_API_REQUEST);
                        ShowHelper.showProgressBar(mProgressBar,mErrorMessageDisplay,recyclerView);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<MostPopularAPIObject>> loader) {

                }
            };

    /** This Loader Callback is used to fill a list with all the read articles using an
     * AsyncTaskLoader called "ATLFillListWithReadArticles"
     * */
    private android.support.v4.app.LoaderManager.LoaderCallbacks<List<String>> loaderGetReadArticlesFromDatabase =
            new android.support.v4.app.LoaderManager.LoaderCallbacks<List<String>>() {

                @Override
                public Loader<List<String>> onCreateLoader(int id, Bundle args) {
                    return new ATLFillListWithReadArticles(getContext());
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



