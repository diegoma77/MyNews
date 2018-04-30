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
import com.example.android.mynews.asynctaskloaders.atl.atldatabase.ATLFillListWithReadArticles;
import com.example.android.mynews.asynctaskloaders.atl.atlrequest.ATLRequestTopStoriesAPI;
import com.example.android.mynews.extras.helperclasses.ShowHelper;
import com.example.android.mynews.rvadapters.RvAdapterTopStories;
import com.example.android.mynews.pojo.TopStoriesAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

/** Fragment that displays Top Stories news
 * using RvAdapterTopStories.
 * This fragment uses two lists:
 * topStoriesObjectList: used to display the news.
 * listOfReadArticlesUrls: filled with the urls of the articles that has been read
 * to display them in a different way (bold style)
 * */
public class PageFragmentTopStories extends android.support.v4.app.Fragment {

    //Logs
    private static final String TAG = "PageFragmentTopStories";

    //IDs to identify the Loaders
    private static final int LOADER_TOP_STORIES_API_REQUEST = 10;
    private static final int LOADER_READ_ARTICLES_DATABASE = 11;

    //Flag to specify the request to APITopStoriesRequester
    private static final int FLAG_TOP_STORIES = 1;

    //List that will store the TopStoriesObject object to display in the RecyclerView
    private List<TopStoriesAPIObject> topStoriesObjectList;

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

        topStoriesObjectList = new ArrayList<>();
        listOfReadArticlesUrls = new ArrayList<>();

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

    /** Loader used to send a Request to Top Stories API
     * */
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

    /** This Loader Callback is used to send a request to Top Stories API using an
     * AsyncTaskLoader called "ATLRequestTopStoriesAPI"
     * */
    private LoaderManager.LoaderCallbacks<List<TopStoriesAPIObject>> loaderTopStoriesAPIRequest =
            new LoaderManager.LoaderCallbacks<List<TopStoriesAPIObject>>() {

                @Override
                public Loader<List<TopStoriesAPIObject>> onCreateLoader(int id, Bundle args) {
                    Log.i(TAG, "onCreateLoader: called! +++");
                    return new ATLRequestTopStoriesAPI(getContext(), FLAG_TOP_STORIES);
                }

                @Override
                public void onLoadFinished(Loader<List<TopStoriesAPIObject>> loader, List<TopStoriesAPIObject> data) {
                    Log.i(TAG, "onLoadFinished: called! +++");
                    topStoriesObjectList.addAll(data);

                    if (topStoriesObjectList.size() != 0) {
                        ShowHelper.showRecyclerView(mProgressBar,mErrorMessageDisplay,recyclerView);
                        RvAdapterTopStories adapterTopStories = new RvAdapterTopStories(
                                getContext(),
                                topStoriesObjectList,
                                listOfReadArticlesUrls);
                        recyclerView.setAdapter(adapterTopStories);

                    } else {
                        ShowHelper.showErrorMessage(mProgressBar,mErrorMessageDisplay,recyclerView);
                        loadLoaderTopStoriesAPIRequest(LOADER_TOP_STORIES_API_REQUEST);
                        ShowHelper.showProgressBar(mProgressBar,mErrorMessageDisplay,recyclerView);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<TopStoriesAPIObject>> loader) {

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



