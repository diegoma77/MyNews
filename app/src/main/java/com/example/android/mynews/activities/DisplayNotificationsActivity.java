package com.example.android.mynews.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.helperclasses.DateHelper;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.Url;
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

public class DisplayNotificationsActivity extends AppCompatActivity {

    //Tag variable
    private static final String TAG = "DisplaySearchArticlesAc";

    //List that will store the JSON Response objects
    private List<ArticlesSearchAPIObject> notificationsArticlesObjectList;

    //List to store the urls for searching the articles
    private List<String> notificationsArticlesListOfUrls;

    //Database Variables
    private DatabaseHelper dbH;
    private Cursor mCursor;

    //DateHelper
    private DateHelper dH;

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
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);

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

        //Creating the list to store the objects
        notificationsArticlesObjectList = new ArrayList<>();

       /** Getting the dates for the urls.
        * Today and 7 days ago
        * */
        //Today
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date today = Calendar.getInstance().getTime();
        String todayDate = df.format(today);
        Log.i ("DATE", todayDate);

        //7 days ago
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        Date sevenDaysAgo = c.getTime();
        String sevenDaysAgoDate = df.format(sevenDaysAgo);
        Log.i ("DATE", sevenDaysAgoDate);

        //Creating the list and saving the articles url
        notificationsArticlesListOfUrls = new ArrayList<>();
        String url_page1 = getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getSectionsAndAdaptForUrl(),
                DateHelper.getOneMonthAgoDateAndConvertToString(),
                DateHelper.getTodayDateAndConvertToString(),
                "1");

        String url_page2 = getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getSectionsAndAdaptForUrl(),
                DateHelper.getOneMonthAgoDateAndConvertToString(),
                DateHelper.getTodayDateAndConvertToString()
                , "2");

        String url_page3 = getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getSectionsAndAdaptForUrl(),
                DateHelper.getOneMonthAgoDateAndConvertToString(),
                DateHelper.getTodayDateAndConvertToString() ,
                "3");

        Log.i("url1", url_page1);
        Log.i("url2", url_page2);
        Log.i("url3", url_page3);

        notificationsArticlesListOfUrls.add(url_page1);
        notificationsArticlesListOfUrls.add(url_page2);
        notificationsArticlesListOfUrls.add(url_page3);

        //The JSON request is done 3 times to get 30 articles (10 per request). It has to be done
        //leaving some time between them in order to avoid ERRORs
        sendJSONRequestsInAnotherThread(
                url_page1,
                url_page2,
                url_page3,
                1000);

        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rvAdapterDisplaySearchArticles = new RvAdapterDisplaySearchArticles(
                DisplayNotificationsActivity.this,
                notificationsArticlesObjectList,
                mCursor,
                notificationsArticlesListOfUrls);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DisplayNotificationsActivity.this, MainActivity.class);
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
                        Toast.makeText(DisplayNotificationsActivity.this, "Response OK", Toast.LENGTH_SHORT).show();
                        parseJSONResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("SendJSONResponse ERROR", url);
                        Toast.makeText(DisplayNotificationsActivity.this, "Response ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(DisplayNotificationsActivity.this);

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
                    articlesSearchAPIObject.setWeb_url(docsObject.getString(Keys.SearchArticles.KEY_WEB_URL));
                    Log.i("WEB_URL", articlesSearchAPIObject.getWeb_url());
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_SNIPPET) != null) {
                    articlesSearchAPIObject.setSnippet(docsObject.getString(Keys.SearchArticles.KEY_SNIPPET));
                    Log.i("SNIPPET", articlesSearchAPIObject.getSnippet());
                }

                JSONArray multimedia_array = docsObject.getJSONArray(Keys.SearchArticles.KEY_MULTIMEDIA);

                if (multimedia_array.length() > 0) {
                    JSONObject multimedia_object = multimedia_array.getJSONObject(2);

                    if (multimedia_object.getString(Keys.SearchArticles.KEY_IMAGE_URL) != null) {
                        articlesSearchAPIObject.setImage_url(Url.ArticleSearchUrl.IMAGE_URL_BASE + multimedia_object.getString(Keys.SearchArticles.KEY_IMAGE_URL));
                        Log.i("IMAGE_URL", articlesSearchAPIObject.getImage_url());
                    }
                } else {
                    articlesSearchAPIObject.setImage_url("");
                    Log.i("IMAGE_URL", "ARRAY.size() = 0");
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_PUB_DATE) != null) {
                    String pub_date = docsObject.getString(Keys.SearchArticles.KEY_PUB_DATE);
                    pub_date.substring(0,10);
                    String day = pub_date.substring(8,10);
                    String month = pub_date.substring(5,7);
                    String year = pub_date.substring(0,4);
                    pub_date = day + "/" + month + "/" + year;
                    articlesSearchAPIObject.setPub_date(pub_date);
                    Log.i("PUB_DATE", articlesSearchAPIObject.getPub_date());
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK) != null) {
                    if (docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK).equals("None")) {
                        articlesSearchAPIObject.setNew_desk("General");
                    }
                    else { articlesSearchAPIObject.setNew_desk(docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK)); }
                    Log.i("NEW_DESK", articlesSearchAPIObject.getNew_desk());
                }

                //We put the object with the results into the ArrayList notificationsArticlesObjectList;
                notificationsArticlesObjectList.add(articlesSearchAPIObject);
                Log.i("NAposition # ", "" + i + " :" + notificationsArticlesObjectList.get(i).getSnippet());

            }

            for (int i = 0; i < notificationsArticlesObjectList.size(); i++) {
                Log.i("NAposition # ", "" + i + " :" + notificationsArticlesObjectList.get(i).getSnippet());
            }

            Log.i("NA_ArrayList.size()", "" + notificationsArticlesObjectList.size());

            mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

            if (notificationsArticlesObjectList != null) {
                recyclerView.setAdapter(new RvAdapterDisplaySearchArticles(this,
                        notificationsArticlesObjectList,
                        mCursor,
                        notificationsArticlesListOfUrls));
                Log.i("setDispNotArtData:", "Called(size = " + notificationsArticlesObjectList.size() + ")");

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

    /**
     * This method builds the Url used to send the JSON request
     * using the strings created (modified) by other methods
     * */
    public String getSearchArticlesUrl (String searchQuery, String sectionsQuery, String beginDate, String endDate, String page) {

        /** Seems that there are faster ways than += to append Strings, like StringBuffer */

        String searchArticleUrl = Url.ArticleSearchUrl.BASE_URL;

        if (!searchQuery.equals("")) {
            searchArticleUrl += Url.ArticleSearchUrl.Q
                    + Url.GeneralTokens.EQUAL
                    + searchQuery
                    + Url.GeneralTokens.AMPERSAND;
        }

        if (!sectionsQuery.equals("")) {
            searchArticleUrl += Url.ArticleSearchUrl.FQ
                    + Url.GeneralTokens.EQUAL
                    + sectionsQuery
                    + Url.GeneralTokens.AMPERSAND;
        }

        if (!beginDate.equals("")) {
            searchArticleUrl += Url.ArticleSearchUrl.BEGIN_DATE
                    + Url.GeneralTokens.EQUAL
                    + beginDate
                    + Url.GeneralTokens.AMPERSAND;
        }

        if (!endDate.equals("")) {
            searchArticleUrl += Url.ArticleSearchUrl.END_DATE
                    + Url.GeneralTokens.EQUAL
                    + endDate
                    + Url.GeneralTokens.AMPERSAND;
        }

        searchArticleUrl += Url.ArticleSearchUrl.SORT_NEWEST + Url.GeneralTokens.AMPERSAND
                + Url.ArticleSearchUrl.PAGE + Url.GeneralTokens.EQUAL + page + Url.GeneralTokens.AMPERSAND
                + Url.ArticleSearchUrl.API_KEY;

        return searchArticleUrl;

    }

    /**
     * This method is used when the SEARCH BUTTON is clicked.
     * It starts the process of searching for the articles according to the information
     * needed from the user.
     * Changes spaces for + symbols.
     * */
    private String getSearchQueryAndAdaptForUrl () {

        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);
        mCursor.moveToFirst();

        String searchQueryAdaptedForUrl = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION));

        if (!searchQueryAdaptedForUrl.equals("")) {
            searchQueryAdaptedForUrl = searchQueryAdaptedForUrl.trim();
            searchQueryAdaptedForUrl = searchQueryAdaptedForUrl.toLowerCase().replace(" ", "+");
        }

        return searchQueryAdaptedForUrl;
    }

    /**
     * This method us used to build the news_desk part of the Url.
     * The news_desk part is related to the checkboxes and allows to filter the
     * searches according to the category
     * */
    private String getSectionsAndAdaptForUrl() {

        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);
        mCursor.moveToFirst();
        mCursor.moveToNext();

        String temporaryQuery;
        String sectionsQuery = "";

        for (int i = 0; i < mCursor.getCount() - 1; i++) {
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION)).equals("")) {
                mCursor.moveToNext();
            }
            else {
                temporaryQuery = "+" + mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION));
                sectionsQuery += temporaryQuery;
                mCursor.moveToNext();
            }
        }
        return sectionsQuery.substring(1);
    }

}