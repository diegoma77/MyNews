package com.example.android.mynews.broadcastreceiver;

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
import com.example.android.mynews.extras.Url;
import com.example.android.mynews.pojo.ArticlesAPIObject;
import com.example.android.mynews.pojo.MostPopularAPIObject;
import com.example.android.mynews.pojo.TopStoriesAPIObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 12/04/2018.
 */

public class ApiFetcher {

    private final String REFERENCE_TO_OBJECT;

    private List<String> listOfUrls;
    private List<ArticlesAPIObject> listOfArticlesObjects;
    private List<MostPopularAPIObject> listOfMostPopularObjects;
    private List<TopStoriesAPIObject> listOfTopStoriesObjects;

    public ApiFetcher(String referenceToObject) {
        REFERENCE_TO_OBJECT = referenceToObject;
        listOfUrls = new ArrayList<>();

        switch (REFERENCE_TO_OBJECT) {
            case Keys.ApiFetcher.ARTICLES_API_REFERENCE: {
                listOfArticlesObjects = new ArrayList<>();
                break;
            }
            case Keys.ApiFetcher.MOSTPOPULAR_API_REFERENCE: {
                listOfMostPopularObjects = new ArrayList<>();
                break;
            }
            case Keys.ApiFetcher.TOPSTORIES_API_REFERENCE: {
                listOfTopStoriesObjects = new ArrayList<>();
                break;
            }
        }

    }

    public void addUrl (String url) {
        listOfUrls.add(url);
    }

    public String getUrl (int position) {
        return listOfUrls.get(position);
    }

    public List<ArticlesAPIObject> getListOfArticlesObjects() {
        if (REFERENCE_TO_OBJECT.equals(Keys.ApiFetcher.ARTICLES_API_REFERENCE)) {
            return listOfArticlesObjects;
        }
        else return null;
    }

    public List<MostPopularAPIObject> getListOfMostPopularObjects() {
        if (REFERENCE_TO_OBJECT.equals(Keys.ApiFetcher.MOSTPOPULAR_API_REFERENCE)) {
            return listOfMostPopularObjects;
        }
        else return null;
    }

    public List<TopStoriesAPIObject> getListOfTopStoriesObjects() {
        if (REFERENCE_TO_OBJECT.equals(Keys.ApiFetcher.TOPSTORIES_API_REFERENCE)) {
            return listOfTopStoriesObjects;
        }
        else return null;
    }

