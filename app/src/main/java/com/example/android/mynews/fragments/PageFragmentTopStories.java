package com.example.android.mynews.fragments;

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
import com.example.android.mynews.rvadapters.RvAdapterTopStories;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.Url;
import com.example.android.mynews.pojo.TopStoriesObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentTopStories extends android.support.v4.app.Fragment {

    // TODO: 13/03/2018 Each time the ViewPager comes back to this Page, the information has to be deleted and requested again.

    //Logs
    private static final String TAG = "PageFragmentTopStories";

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private ArrayList<TopStoriesObject> topStoriesObjectsArrayList;

    //Top Stories table name
    private String table_name_top_stories = DatabaseContract.Database.TOP_STORIES_TABLE_NAME;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    //RecyclerView and RecyclerViewAdapter
    private RecyclerView recyclerView;
    private RvAdapterTopStories rvAdapterTopStories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);

        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        topStoriesObjectsArrayList = new ArrayList<TopStoriesObject>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        rvAdapterTopStories = new RvAdapterTopStories(getActivity());

        loadTopStoriesInfo();

        recyclerView.setAdapter(rvAdapterTopStories);

        return view;
        
    }

    public void loadTopStoriesInfo () {

        showTopStoriesView();
        sendJSONRequest(new Url().getTopStoriesApiUrl());

    }

    public void showTopStoriesView () {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage () {
        /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void sendJSONRequest (String url){

        Toast.makeText(getContext(), "Data is loading", Toast.LENGTH_LONG).show();

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

        // TODO: 13/03/2018 Add if statements to check if the data was received or not and avoid crashes
        
        try {

            //We create the object that is going to store all the information
            TopStoriesObject topStoriesObject = new TopStoriesObject();

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObject_response = new JSONObject(response);

            //JSON array made of the objects inside the "result"
            JSONArray results_array =
                    jsonObject_response.getJSONArray(Keys.TopStoriesKeys.KEY_RESULTS);

            //Iterating through "results_array"
            for (int i = 0; i < results_array.length(); i++) {

                //We get the "i results object"
                JSONObject dataObject = results_array.getJSONObject(i);

                //We get the multimedia array from the "i results object".
                JSONArray multimedia_array = dataObject.getJSONArray(Keys.TopStoriesKeys.KEY_MULTIMEDIA);

                for (int j = 0; j < multimedia_array.length(); j++) {

                    JSONObject multimedia_object = multimedia_array.getJSONObject(j);

                    // TODO: 13/03/2018 Erase "switch statement" when decided which image to take
                    switch (j) {
                        case 0:
                            topStoriesObject.setImageThumbnail(
                                    multimedia_object.getString(Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                        case 1:
                            topStoriesObject.setImageThumblarge(multimedia_object.getString(
                                    Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                        case 2:
                            topStoriesObject.setImageNormal(multimedia_object.getString(
                                    Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                        case 3:
                            topStoriesObject.setImageMedium(multimedia_object.getString(
                                    Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                        case 4:
                            topStoriesObject.setImageSuperjumbo(multimedia_object.getString(
                                    Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                    }

                }

                //GETS the rest of the data from the dataObject and puts
                topStoriesObject.setSection(dataObject.getString(Keys.TopStoriesKeys.KEY_SECTION));
                topStoriesObject.setTitle(dataObject.getString(Keys.TopStoriesKeys.KEY_TITLE));
                topStoriesObject.setArticleUrl(dataObject.getString(Keys.TopStoriesKeys.KEY_ARTICLE_URL));

                String updated_date = dataObject.getString(Keys.TopStoriesKeys.KEY_UPDATED_DATE);
                topStoriesObject.setUpdatedDate(updated_date.substring(0,10));

                Log.i("SECTION", topStoriesObject.getSection());
                Log.i("TITLE", topStoriesObject.getTitle());
                Log.i("UPDATE_DATE", topStoriesObject.getUpdatedDate());
                Log.i("IMAGE_URL_THUMBNAIL", topStoriesObject.getImageThumbnail());
                Log.i("IMAGE_URL_THUMBLARGE", topStoriesObject.getImageThumblarge());
                Log.i("IMAGE_URL_NORMAL", topStoriesObject.getImageNormal());
                Log.i("IMAGE_URL_MEDIUM", topStoriesObject.getImageMedium());
                Log.i("IMAGE_URL_SUPERJUMBO", topStoriesObject.getImageSuperjumbo());
                Log.i("ARTICLE_URL", topStoriesObject.getArticleUrl());

                //We put the object with the results into the ArrayList topStoriesObjectArrayList;

                    topStoriesObjectsArrayList.add(topStoriesObject);

            }

            Log.i("ArrayList.size():", "" + topStoriesObjectsArrayList.size());

            if (topStoriesObjectsArrayList != null) {
                showTopStoriesView();
                rvAdapterTopStories.setTopStoriesData(topStoriesObjectsArrayList);
                Log.i("setTopStoriesData:", "Called(size = " + topStoriesObjectsArrayList.size() + ")");

            }
            else {
                showErrorMessage();
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}



