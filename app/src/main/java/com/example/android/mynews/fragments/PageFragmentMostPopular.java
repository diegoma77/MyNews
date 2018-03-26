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
import com.example.android.mynews.pojo.MostPopularObject;
import com.example.android.mynews.rvadapters.RvAdapterMostPopular;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentMostPopular extends android.support.v4.app.Fragment {

    //Logs
    private static final String TAG = "PageFragmentTopStories";

    //Array that will store the TopStoriesObject object to display in the RecyclerView
    private ArrayList<MostPopularObject> mostPopularObjectsArrayList;

    //Variables to store views related to the articles upload
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    //Database variables
    Cursor mCursor;
    DatabaseHelper dbH;

    //RecyclerView and RecyclerViewAdapter
    private RecyclerView recyclerView;
    private RvAdapterMostPopular rvAdapterMostPopular;

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

        mostPopularObjectsArrayList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        rvAdapterMostPopular = new RvAdapterMostPopular(getActivity(), mCursor);

        loadMostPopularInfo();

        recyclerView.setAdapter(rvAdapterMostPopular);

        return view;
        
    }

    public void loadMostPopularInfo () {

        sendJSONRequest(Url.MostPopularUrl.MP_FINAL_URL);

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

                //We get the "i results object"
                JSONObject resultsObject = results_array.getJSONObject(i);

                //GETS the rest of the data from the dataObject
                if (resultsObject.getString(Keys.MostPopularKeys.KEY_SECTION) != null) {
                    mostPopularObject.setSection(resultsObject.getString(Keys.MostPopularKeys.KEY_SECTION));
                    Log.i("SECTION", mostPopularObject.getSection());
                }

                if (resultsObject.getString(Keys.MostPopularKeys.KEY_TITLE) != null) {
                    mostPopularObject.setTitle(resultsObject.getString(Keys.MostPopularKeys.KEY_TITLE));
                    Log.i("TITLE", mostPopularObject.getTitle());
                }

                if (resultsObject.getString(Keys.MostPopularKeys.KEY_ARTICLE_URL) != null) {
                    mostPopularObject.setArticle_url(resultsObject.getString(Keys.MostPopularKeys.KEY_ARTICLE_URL));
                    Log.i("ARTICLE_URL", mostPopularObject.getArticle_url());
                }

                if (resultsObject.getString(Keys.MostPopularKeys.KEY_PUBLISHED_DATE) != null) {
                    String published_date = resultsObject.getString(Keys.MostPopularKeys.KEY_PUBLISHED_DATE);
                    published_date.substring(0,10);
                    String day = published_date.substring(8,10);
                    String month = published_date.substring(5,7);
                    String year = published_date.substring(0,4);
                    published_date = day + "/" + month + "/" + year;
                    mostPopularObject.setPublished_date(published_date);
                    Log.i("PUBLISHED DATE", mostPopularObject.getPublished_date());
                }

                JSONArray media_array = resultsObject.getJSONArray(Keys.MostPopularKeys.KEY_MEDIA);

                JSONObject media_Object0 = media_array.getJSONObject(0);

                JSONArray media_metadata = media_Object0.getJSONArray(Keys.MostPopularKeys.KEY_MEDIA_METADATA);

                JSONObject media_metadata0 = media_metadata.getJSONObject(0);

                if (media_metadata0.getString(Keys.MostPopularKeys.KEY_IMAGE_URL) != null){
                    mostPopularObject.setImage_thumbnail(media_metadata0.getString(Keys.MostPopularKeys.KEY_IMAGE_URL));
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

                rvAdapterMostPopular.setMostPopularData(mostPopularObjectsArrayList);
                Log.i("setMOSTPOPULARData:", "Called(size = " + mostPopularObjectsArrayList.size() + ")");
                showRecyclerView();

            }
            else {
                showErrorMessage();
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}



