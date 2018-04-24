package com.example.android.mynews.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;
import com.example.android.mynews.extras.interfaceswithconstants.Url;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;
import com.example.android.mynews.rvadapters.RvAdapterDisplaySearchArticles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.mynews.activities.MainActivity.setOverflowButtonColor;

public class DisplaySearchArticlesActivity extends AppCompatActivity {

    //Tag variable
    private static final String TAG = "DisplaySearchArticlesAc";

    //List that will store the JSON Response objects
    private List<ArticlesSearchAPIObject> articlesSearchAPIObjectList;

    //List to store the urls for searching the articles
    private List<String> searchArticlesListOfUrls;

    //Database Variables
    private DatabaseHelper dbH;
    private Cursor mCursor;

    //Toolbar variable
    private Toolbar toolbar;

    //RecyclerView Variables
    private RecyclerView recyclerView;
    private RvAdapterDisplaySearchArticles rvAdapterDisplaySearchArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_found_articles);

        dbH = new DatabaseHelper(this);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

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

        //Get the URLs from SearchArticlesActivity and display them
        final Intent intent = getIntent();

        Log.i(TAG, intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE1));
        Log.i(TAG, intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE2));
        Log.i(TAG, intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE3));

        //Creating the list to store the objects
        articlesSearchAPIObjectList = new ArrayList<>();

        //Creating the list and saving the articles url
        searchArticlesListOfUrls = new ArrayList<>();
        searchArticlesListOfUrls.add(intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE1));
        searchArticlesListOfUrls.add(intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE2));
        searchArticlesListOfUrls.add(intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE3));

        //The JSON request is done 3 times to get 30 articles (10 per request). It has to be done
        //leaving some time between them in order to avoid ERRORs
        sendJSONRequestsInAnotherThread(
                intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE1),
                intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE2),
                intent.getExtras().getString(Keys.PutExtras.INTENT_SA_PAGE3),
                1000);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rvAdapterDisplaySearchArticles = new RvAdapterDisplaySearchArticles(
                DisplaySearchArticlesActivity.this,
                articlesSearchAPIObjectList,
                mCursor,
                searchArticlesListOfUrls);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DisplaySearchArticlesActivity.this, SearchArticlesActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method sends the JSON request using Volley
     */
    public void sendJSONRequestToArticleSearchAPI(final String url) {

        Log.i(TAG, "SENDING JSON REQUEST");

        Log.i ("SeeTheURL", url);

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

    public void parseJSONResponse(String responseFromServer) {

        String parseTAG = "PARSEtag";

        if (responseFromServer == null || responseFromServer.length() == 0) return;

        try {

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObjectResponseFromServer = new JSONObject(responseFromServer);
            Log.i(parseTAG, "jsonObject_response READ");

            //There is a property of the object that is called response that doesnt' have to do
            //with the API response itself. We get it here.
            JSONObject responsePropertyObject =
                    jsonObjectResponseFromServer.getJSONObject(Keys.SearchArticles.KEY_RESPONSE);
            Log.i(parseTAG, "responseProperyObject READ");

            //JSON array made of the objects inside the "response" object
            JSONArray docs_array =
                    responsePropertyObject.getJSONArray(Keys.SearchArticles.KEY_DOCS);
            Log.i(parseTAG, "docs_array READ");
            Log.i(parseTAG, docs_array.length() + "");

            //Iterating through "docs_array"
            for (int i = 0; i < docs_array.length(); i++) {

                //We create the object that is going to store all the information
                ArticlesSearchAPIObject articlesSearchAPIObject = new ArticlesSearchAPIObject();

                //We GET the "i results" object
                JSONObject docsObject = docs_array.getJSONObject(i);

                //We GET the data from the dataObject
                if (docsObject.getString(Keys.SearchArticles.KEY_WEB_URL) != null) {
                    articlesSearchAPIObject.setWebUrl(docsObject.getString(Keys.SearchArticles.KEY_WEB_URL));
                    Log.i("WEB_URL", articlesSearchAPIObject.getWebUrl());
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_SNIPPET) != null) {
                    articlesSearchAPIObject.setSnippet(docsObject.getString(Keys.SearchArticles.KEY_SNIPPET));
                    Log.i("SNIPPET", articlesSearchAPIObject.getSnippet());
                }

                JSONArray multimedia_array = docsObject.getJSONArray(Keys.SearchArticles.KEY_MULTIMEDIA);

                if (multimedia_array.length() > 0) {
                    JSONObject multimedia_object = multimedia_array.getJSONObject(2);

                    if (multimedia_object.getString(Keys.SearchArticles.KEY_IMAGE_URL) != null) {
                        articlesSearchAPIObject.setImageUrl(Url.ArticleSearchUrl.IMAGE_URL_BASE + multimedia_object.getString(Keys.SearchArticles.KEY_IMAGE_URL));
                        Log.i("IMAGE_URL", articlesSearchAPIObject.getImageUrl());
                    }
                } else {
                    articlesSearchAPIObject.setImageUrl("");
                    Log.i("IMAGE_URL", "ARRAY.size() = 0");
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_PUB_DATE) != null) {
                    String pub_date = docsObject.getString(Keys.SearchArticles.KEY_PUB_DATE);
                    pub_date.substring(0,10);
                    String day = pub_date.substring(8,10);
                    String month = pub_date.substring(5,7);
                    String year = pub_date.substring(0,4);
                    pub_date = day + "/" + month + "/" + year;
                    articlesSearchAPIObject.setPubDate(pub_date);
                    Log.i("PUB_DATE", articlesSearchAPIObject.getPubDate());
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK) != null) {
                    if (docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK).equals("None")) {
                        articlesSearchAPIObject.setNewDesk("General");
                    }
                    else { articlesSearchAPIObject.setNewDesk(docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK)); }
                    Log.i("NEW_DESK", articlesSearchAPIObject.getNewDesk());
                }

                //We put the object with the results into the ArrayList articlesSearchAPIObjectList;
                articlesSearchAPIObjectList.add(articlesSearchAPIObject);
                Log.i("SAposition # ", "" + i + " :" + articlesSearchAPIObjectList.get(i).getSnippet());

            }

            for (int i = 0; i < articlesSearchAPIObjectList.size(); i++) {
                Log.i("SAposition # ", "" + i + " :" + articlesSearchAPIObjectList.get(i).getSnippet());
            }

            Log.i("SA_ArrayList.size()", "" + articlesSearchAPIObjectList.size());

            if (articlesSearchAPIObjectList != null) {
                recyclerView.setAdapter(new RvAdapterDisplaySearchArticles(this,
                        articlesSearchAPIObjectList,
                        mCursor,
                        searchArticlesListOfUrls));
                Log.i("setDispSearchArtData:", "Called(size = " + articlesSearchAPIObjectList.size() + ")");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send the JSON Request to the API. It makes the calls one after another leaving some space between
     * them in order to avoid ERRORs.
     * */
    private void sendJSONRequestsInAnotherThread(final String url, final String url2, final String url3, final int timeInMillis) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //First request
                    sendJSONRequestToArticleSearchAPI(url);
                    Log.i("Request 1", "Start Sleeping");
                    Thread.sleep(timeInMillis);
                    Log.i("Request 1", "Stop Sleeping");

                    //Second request
                    sendJSONRequestToArticleSearchAPI(url2);
                    Log.i("Request 2", "2 Start Sleeping");
                    Thread.sleep(timeInMillis);
                    Log.i("Request 2 ", "Stop Sleeping");

                    //Third request
                    sendJSONRequestToArticleSearchAPI(url3);
                    Log.i("Request 3", "2 Start Sleeping");
                    Thread.sleep(timeInMillis);
                    Log.i("Request 3", "Stop Sleeping");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}