package com.example.android.mynews.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_url_loader);

        Toolbar toolbar = (Toolbar) findViewById(R.id.webView_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //We get the url that will be displayed by the WebView and store it
        Intent intent = getIntent();
        article_url = intent.getStringExtra(Keys.PutExtras.ARTICLE_URL_SENT);

        //We load the url
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        mWebView.loadUrl(article_url);
        mWebView.setWebViewClient(new WebViewClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

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
}
