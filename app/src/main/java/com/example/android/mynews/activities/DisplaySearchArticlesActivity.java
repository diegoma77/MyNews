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
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.Url;
import com.example.android.mynews.pojo.MostPopularObject;
import com.example.android.mynews.pojo.SearchArticlesObject;
import com.example.android.mynews.rvadapters.RvAdapterDisplaySearchArticles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.mynews.activities.MainActivity.setOverflowButtonColor;

public class DisplaySearchArticlesActivity extends AppCompatActivity {

    private static final String TAG = "DisplaySearchArticlesAc";

    private List<SearchArticlesObject> searchArticlesObjectList;

    //Toolbar variable
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private RvAdapterDisplaySearchArticles rvAdapterDisplaySearchArticles;

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

        Log.i(TAG, intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE1));
        Log.i(TAG, intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE2));
        Log.i(TAG, intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE3));

        searchArticlesObjectList = new ArrayList<>();

        // TODO: 17/03/2018 JSONRequests cannot be done at the same time (otherwise, they return an ERROR)
        // TODO: 17/03/2018 Do the last two in two different threads
        sendJSONRequest(intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE1));
        //sendJSONRequest(intent.getExtras().getString(Url.ArticleSearchUrl.INTENT_SA_PAGE2));
        //sendJSONRequest(intent.getExtras().getString(Url.ArticleSearchUrl.INTENT_SA_PAGE3));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rvAdapterDisplaySearchArticles = new RvAdapterDisplaySearchArticles(DisplaySearchArticlesActivity.this, searchArticlesObjectList);


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
                        parseJSONResponse(response);

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

    public void parseJSONResponse (String response) {

        if (response == null || response.length() == 0) return;

        // TODO: 13/03/2018 Add if statements to check if the data was received or not and avoid crashes

        try {

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObject_response = new JSONObject(response);

            //JSON array made of the objects inside the "result"
            JSONArray docs_array =
                    jsonObject_response.getJSONArray(Keys.SearchArticles.KEY_DOCS);

            //Iterating through "docs_array"
            for (int i = 0; i < docs_array.length(); i++) {

                //We create the object that is going to store all the information
                SearchArticlesObject searchArticlesObject = new SearchArticlesObject();

                // TODO: 13/03/2018 We have yet to decide what image to get

                //We GET the "i results" object
                JSONObject dataObject = docs_array.getJSONObject(i);

                //We GET the data from the dataObject
                if (dataObject.getString(Keys.SearchArticles.KEY_WEB_URL) != null) {
                    searchArticlesObject.setWeb_url(dataObject.getString(Keys.SearchArticles.KEY_WEB_URL));
                    Log.i("WEB_URL", searchArticlesObject.getWeb_url());
                }

                if (dataObject.getString(Keys.SearchArticles.KEY_SNIPPET) != null) {
                    searchArticlesObject.setSnippet(dataObject.getString(Keys.SearchArticles.KEY_SNIPPET));
                    Log.i("SNIPPET", searchArticlesObject.getSnippet());
                }

                JSONArray multimedia_array = dataObject.getJSONArray(Keys.SearchArticles.KEY_MULTIMEDIA);

                JSONObject multimedia_object = multimedia_array.getJSONObject(0);

                if (multimedia_object.getString(Keys.SearchArticles.KEY_IMAGE_URL) != null) {
                    searchArticlesObject.setImage_url(multimedia_object.getString(Keys.SearchArticles.KEY_IMAGE_URL));
                    Log.i("IMAGE_URL", searchArticlesObject.getImage_url());
                }

                if (dataObject.getString(Keys.SearchArticles.KEY_PUB_DATE) != null) {
                    searchArticlesObject.setPub_date(dataObject.getString(Keys.SearchArticles.KEY_PUB_DATE));
                    Log.i("PUB_DATE", searchArticlesObject.getPub_date());
                }

                if (dataObject.getString(Keys.SearchArticles.KEY_NEW_DESK) != null) {
                    searchArticlesObject.setNew_desk(dataObject.getString(Keys.SearchArticles.KEY_NEW_DESK));
                    Log.i("NEW_DESK", searchArticlesObject.getNew_desk());
                }

                //We put the object with the results into the ArrayList searchArticlesObjectList;
                searchArticlesObjectList.add(searchArticlesObject);
                Log.i("SAposition # ", "" + i + " :" + searchArticlesObjectList.get(i).getSnippet());

            }

        for (int i = 0; i < searchArticlesObjectList.size() ; i++) {
            Log.i("SAposition # ", "" + i + " :" + searchArticlesObjectList.get(i).getSnippet());
        }

        Log.i("SA_ArrayList.size():", "" + searchArticlesObjectList.size());

        if (searchArticlesObjectList != null) {
            recyclerView.setAdapter(new RvAdapterDisplaySearchArticles(this, searchArticlesObjectList));
            Log.i("setDispSearchArtData:", "Called(size = " + searchArticlesObjectList.size() + ")");

        }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

}
