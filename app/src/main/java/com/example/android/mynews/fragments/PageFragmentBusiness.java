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
import com.example.android.mynews.pojo.BusinessObject;
import com.example.android.mynews.rvadapters.RvAdapterBusiness;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentBusiness extends android.support.v4.app.Fragment {

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private ArrayList<BusinessObject> businessObjectArrayList;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    //RecyclerView and RecyclerViewAdapter
    private RecyclerView recyclerView;
    private RvAdapterBusiness rvAdapterBusiness;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);

        mErrorMessageDisplay = (TextView) view.findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        businessObjectArrayList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        rvAdapterBusiness = new RvAdapterBusiness(getActivity());

        loadBusinessInfo();

        recyclerView.setAdapter(rvAdapterBusiness);

        return view;
        
    }

    public void loadBusinessInfo() {

        showBusinessView();
        sendJSONRequest(Url.BusinessUrl.B_FINAL_URL);

    }

    public void showBusinessView () {
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

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObject_response = new JSONObject(response);

            //JSON array made of the objects inside the "result"
            JSONArray results_array =
                    jsonObject_response.getJSONArray(Keys.Business.KEY_RESULTS);

            //Iterating through "results_array"
            for (int i = 0; i < results_array.length(); i++) {

                //We create the object that is going to store all the information
                BusinessObject businessObject = new BusinessObject();

                //We get the "i results object"
                JSONObject dataObject = results_array.getJSONObject(i);

                //We get the multimedia array from the "i results object".
                JSONArray multimedia_array = dataObject.getJSONArray(Keys.Business.KEY_MULTIMEDIA);

                Log.i ("MULTIMEDIA_LENGTH", "" + multimedia_array.length());

                for (int j = 0; j < multimedia_array.length(); j++) {

                    JSONObject multimedia_object = multimedia_array.getJSONObject(j);

                    // TODO: 13/03/2018 Erase "switch statement" when decided which image to take
                    switch (j) {
                        case 0:
                            if (multimedia_object.getString(Keys.Business.KEY_IMAGE_URL) != null) {
                                businessObject.setImageThumbnail(
                                        multimedia_object.getString(Keys.Business.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_THUMBNAIL", businessObject.getImageThumbnail());

                            }
                            break;

                        case 1:
                            if (multimedia_object.getString(Keys.Business.KEY_IMAGE_URL) != null) {
                                businessObject.setImageThumblarge(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_THUMBLARGE", businessObject.getImageThumblarge());
                            }
                            break;

                        case 2:
                            if (multimedia_object.getString(Keys.Business.KEY_IMAGE_URL) != null) {
                                businessObject.setImageNormal(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_NORMAL", businessObject.getImageNormal());
                            }
                            break;

                        case 3:
                            if (multimedia_object.getString(Keys.Business.KEY_IMAGE_URL) != null) {
                                businessObject.setImageMedium(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_MEDIUM", businessObject.getImageMedium());
                            }
                            break;

                        case 4:
                            if (multimedia_object.getString(Keys.Business.KEY_IMAGE_URL) != null) {
                                businessObject.setImageSuperjumbo(multimedia_object.getString(
                                        Keys.TopStoriesKeys.KEY_IMAGE_URL));
                                Log.i("IMAGE_URL_SUPERJUMBO", businessObject.getImageSuperjumbo());
                            }
                            break;
                    }
                }

                //CHECKS that the data from the JSON objects is not null
                //If its not null, it sets the property of the object with the value
                if (dataObject.getString(Keys.Sports.KEY_SUBSECTION) != null) {
                    businessObject.setSubsection(dataObject.getString(Keys.Business.KEY_SUBSECTION));
                    Log.i("SECTION", businessObject.getSubsection());
                }

                if (dataObject.getString(Keys.Sports.KEY_TITLE) != null) {
                    businessObject.setTitle(dataObject.getString(Keys.Business.KEY_TITLE));
                    Log.i("TITLE", businessObject.getTitle());
                }

                if (dataObject.getString(Keys.Sports.KEY_ARTICLE_URL) != null) {
                    businessObject.setArticleUrl(dataObject.getString(Keys.Business.KEY_ARTICLE_URL));
                    Log.i("ARTICLE_URL", businessObject.getArticleUrl());
                }

                String updated_date = dataObject.getString(Keys.Business.KEY_UPDATED_DATE);
                if (updated_date != null) {
                    businessObject.setUpdatedDate(updated_date.substring(0, 10));
                    Log.i("UPDATE_DATE", businessObject.getUpdatedDate());
                }

                businessObjectArrayList.add(businessObject);
                Log.i("B_ARRAYLIST # ", "" + i + ", " + businessObjectArrayList.get(i).getTitle());

            }

            //Loop to see that all objects ib the ArrayList are different
            for (int i = 0; i < businessObjectArrayList.size() ; i++) {
               Log.i("B_ARRAY_SUMM_TITLES # ", "" + i + " :" + businessObjectArrayList.get(i).getTitle());
            }

            //Sets the RVAdapter with the data from the JSON response
            if (businessObjectArrayList != null){
               showBusinessView();
               rvAdapterBusiness.setBusinessData(businessObjectArrayList);
               Log.i ("B_ADAPTER SET WITH:", "" + businessObjectArrayList.size() + " objects");
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

}



