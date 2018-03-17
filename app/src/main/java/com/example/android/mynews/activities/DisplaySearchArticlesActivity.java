package com.example.android.mynews.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.mynews.R;
import com.example.android.mynews.extras.Url;
import com.example.android.mynews.rvadapters.RvAdapterDisplaySearchArticles;

import static com.example.android.mynews.activities.MainActivity.setOverflowButtonColor;

public class DisplaySearchArticlesActivity extends AppCompatActivity {

    private static final String TAG = "DisplaySearchArticlesAc";

    //Toolbar variable
    private Toolbar toolbar;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_search_articles);

        // TODO: 17/03/2018 Create its own layout to set the title as needed
        //Sets the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Displays home button in toolbar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Changes the color of the Toolbar Overflow Button to white
        setOverflowButtonColor(toolbar, Color.WHITE);

        //Get the URLs from SearchArticlesActivity and display them
        Intent intent = getIntent();

        Log.i(TAG, intent.getExtras().getString(Url.ArticleSearchUrl.INTENT_PAGE1));
        Log.i(TAG, intent.getExtras().getString(Url.ArticleSearchUrl.INTENT_PAGE2));
        Log.i(TAG, intent.getExtras().getString(Url.ArticleSearchUrl.INTENT_PAGE3));

        // TODO: 17/03/2018 JSONRequests cannot be done at the same time (otherwise, they return an ERROR)
        // TODO: 17/03/2018 Do the last two in two different threads
        sendJSONRequest(intent.getExtras().getString(Url.ArticleSearchUrl.INTENT_PAGE1));
        //sendJSONRequest(intent.getExtras().getString(Url.ArticleSearchUrl.INTENT_PAGE2));
        //sendJSONRequest(intent.getExtras().getString(Url.ArticleSearchUrl.INTENT_PAGE3));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new RvAdapterDisplaySearchArticles(this));

        // TODO: 17/03/2018 sendJSON request should be repeated 3 times with the different Urls (PAGE_ONE, PAGE_TWO and PAGE_THREE)
        // TODO: 17/03/2018 The reason is that the SearchArticlesAPI only returns a max of 10 results at a time, what is a short quantity


    }


    /**
     * This method sends the JSON request using Volley
     * */
    public void sendJSONRequest (final String url){

        Log.i(TAG,"SENDING JSON REQUEST");

        //String request
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("SendJSONResponse OK___", url);
                        Toast.makeText(DisplaySearchArticlesActivity.this, "Response OK", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("SendJSONResponse ERROR", url);
                        Toast.makeText(DisplaySearchArticlesActivity.this, "Response ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(DisplaySearchArticlesActivity.this);

        //Adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    // TODO: 17/03/2018 Carry on implementing the JSONRequest


}
