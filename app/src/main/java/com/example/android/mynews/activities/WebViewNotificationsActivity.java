package com.example.android.mynews.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.android.mynews.R;
import com.example.android.mynews.asynctaskloaders.atldatabase.ATLInsertReadArticleInDatabase;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;

import java.util.List;

/**
 * Created by Diego Fajardo on 14/03/2018.
 */

/** Activity that displays the articles related to Notifications Activity
 * that have been displayed in a RecyclerView in DisplayNotificationsActivity
 * It uses a WebView. It also sets the articles as a Read Article (inserts
 * the url in the database)
 * */
public class WebViewNotificationsActivity extends AppCompatActivity {

    private static final String TAG = "WebViewSearchActivityTr";

    //Loader ID
    private static final int LOADER_INSERT_URL_DATABASE = 21;

    //WebView
    private WebView mWebView;

    //Variable that store the url that will be loaded by the WebView
    private String article_url;

    //Progress Bar
    private ProgressBar progressBar;

    //Intent variable
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_url_loader);

        Toolbar toolbar = (Toolbar) findViewById(R.id.webView_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //We get the progress bar
        progressBar = (ProgressBar) findViewById(R.id.webView_progressBar);

        //We insert the article in the database if needed (first we get the information from
        //the intent)
        intent = getIntent();
        article_url = intent.getStringExtra(Keys.PutExtras.ARTICLE_URL_SENT);

        //We insert the article in the database if needed
        loadLoaderInsertArticleUrlInDatabase(LOADER_INSERT_URL_DATABASE);

        //We prepare to load the url
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        //We load the url
        mWebView.loadUrl(article_url);

    }

    /*********************
     * MENU OPTIONS ******
     ********************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                startActivity(new Intent(WebViewNotificationsActivity.this, DisplayNotificationsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(WebViewNotificationsActivity.this, DisplaySearchArticlesActivity.class));
        super.onBackPressed();
    }

    /**
     * INNER CLASS used to
     * load the website
     */
    public class MyWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    /**************************
     *** LOADERS **************
     **************************/

    /** Used to insert the article url
     * in the database if needed
     * */
    private void loadLoaderInsertArticleUrlInDatabase(int id) {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<String>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: ");
            loaderManager.initLoader(id, null, loaderInsertUrlInDatabase);
        } else {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: ");
            loaderManager.restartLoader(id, null, loaderInsertUrlInDatabase);
        }
    }


    /**************************
     *** LOADER CALLBACKS *****
     **************************/

    /** Used to insert the article url
     * in the database if needed
     * */
    private LoaderManager.LoaderCallbacks<String> loaderInsertUrlInDatabase =
            new LoaderManager.LoaderCallbacks<String>() {

                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    Log.i(TAG, "onCreateLoader: called! +++");
                    return new ATLInsertReadArticleInDatabase(getApplicationContext(), article_url);
                }

                @Override
                public void onLoadFinished(Loader<String> loader, String data) {

                }

                @Override
                public void onLoaderReset(Loader<String> loader) {

                }
            };
}
