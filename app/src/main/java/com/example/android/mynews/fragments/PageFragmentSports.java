package com.example.android.mynews.fragments;

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
import com.example.android.mynews.pojo.SportsObject;
import com.example.android.mynews.rvadapters.RvAdapterSports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentSports extends android.support.v4.app.Fragment {

    //Logs
    private static final String TAG = "PageFragmentTopStories";

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private ArrayList<SportsObject> sportsObjectArrayList;

    //Variables to store views related to the articles upload
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    //Database variables
    Cursor mCursor;
    DatabaseHelper dbH;

    //RecyclerView and RecyclerViewAdapter
    private RecyclerView recyclerView;
    private RvAdapterSports rvAdapterSports;

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

        sportsObjectArrayList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        rvAdapterSports = new RvAdapterSports(getActivity(), mCursor);

        loadSportsInfo();

        recyclerView.setAdapter(rvAdapterSports);

        return view;
        
    }

    public void loadSportsInfo() {

        sendJSONRequest(Url.SportsUrl.S_FINAL_URL);

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

    public void sendJSONRequest (String url){

        showProgressBar();

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
                    jsonObject_response.getJSONArray(Keys.Sports.KEY_RESULTS);

            //Iterating through "results_array"
            for (int i = 0; i < results_array.length(); i++) {

                //We create the object that is going to store all the information
                SportsObject sportsObject = new SportsObject();

                //We get the "i results object"
                JSONObject dataObject = results_array.getJSONObject(i);

                //We get the multimedia array from the "i results object".
                JSONArray multimedia_array = dataObject.getJSONArray(Keys.Sports.KEY_MULTIMEDIA);

                Log.i ("MULTIMEDIA_LENGTH", "" + multimedia_array.length());

                for (int j = 0; j < multimedia_array.length(); j++) {

                    JSONObject multimedia_object = multimedia_array.getJSONObject(j);

                    switch (j) {
                        case 0:
                            if (multimedia_object.getString(Keys.Sports.KEY_IMAGE_URL) != null) {
                                sportsObject.setImageThumbnail(
                                        multimedia_object.getString(Keys.Sports.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_THUMBNAIL", sportsObject.getImageThumbnail());

                            }
                            break;

                        case 1:
                            if (multimedia_object.getString(Keys.Sports.KEY_IMAGE_URL) != null) {
                                sportsObject.setImageThumblarge(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_THUMBLARGE", sportsObject.getImageThumblarge());
                            }
                            break;

                        case 2:
                            if (multimedia_object.getString(Keys.Sports.KEY_IMAGE_URL) != null) {
                                sportsObject.setImageNormal(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_NORMAL", sportsObject.getImageNormal());
                            }
                            break;

                        case 3:
                            if (multimedia_object.getString(Keys.Sports.KEY_IMAGE_URL) != null) {
                                sportsObject.setImageMedium(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_MEDIUM", sportsObject.getImageMedium());
                            }
                            break;

                        case 4:
                            if (multimedia_object.getString(Keys.Sports.KEY_IMAGE_URL) != null) {
                                sportsObject.setImageSuperjumbo(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_SUPERJUMBO", sportsObject.getImageSuperjumbo());
                            }
                            break;
                    }
                }

                //CHECKS that the data from the JSON objects is not null
                //If its not null, it sets the property of the object with the value
                if (dataObject.getString(Keys.Sports.KEY_SUBSECTION) != null) {
                    sportsObject.setSubsection(dataObject.getString(Keys.Sports.KEY_SUBSECTION));
                    Log.i("SECTION", sportsObject.getSubsection());
                }

                if (dataObject.getString(Keys.Sports.KEY_TITLE) != null) {
                    sportsObject.setTitle(dataObject.getString(Keys.Sports.KEY_TITLE));
                    Log.i("TITLE", sportsObject.getTitle());
                }

                if (dataObject.getString(Keys.Sports.KEY_ARTICLE_URL) != null) {
                    sportsObject.setArticleUrl(dataObject.getString(Keys.Sports.KEY_ARTICLE_URL));
                    Log.i("ARTICLE_URL", sportsObject.getArticleUrl());
                }

                if (dataObject.getString(Keys.Sports.KEY_UPDATED_DATE) != null) {
                    String updated_date = dataObject.getString(Keys.Sports.KEY_UPDATED_DATE);
                    updated_date.substring(0,10);
                    String day = updated_date.substring(8,10);
                    String month = updated_date.substring(5,7);
                    String year = updated_date.substring(0,4);
                    updated_date = day + "/" + month + "/" + year;
                    sportsObject.setUpdatedDate(updated_date);
                    Log.i("UPDATE_DATE", sportsObject.getUpdatedDate());
                }

                sportsObjectArrayList.add(sportsObject);
                Log.i("TS_ARRAYLIST # ", "" + i + ", " + sportsObjectArrayList.get(i).getTitle());

            }

            //Loop to see that all objects ib the ArrayList are different
            for (int i = 0; i < sportsObjectArrayList.size() ; i++) {
               Log.i("TS_ARRAY_SUMM_TITLES # ", "" + i + " :" + sportsObjectArrayList.get(i).getTitle());
            }

            //Sets the RVAdapter with the data from the JSON response
            if (sportsObjectArrayList != null){
               rvAdapterSports.setSportsData(sportsObjectArrayList);
               Log.i ("TS_ADAPTER SET WITH:", "" + sportsObjectArrayList.size() + " objects");
               showRecyclerView();
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

}



