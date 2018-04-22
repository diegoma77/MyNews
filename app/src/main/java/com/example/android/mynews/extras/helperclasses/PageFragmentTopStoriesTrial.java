package com.example.android.mynews.extras.helperclasses;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.Url;
import com.example.android.mynews.pojo.TopStoriesObject;
import com.example.android.mynews.rvadapters.RvAdapterTopStories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentTopStoriesTrial extends android.support.v4.app.Fragment {

    //Logs
    private static final String TAG = "PageFragmentTopStories";

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private List<TopStoriesObject> topStoriesObjectList;

    //Variables to store views related to the articles upload
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    //Database variables
    Cursor mCursor;
    DatabaseHelper dbH;

    //RecyclerView and RecyclerViewAdapter
    private RecyclerView recyclerView;
    private RvAdapterTopStories rvAdapterTopStories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rv_fragments_layout, container, false);

        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        dbH = new DatabaseHelper(getContext());
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

        recyclerView.setHasFixedSize(true);

        topStoriesObjectList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        rvAdapterTopStories = new RvAdapterTopStories(getActivity(), mCursor);

        loadTopStoriesInfo();

        recyclerView.setAdapter(rvAdapterTopStories);

        return view;
        
    }

    public void loadTopStoriesInfo () {

        sendJSONRequestToTopStoriesAPI(Url.TopStoriesUrl.TS_FINAL_URL);

    }

    public void showProgressBar () {

        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

    }

    public void showRecyclerView() {

        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    public void showErrorMessage () {

        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

    }

    public void sendJSONRequestToTopStoriesAPI(String url){

        showProgressBar();

        //String request
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSONResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                        showErrorMessage();
                    }
                }
        );

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public void parseJSONResponse (String response) {

        if (response == null || response.length() == 0) return;
        
        try {

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObject_response = new JSONObject(response);

            //JSON array made of the objects inside the "result"
            JSONArray results_array =
                    jsonObject_response.getJSONArray(Keys.TopStoriesKeys.KEY_RESULTS);

            //Iterating through "results_array"
            for (int i = 0; i < results_array.length(); i++) {

                //We create the object that is going to store all the information
                TopStoriesObject topStoriesObject = new TopStoriesObject();

                //We get the "i results object"
                JSONObject dataObject = results_array.getJSONObject(i);

                //We get the multimedia array from the "i results object".
                JSONArray multimedia_array = dataObject.getJSONArray(Keys.TopStoriesKeys.KEY_MULTIMEDIA);

                Log.i ("MULTIMEDIA_LENGTH", "" + multimedia_array.length());

                for (int j = 0; j < multimedia_array.length(); j++) {

                    JSONObject multimedia_object = multimedia_array.getJSONObject(j);

                    switch (j) {
                        case 0:
                            if (multimedia_object.getString(Keys.TopStoriesKeys.KEY_IMAGE_URL) != null) {
                                topStoriesObject.setImageThumbnail(
                                        multimedia_object.getString(Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_THUMBNAIL", topStoriesObject.getImageThumbnail());

                            }
                            break;

                        case 1:
                            if (multimedia_object.getString(Keys.TopStoriesKeys.KEY_IMAGE_URL) != null) {
                                topStoriesObject.setImageThumblarge(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_THUMBLARGE", topStoriesObject.getImageThumblarge());
                            }
                            break;

                        case 2:
                            if (multimedia_object.getString(Keys.TopStoriesKeys.KEY_IMAGE_URL) != null) {
                                topStoriesObject.setImageNormal(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_NORMAL", topStoriesObject.getImageNormal());
                            }
                            break;

                        case 3:
                            if (multimedia_object.getString(Keys.TopStoriesKeys.KEY_IMAGE_URL) != null) {
                                topStoriesObject.setImageMedium(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_MEDIUM", topStoriesObject.getImageMedium());
                            }
                            break;

                        case 4:
                            if (multimedia_object.getString(Keys.TopStoriesKeys.KEY_IMAGE_URL) != null) {
                                topStoriesObject.setImageSuperjumbo(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_SUPERJUMBO", topStoriesObject.getImageSuperjumbo());
                            }
                            break;
                    }
                }

                //CHECKS that the data from the JSON objects is not null
                //If its not null, it sets the property of the object with the value
                if (dataObject.getString(Keys.TopStoriesKeys.KEY_SECTION) != null) {
                    topStoriesObject.setSection(dataObject.getString(Keys.TopStoriesKeys.KEY_SECTION));
                    Log.i("SECTION", topStoriesObject.getSection());
                }

                if (dataObject.getString(Keys.TopStoriesKeys.KEY_TITLE) != null) {
                    topStoriesObject.setTitle(dataObject.getString(Keys.TopStoriesKeys.KEY_TITLE));
                    Log.i("TITLE", topStoriesObject.getTitle());
                }

                if (dataObject.getString(Keys.TopStoriesKeys.KEY_ARTICLE_URL) != null) {
                    topStoriesObject.setArticleUrl(dataObject.getString(Keys.TopStoriesKeys.KEY_ARTICLE_URL));
                    Log.i("ARTICLE_URL", topStoriesObject.getArticleUrl());
                }

                if (dataObject.getString(Keys.TopStoriesKeys.KEY_UPDATED_DATE) != null) {
                    String updated_date = dataObject.getString(Keys.TopStoriesKeys.KEY_UPDATED_DATE);
                    updated_date.substring(0,10);
                    String day = updated_date.substring(8,10);
                    String month = updated_date.substring(5,7);
                    String year = updated_date.substring(0,4);
                    updated_date = day + "/" + month + "/" + year;
                    topStoriesObject.setUpdatedDate(updated_date);
                    Log.i("UPDATE_DATE", topStoriesObject.getUpdatedDate());
                }

                topStoriesObjectList.add(topStoriesObject);
                Log.i("TS_ARRAYLIST # ", "" + i + ", " + topStoriesObjectList.get(i).getTitle());

            }

            //Loop to see that all objects ib the ArrayList are different
            for (int i = 0; i < topStoriesObjectList.size() ; i++) {
               Log.i("TS_ARRAY_SUMM_TITLES # ", "" + i + " :" + topStoriesObjectList.get(i).getTitle());
            }

            //Sets the RVAdapter with the data from the JSON response
            if (topStoriesObjectList != null){
               showRecyclerView();
               rvAdapterTopStories.setTopStoriesData(topStoriesObjectList);
               Log.i ("TS_ADAPTER SET WITH:", "" + topStoriesObjectList.size() + " objects");
               showRecyclerView();
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

}


