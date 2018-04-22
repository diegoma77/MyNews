package com.example.android.mynews.apirequesters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.helperclasses.ToastHelper;
import com.example.android.mynews.pojo.TopStoriesAPIObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/04/2018.
 */

public class APITopStoriesRequester {

    // TODO: 23/04/2018 Can be removed

    Context context;

    //This variable stores the list of urls to do the requests
    private List<String> listOfUrls;

    //This variable stores the objects with all the needed information from
    //the request
    private List<TopStoriesAPIObject> listOfTopStoriesObjects;

    /** Constructor **/
    public APITopStoriesRequester (Context context) {
        this.context = context;
        this.listOfUrls = new ArrayList<>();
        this.listOfTopStoriesObjects = new ArrayList<>();
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
    public List<TopStoriesAPIObject> getListOfTopStoriesObjects() {
        if (listOfTopStoriesObjects != null){ return listOfTopStoriesObjects;
        } else { return null; }
    }


    /************************************
     * METHODS TO DO THE API REQUEST ****
     ************************************/

    public void startJSONRequestTopStoriesAPI(
            String url,
            final Context context) {

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
                        TopStoriesAPIParseJSONResponse(response);

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

    private void TopStoriesAPIParseJSONResponse(String response) {

        if (response == null || response.length() == 0) {
            return;
        }

        try {

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObject_response = new JSONObject(response);

            //JSON array made of the objects inside the "result"
            JSONArray results_array =
                    jsonObject_response.getJSONArray(Keys.TopStoriesKeys.KEY_RESULTS);

            //Iterating through "results_array"
            for (int i = 0; i < results_array.length(); i++) {

                //We create the object that is going to store all the information
                TopStoriesAPIObject topStoriesObject = new TopStoriesAPIObject();

                //We get the "i results object"
                JSONObject dataObject = results_array.getJSONObject(i);

                //We get the multimedia array from the "i results object".
                JSONArray multimedia_array = dataObject.getJSONArray(Keys.TopStoriesKeys.KEY_MULTIMEDIA);

                Log.i ("MULTIMEDIA_LENGTH", "" + multimedia_array.length());

                JSONObject multimedia_object = multimedia_array.getJSONObject(1);

                if (multimedia_object.getString(Keys.TopStoriesAPIKeys.KEY_IMAGE_URL) != null) {
                    topStoriesObject.setImageThumblarge(
                            multimedia_object.getString(Keys.TopStoriesAPIKeys.KEY_IMAGE_URL));
                    Log.i("IMAGE_URL_THUMBNAIL", topStoriesObject.getImageThumblarge());
                }

                //CHECKS that the data from the JSON objects is not null
                //If its not null, it sets the property of the object with the value
                if (dataObject.getString(Keys.TopStoriesAPIKeys.KEY_SECTION) != null) {
                    topStoriesObject.setSection(dataObject.getString(Keys.TopStoriesAPIKeys.KEY_SECTION));
                    Log.i("SECTION", topStoriesObject.getSection());
                }

                if (dataObject.getString(Keys.TopStoriesAPIKeys.KEY_SUBSECTION) != null) {
                    topStoriesObject.setSubsection(dataObject.getString(Keys.TopStoriesAPIKeys.KEY_SUBSECTION));
                    Log.i("SECTION", topStoriesObject.getSubsection());
                }

                if (dataObject.getString(Keys.TopStoriesAPIKeys.KEY_TITLE) != null) {
                    topStoriesObject.setTitle(dataObject.getString(Keys.TopStoriesAPIKeys.KEY_TITLE));
                    Log.i("TITLE", topStoriesObject.getTitle());
                }

                if (dataObject.getString(Keys.TopStoriesAPIKeys.KEY_ARTICLE_URL) != null) {
                    topStoriesObject.setArticleUrl(dataObject.getString(Keys.TopStoriesAPIKeys.KEY_ARTICLE_URL));
                    Log.i("ARTICLE_URL", topStoriesObject.getArticleUrl());
                }

                if (dataObject.getString(Keys.TopStoriesAPIKeys.KEY_UPDATED_DATE) != null) {
                    String updated_date = dataObject.getString(Keys.TopStoriesAPIKeys.KEY_UPDATED_DATE);
                    updated_date.substring(0,10);
                    String day = updated_date.substring(8,10);
                    String month = updated_date.substring(5,7);
                    String year = updated_date.substring(0,4);
                    updated_date = day + "/" + month + "/" + year;
                    topStoriesObject.setUpdatedDate(updated_date);
                    Log.i("UPDATE_DATE", topStoriesObject.getUpdatedDate());
                }

                listOfTopStoriesObjects.add(topStoriesObject);
                Log.i("TS_ARRAYLIST # ", "" + i + ", " + listOfTopStoriesObjects.get(i).getTitle());

            }

            //Loop to see that all objects in the ArrayList are different
            for (int i = 0; i < listOfTopStoriesObjects.size() ; i++) {
                Log.i("TS_ARRAY_SUMM_TITLES # ", "" + i + " :" + listOfTopStoriesObjects.get(i).getTitle());
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }

    }





}
