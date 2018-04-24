package com.example.android.mynews.apirequesters;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.mynews.extras.helperclasses.ToastHelper;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;
import com.example.android.mynews.extras.interfaceswithconstants.Url;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 24/04/2018.
 */

public class APISearchArticlesRequester {

    //Context
    Context context;

    //This variable stores the list of urls to do the requests
    private List<String> listOfUrls;

    //This variable stores the objects with all the needed information from
    //the request
    private List<ArticlesSearchAPIObject> listOfArticlesSearchObjects;

    /** Constructor **/
    public APISearchArticlesRequester (Context context) {
        this.context = context;
        this.listOfUrls = new ArrayList<>();
        this.listOfArticlesSearchObjects = new ArrayList<>();
    }

    /** Method to add urls
     * to the listOfUrls list**/
    public void addUrl (String url) {
        listOfUrls.add(url);
    }

    /** Method to get a url
     * from the list of urls*/
    public String getUrl (int position) {
        return listOfUrls.get(position);
    }

    /** Method to get the size
     * of listOfUrls*/
    public int getlistOfUrlsSize () {
        return listOfUrls.size();
    }

    /** Method to get the list
     * of TopStoriesAPI Objects*/
    public List<ArticlesSearchAPIObject> getListOfArticlesSearchObjects() {
        if (listOfArticlesSearchObjects != null){ return listOfArticlesSearchObjects;
        } else { return null; }
    }


    /************************************
     * METHODS TO DO THE API REQUEST ****
     ************************************/

    public void startJSONRequestArticlesSearchAPI(String url) {

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding the string request to request queue
        requestQueue.add(createStringRequest(url, context));

    }


    private StringRequest createStringRequest(String url, final Context context) {

        //String request
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArticlesSearchAPIParseJSONResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //In case of error of in the Request,//we get a Toast
                        //ToastHelper.toastShort(context, error.getMessage());
                    }
                }
        );

        return stringRequest;

    }

    private void ArticlesSearchAPIParseJSONResponse(String response) {

        if (response == null || response.length() == 0) {
            return;
        }

        /** If the JSON Request is done to ARTICLE SEARCH API,
         * then this code will run*/
        try {

            String parseTAG = "PARSEtag";

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObjectResponseFromServer = new JSONObject(response);
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
                    pub_date.substring(0, 10);
                    String day = pub_date.substring(8, 10);
                    String month = pub_date.substring(5, 7);
                    String year = pub_date.substring(0, 4);
                    pub_date = day + "/" + month + "/" + year;
                    articlesSearchAPIObject.setPubDate(pub_date);
                    Log.i("PUB_DATE", articlesSearchAPIObject.getPubDate());
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK) != null) {
                    if (docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK).equals("None")) {
                        articlesSearchAPIObject.setNewDesk("General");
                    } else {
                        articlesSearchAPIObject.setNewDesk(docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK));
                    }
                    Log.i("NEW_DESK", articlesSearchAPIObject.getNewDesk());
                }

                //We fill the list with the object
                listOfArticlesSearchObjects.add(articlesSearchAPIObject);
                Log.i("position # ", "" + i + " :" + listOfArticlesSearchObjects.get(i).getSnippet());

            }

            //Loop for logs
            for (int i = 0; i < listOfArticlesSearchObjects.size() ; i++) {
                Log.i("TS_ARRAY_SUMM_TITLES # ", "" + i + " :" + listOfArticlesSearchObjects.get(i).getNewDesk());
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }

    }
}
