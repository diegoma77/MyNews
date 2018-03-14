package com.example.android.mynews.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.mynews.R;
import com.example.android.mynews.extras.Keys;

/**
 * Created by Diego Fajardo on 14/03/2018.
 */

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    private String article_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_url_loader);

        Intent intent = getIntent();
        article_url = intent.getStringExtra(Keys.PutExtras.ARTICLE_URL_SENT);

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

}
