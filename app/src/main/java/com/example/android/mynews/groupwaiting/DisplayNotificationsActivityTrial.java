package com.example.android.mynews.groupwaiting;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.mynews.R;
import com.example.android.mynews.activities.DisplayNotificationsActivity;
import com.example.android.mynews.activities.MainActivity;
import com.example.android.mynews.asynctaskloaders.atlhelper.AsyncTaskLoaderHelper;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.helperclasses.DateHelper;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;
import com.example.android.mynews.extras.interfaceswithconstants.Url;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;
import com.example.android.mynews.rvadapters.RvAdapterDisplaySearchArticles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.android.mynews.activities.MainActivity.setOverflowButtonColor;

public class DisplayNotificationsActivityTrial extends AppCompatActivity {

    //Tag variable
    private static final String TAG = "DisplayNotificationsAct";

    // TODO: 24/04/2018 Define
    private static final int LOADER_GET_LIST_IN_BACKGROUND = 41;
    private static final int LOADER_READ_ARTICLES_DATABASE = 43;

    //List that will store the JSON Response objects
    private List<ArticlesSearchAPIObject> articlesSearchAPIObjectList;

    //List that will store the urls of the read articles in the database
    private List<String> listOfReadArticlesUrls;

    //Toolbar variable
    private Toolbar toolbar;

    //RecyclerView Variables
    private RecyclerView recyclerView;
    private RvAdapterDisplaySearchArticlesTrial rvAdapterDisplaySearchArticlesTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_found_articles);

        //Sets the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Displays home button in toolbar
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Changes the color of the Toolbar Overflow ButtonListener to white
        setOverflowButtonColor(toolbar, Color.WHITE);

        //Instantiating the lists to store the objects
        articlesSearchAPIObjectList = new ArrayList<>();
        listOfReadArticlesUrls = new ArrayList<>();

        //Instantiating and preparing the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapterDisplaySearchArticlesTrial = new RvAdapterDisplaySearchArticlesTrial(
                DisplayNotificationsActivityTrial.this,
                articlesSearchAPIObjectList,
                listOfReadArticlesUrls);

        //We get the list from the activity that passed it (SearchArticlesActivity or WebViewSearchActivity)
        loadLoaderGetListFromDatabaseInBackground(LOADER_GET_LIST_IN_BACKGROUND);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DisplayNotificationsActivityTrial.this, NotificationsActivityTrial.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**************************
     *** LOADERS **************
     **************************/

    private void loadLoaderGetListFromDatabaseInBackground(int id) {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<ArticlesSearchAPIObject>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderGetListFromDatabaseInBackground: init");
            loaderManager.initLoader(id, null, loaderGetListInBackground);
        } else {
            Log.i(TAG, "loadLoaderGetListFromDatabaseInBackground: restart");
            loaderManager.restartLoader(id, null, loaderGetListInBackground);
        }

    }

    private void loadLoaderGetReadArticlesFromDatabase(int id) {

        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<ArticlesSearchAPIObject>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: init");
            loaderManager.initLoader(id, null, loaderGetReadArticlesFromDatabase);
        } else {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: restart");
            loaderManager.restartLoader(id, null, loaderGetReadArticlesFromDatabase);
        }
    }

    /**************************
     *** LOADER CALLBACKS *****
     **************************/

    private LoaderManager.LoaderCallbacks<List<ArticlesSearchAPIObject>> loaderGetListInBackground =
            new LoaderManager.LoaderCallbacks<List<ArticlesSearchAPIObject>>() {


                @Override
                public Loader<List<ArticlesSearchAPIObject>> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.getListFromDatabaseArticlesForSearchArticles(DisplayNotificationsActivityTrial.this);
                }

                @Override
                public void onLoadFinished(Loader<List<ArticlesSearchAPIObject>> loader, List<ArticlesSearchAPIObject> data) {

                    if (data != null) {
                        articlesSearchAPIObjectList.addAll(data);
                    }

                    Log.i(TAG, "onLoadFinished: list_size:" + articlesSearchAPIObjectList.size());

                    rvAdapterDisplaySearchArticlesTrial = new RvAdapterDisplaySearchArticlesTrial(
                            DisplayNotificationsActivityTrial.this,
                            articlesSearchAPIObjectList,
                            listOfReadArticlesUrls);
                    recyclerView.setAdapter(rvAdapterDisplaySearchArticlesTrial);

                    loadLoaderGetReadArticlesFromDatabase(LOADER_READ_ARTICLES_DATABASE);
                }

                @Override
                public void onLoaderReset(Loader<List<ArticlesSearchAPIObject>> loader) {

                }

            };

    private android.support.v4.app.LoaderManager.LoaderCallbacks<List<String>> loaderGetReadArticlesFromDatabase =
            new android.support.v4.app.LoaderManager.LoaderCallbacks<List<String>>() {

                @Override
                public Loader<List<String>> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.getArticlesReadFromDatabase(DisplayNotificationsActivityTrial.this);
                }

                @Override
                public void onLoadFinished(Loader<List<String>> loader, List<String> data) {

                    if (data != null) {
                        listOfReadArticlesUrls.addAll(data);
                    }

                    rvAdapterDisplaySearchArticlesTrial = new RvAdapterDisplaySearchArticlesTrial(
                            DisplayNotificationsActivityTrial.this,
                            articlesSearchAPIObjectList,
                            listOfReadArticlesUrls);
                    recyclerView.setAdapter(rvAdapterDisplaySearchArticlesTrial);
                }

                @Override
                public void onLoaderReset(Loader<List<String>> loader) {

                }
            };
}