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
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.Url;
import com.example.android.mynews.pojo.MostPopularObject;
import com.example.android.mynews.pojo.TopStoriesObject;
import com.example.android.mynews.rvadapters.RvAdapterMostPopular;
import com.example.android.mynews.rvadapters.RvAdapterTopStories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentMostPopular extends android.support.v4.app.Fragment {

    // TODO: 13/03/2018 Each time the ViewPager comes back to this Page, the information has to be deleted and requested again.

    //Logs
    private static final String TAG = "PageFragmentTopStories";

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private ArrayList<MostPopularObject> mostPopularObjectsArrayList;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    //RecyclerView and RecyclerViewAdapter
    private RecyclerView recyclerView;
    private RvAdapterMostPopular rvAdapterMostPopular;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);

        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        mostPopularObjectsArrayList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        rvAdapterMostPopular = new RvAdapterMostPopular(getActivity());

        loadMostPopularInfo();

        recyclerView.setAdapter(rvAdapterMostPopular);

        return view;
        
    }

    public void loadMostPopularInfo () {

        showMostPopularView();
        sendJSONRequest(new Url().getMostPopularApiUrl());

    }

    public void showMostPopularView () {
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
                                "Error getting the info", Toast.LENGTH_SHORT).show();
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

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObject_response = new JSONObject(response);

            //JSON array made of the objects inside the "result"
            JSONArray results_array =
                    jsonObject_response.getJSONArray(Keys.MostPopularKeys.KEY_RESULTS);

            //Iterating through "results_array"
            for (int i = 0; i < results_array.length(); i++) {

                //We create the object that is going to store all the information
                MostPopularObject mostPopularObject = new MostPopularObject();

                // TODO: 13/03/2018 We have yet to get the image url

                //We get the "i results object"
                JSONObject dataObject = results_array.getJSONObject(i);

                //GETS the rest of the data from the dataObject
                if (dataObject.getString(Keys.MostPopularKeys.KEY_SECTION) != null) {
                    mostPopularObject.setSection(dataObject.getString(Keys.MostPopularKeys.KEY_SECTION));
                    Log.i("SECTION", mostPopularObject.getSection());
                }

                if (dataObject.getString(Keys.MostPopularKeys.KEY_TITLE) != null) {
                    mostPopularObject.setTitle(dataObject.getString(Keys.MostPopularKeys.KEY_TITLE));
                    Log.i("TITLE", mostPopularObject.getTitle());
                }

                if (dataObject.getString(Keys.MostPopularKeys.KEY_ARTICLE_URL) != null) {
                    mostPopularObject.setArticle_url(dataObject.getString(Keys.MostPopularKeys.KEY_ARTICLE_URL));
                    Log.i("ARTICLE_URL", mostPopularObject.getArticle_url());
                }

                if (dataObject.getString(Keys.MostPopularKeys.KEY_PUBLISHED_DATE) != null) {
                    mostPopularObject.setPublished_date(dataObject.getString(Keys.MostPopularKeys.KEY_PUBLISHED_DATE));
                    Log.i("PUBLISHED DATE", mostPopularObject.getPublished_date());
                }

                //We put the object with the results into the ArrayList topStoriesObjectArrayList;
                mostPopularObjectsArrayList.add(mostPopularObject);
                Log.i("MPposition # ", "" + i + " :" + mostPopularObjectsArrayList.get(i).getTitle());

            }

            for (int i = 0; i < mostPopularObjectsArrayList.size() ; i++) {

                Log.i("MPposition # ", "" + i + " :" + mostPopularObjectsArrayList.get(i).getTitle());

            }

            Log.i("ArrayList.size():", "" + mostPopularObjectsArrayList.size());

            if (mostPopularObjectsArrayList != null) {

                showMostPopularView();
                rvAdapterMostPopular.setMostPopularData(mostPopularObjectsArrayList);
                Log.i("setMOSTPOPULARData:", "Called(size = " + mostPopularObjectsArrayList.size() + ")");

            }
            else {
                showErrorMessage();
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}



