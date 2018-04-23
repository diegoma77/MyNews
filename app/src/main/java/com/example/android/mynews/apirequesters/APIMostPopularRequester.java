package com.example.android.mynews.apirequesters;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.helperclasses.ToastHelper;
import com.example.android.mynews.pojo.MostPopularAPIObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 23/04/2018.
 */

public class APIMostPopularRequester {

    // TODO: 23/04/2018 Do this class

    //Context
    Context context;

    //This variable stores the list of urls to do the requests
    private List<String> listOfUrls;

    //This variable stores the objects with all the needed information from
    //the request
    private List<MostPopularAPIObject> listOfMostPopularObjects;

    /** Constructor **/
    public APIMostPopularRequester (Context context) {
        this.context = context;
        this.listOfUrls = new ArrayList<>();
        this.listOfMostPopularObjects = new ArrayList<>();
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
     * of MostPopularAPI Objects*/
    public List<MostPopularAPIObject> getListOfMostPopularObjects() {
        if (listOfMostPopularObjects != null){ return listOfMostPopularObjects;
        } else { return null; }
    }


    /************************************
     * METHODS TO DO THE API REQUEST ****
     ************************************/

    public void startJSONRequestMostPopularAPI(
            String url) {

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
                        MostPopularAPIParseJSONResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //In case of error of in the Request,//we get a Toast
                        ToastHelper.toastShort(context, error.getMessage());
                    }
                }
        );

        return stringRequest;

    }

    private void MostPopularAPIParseJSONResponse(String response) {

        if (response == null || response.length() == 0) {
            return;
        }

        try {

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObject_response = new JSONObject(response);

            //JSON array made of the objects inside the "result"
            JSONArray results_array =
                    jsonObject_response.getJSONArray(Keys.MostPopularKeys.KEY_RESULTS);

            //Iterating through "results_array"
            for (int i = 0; i < results_array.length(); i++) {

                //We create the object that is going to store all the information
                MostPopularAPIObject mostPopularAPIObject = new MostPopularAPIObject();

                //We get the "i results object"
                JSONObject resultsObject = results_array.getJSONObject(i);

                //GETS the rest of the data from the dataObject
                if (resultsObject.getString(Keys.MostPopularKeys.KEY_SECTION) != null) {
                    mostPopularAPIObject.setSection(resultsObject.getString(Keys.MostPopularKeys.KEY_SECTION));
                    Log.i("SECTION", mostPopularAPIObject.getSection());
                }

                if (resultsObject.getString(Keys.MostPopularKeys.KEY_TITLE) != null) {
                    mostPopularAPIObject.setTitle(resultsObject.getString(Keys.MostPopularKeys.KEY_TITLE));
                    Log.i("TITLE", mostPopularAPIObject.getTitle());
                }

                if (resultsObject.getString(Keys.MostPopularKeys.KEY_ARTICLE_URL) != null) {
                    mostPopularAPIObject.setArticle_url(resultsObject.getString(Keys.MostPopularKeys.KEY_ARTICLE_URL));
                    Log.i("ARTICLE_URL", mostPopularAPIObject.getArticleUrl());
                }

                if (resultsObject.getString(Keys.MostPopularKeys.KEY_PUBLISHED_DATE) != null) {
                    String published_date = resultsObject.getString(Keys.MostPopularKeys.KEY_PUBLISHED_DATE);
                    published_date.substring(0,10);
                    String day = published_date.substring(8,10);
                    String month = published_date.substring(5,7);
                    String year = published_date.substring(0,4);
                    published_date = day + "/" + month + "/" + year;
                    mostPopularAPIObject.setPublishedDate(published_date);
                    Log.i("PUBLISHED DATE", mostPopularAPIObject.getPublishedDate());
                }

                JSONArray media_array = resultsObject.getJSONArray(Keys.MostPopularKeys.KEY_MEDIA);

                JSONObject media_Object0 = media_array.getJSONObject(0);

                JSONArray media_metadata = media_Object0.getJSONArray(Keys.MostPopularKeys.KEY_MEDIA_METADATA);

                JSONObject media_metadata0 = media_metadata.getJSONObject(0);

                if (media_metadata0.getString(Keys.MostPopularKeys.KEY_IMAGE_URL) != null){
                    mostPopularAPIObject.setImage_thumbnail(media_metadata0.getString(Keys.MostPopularKeys.KEY_IMAGE_URL));
                }

                //We fill the list with the object
                listOfMostPopularObjects.add(mostPopularAPIObject);
                Log.i("MPposition # ", "" + i + " :" + listOfMostPopularObjects.get(i).getTitle());

            }

            //Loop for logs
            for (int i = 0; i < listOfMostPopularObjects.size() ; i++) {
                Log.i("MPposition # ", "" + i + " :" + listOfMostPopularObjects.get(i).getTitle());
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }

    }
}