    public void startJSONRequestArticlesAPI(
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
                        if (REFERENCE_TO_OBJECT.equals(Keys.ApiFetcher.ARTICLES_API_REFERENCE)) {
                            ArticlesAPIParseJSONResponse(response);
                        } else if (REFERENCE_TO_OBJECT.equals(Keys.ApiFetcher.MOSTPOPULAR_API_REFERENCE)){
                            MostPopularAPIParseJSONResponse(response);
                        } else if (REFERENCE_TO_OBJECT.equals(Keys.ApiFetcher.TOPSTORIES_API_REFERENCE)){
                            TopStoriesAPIParseJSONResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                        showErrorMessage(context);
                    }
                }
        );

        return stringRequest;

    }

    private void ArticlesAPIParseJSONResponse(String response) {

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
                ArticlesAPIObject articlesAPIObject = new ArticlesAPIObject();

                //We GET the "i results" object
                JSONObject docsObject = docs_array.getJSONObject(i);

                //We GET the data from the dataObject
                if (docsObject.getString(Keys.SearchArticles.KEY_WEB_URL) != null) {
                    articlesAPIObject.setWeb_url(docsObject.getString(Keys.SearchArticles.KEY_WEB_URL));
                    Log.i("WEB_URL", articlesAPIObject.getWeb_url());
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_SNIPPET) != null) {
                    articlesAPIObject.setSnippet(docsObject.getString(Keys.SearchArticles.KEY_SNIPPET));
                    Log.i("SNIPPET", articlesAPIObject.getSnippet());
                }

                JSONArray multimedia_array = docsObject.getJSONArray(Keys.SearchArticles.KEY_MULTIMEDIA);

                if (multimedia_array.length() > 0) {
                    JSONObject multimedia_object = multimedia_array.getJSONObject(2);

                    if (multimedia_object.getString(Keys.SearchArticles.KEY_IMAGE_URL) != null) {
                        articlesAPIObject.setImage_url(Url.ArticleSearchUrl.IMAGE_URL_BASE + multimedia_object.getString(Keys.SearchArticles.KEY_IMAGE_URL));
                        Log.i("IMAGE_URL", articlesAPIObject.getImage_url());
                    }
                } else {
                    articlesAPIObject.setImage_url("");
                    Log.i("IMAGE_URL", "ARRAY.size() = 0");
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_PUB_DATE) != null) {
                    String pub_date = docsObject.getString(Keys.SearchArticles.KEY_PUB_DATE);
                    pub_date.substring(0, 10);
                    String day = pub_date.substring(8, 10);
                    String month = pub_date.substring(5, 7);
                    String year = pub_date.substring(0, 4);
                    pub_date = day + "/" + month + "/" + year;
                    articlesAPIObject.setPub_date(pub_date);
                    Log.i("PUB_DATE", articlesAPIObject.getPub_date());
                }

                if (docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK) != null) {
                    if (docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK).equals("None")) {
                        articlesAPIObject.setNew_desk("General");
                    } else {
                        articlesAPIObject.setNew_desk(docsObject.getString(Keys.SearchArticles.KEY_NEW_DESK));
                    }
                    Log.i("NEW_DESK", articlesAPIObject.getNew_desk());
                }

                //We put the object with the results into the ArrayList articlesAPIObjectList;
                listOfArticlesObjects.add(articlesAPIObject);
                Log.i("position # ", "" + i + " :" + listOfArticlesObjects.get(i).getSnippet());

            }

            Log.i("ArrayList.size()", "" + listOfArticlesObjects.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /** If the JSON Request is done to MOST POPULAR API,
     * then this code will run*/
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
                    Log.i("ARTICLE_URL", mostPopularAPIObject.getArticle_url());
                }

                if (resultsObject.getString(Keys.MostPopularKeys.KEY_PUBLISHED_DATE) != null) {
                    String published_date = resultsObject.getString(Keys.MostPopularKeys.KEY_PUBLISHED_DATE);
                    published_date.substring(0,10);
                    String day = published_date.substring(8,10);
                    String month = published_date.substring(5,7);
                    String year = published_date.substring(0,4);
                    published_date = day + "/" + month + "/" + year;
                    mostPopularAPIObject.setPublished_date(published_date);
                    Log.i("PUBLISHED DATE", mostPopularAPIObject.getPublished_date());
                }

                JSONArray media_array = resultsObject.getJSONArray(Keys.MostPopularKeys.KEY_MEDIA);

                JSONObject media_Object0 = media_array.getJSONObject(0);

                JSONArray media_metadata = media_Object0.getJSONArray(Keys.MostPopularKeys.KEY_MEDIA_METADATA);

                JSONObject media_metadata0 = media_metadata.getJSONObject(0);

                if (media_metadata0.getString(Keys.MostPopularKeys.KEY_IMAGE_URL) != null){
                    mostPopularAPIObject.setImage_thumbnail(media_metadata0.getString(Keys.MostPopularKeys.KEY_IMAGE_URL));
                }

                //We put the object with the results into the ArrayList topStoriesObjectArrayList;
                listOfMostPopularObjects.add(mostPopularAPIObject);
                Log.i("MPposition # ", "" + i + " :" + listOfMostPopularObjects.get(i).getTitle());

            }

            Log.i("ArrayList.size()", "" + listOfMostPopularObjects.size());

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    /** If the JSON Request is done to TOP STORIES API,
     * then this code will run*/
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

            //Loop to see that all objects ib the ArrayList are different
            for (int i = 0; i < listOfTopStoriesObjects.size() ; i++) {
                Log.i("TS_ARRAY_SUMM_TITLES # ", "" + i + " :" + listOfTopStoriesObjects.get(i).getTitle());
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }

    }

    public static void showErrorMessage(Context context) {
        Toast.makeText(context, "An error has occurred", Toast.LENGTH_SHORT).show();
    }

}

