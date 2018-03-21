package com.example.android.mynews.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.android.mynews.R;
import com.example.android.mynews.data.AndroidDatabaseManager;
import com.example.android.mynews.extras.Keys;

/**
 * Created by Diego Fajardo on 14/03/2018.
 */

public class WebViewMainActivity extends AppCompatActivity {

    //WebView
    private WebView mWebView;

    //Variable that store the urk that will be loaded by the WebView
    private String article_url;

    //Progress Bar
    private ProgressBar progressBar;

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

        //We get the url that will be displayed by the WebView and store it
        Intent intent = getIntent();
        article_url = intent.getStringExtra(Keys.PutExtras.ARTICLE_URL_SENT);

        //We load the url
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(article_url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(WebViewMainActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(WebViewMainActivity.this, MainActivity.class);
        startActivity(intent);

        super.onBackPressed();
    }


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

}
