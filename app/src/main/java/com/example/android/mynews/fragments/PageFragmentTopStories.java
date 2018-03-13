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

import com.example.android.mynews.R;
import com.example.android.mynews.data.DataFromJSONInString;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.adapters.RvAdapterTopStories;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.TopStoriesResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentTopStories extends android.support.v4.app.Fragment {

    //Logs
    private String Log_info = "INFORMATION TOPSTORIES";
    private String Log_error = "ERROR TOPSTORIES";

    //Array that will store the TopStoriesResults object to display in the RecyclerView
    private ArrayList<TopStoriesResults> topStoryResults = new ArrayList<>();

    //Top Stories table name
    private String table_name_top_stories = DatabaseContract.Database.TOP_STORIES_TABLE_NAME;

    //URL construction
    private String BASE_URL = "http://api.nytimes.com/svc/topstories/v2/";
    private String news_section = "world";
    private String format = ".json";
    private String QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
    private String URL = BASE_URL + news_section + format + QM_ApiKey ;

    //RecyclerView and RecyclerViewAdapter
    RecyclerView recyclerView;
    RvAdapterTopStories rvAdapterTopStories;

    ///DATABASE Variables
    Cursor mCursor;
    DatabaseHelper dbH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // TODO: 13/03/2018 Inflate another fragment (with progress bar) while data is loading. 

        // TODO: 13/03/2018 Inflate this fragment only when data is loaded 
        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);

        dbH = new DatabaseHelper(getActivity());
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.TOP_STORIES_TABLE_NAME);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // TODO: 13/03/2018 Call the adapter passing the ArrayList as a paramenter so the RecyclerView will have the ArrayList 
        rvAdapterTopStories = new RvAdapterTopStories(getActivity(), mCursor);
        recyclerView.setAdapter(rvAdapterTopStories);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // TODO: 13/03/2018 Create an StringRequest method here (= sendJSONRequest) with method "parseJSONRequestW inside.

        return view;
        
    }

    // TODO: 13/03/2018 Add method getRequestURL that returns a URL for the constructor of StringRequest
    // (use different parameters to get different URLS).

    // TODO: 13/03/2018 Create method sendJSONRequest that creates a JSONRequest (= to StringRequest) (basically, loadData in ActivityLoader)

    // TODO: 13/03/2018 Create method parseJSONResponse (basically, save data method in ActivityLoader)
    // TODO: 13/03/2018 Check that the response is not null or that the response.length is not 0 (return nothing)

    public void parseJSONResponse (JSONObject response) {

        //if (response == null || response.length() == 0) return;

        // TODO: 13/03/2018 Add if statements to check if the data was received or not and avoid crashes
        
        try {

            //We create the object that is going to store all the information
            DataFromJSONInString dataFromJSONInString = new DataFromJSONInString();

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

                    // TODO: 13/03/2018 Erase switch when decided which image to take
                    switch (j) {
                        case 0:
                            dataFromJSONInString.setImageThumbnail(
                                    multimedia_object.getString(Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                        case 1:
                            dataFromJSONInString.setImageThumblarge(multimedia_object.getString(
                                    Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                        case 2:
                            dataFromJSONInString.setImageNormal(multimedia_object.getString(
                                    Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                        case 3:
                            dataFromJSONInString.setImageMedium(multimedia_object.getString(
                                    Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                        case 4:
                            dataFromJSONInString.setImageSuperjumbo(multimedia_object.getString(
                                    Keys.TopStoriesKeys.KEY_IMAGE_URL));
                            break;

                    }

                }

                //GETS the rest of the data from the dataObject
                dataFromJSONInString.setSection(dataObject.getString(Keys.TopStoriesKeys.KEY_SECTION));
                dataFromJSONInString.setTitle(dataObject.getString(Keys.TopStoriesKeys.KEY_TITLE));
                dataFromJSONInString.setUpdatedDate(dataObject.getString(Keys.TopStoriesKeys.KEY_UPDATED_DATE));
                dataFromJSONInString.setArticleUrl(dataObject.getString(Keys.TopStoriesKeys.KEY_ARTICLE_URL));

                Log.i("SECTION", dataFromJSONInString.getSection());
                Log.i("TITLE", dataFromJSONInString.getTitle());
                Log.i("UPDATE_DATE", dataFromJSONInString.getUpdatedDate());
                Log.i("IMAGE_URL_THUMBNAIL", dataFromJSONInString.getImageThumbnail());
                Log.i("IMAGE_URL_THUMBLARGE", dataFromJSONInString.getImageThumblarge());
                Log.i("IMAGE_URL_NORMAL", dataFromJSONInString.getImageNormal());
                Log.i("IMAGE_URL_MEDIUM", dataFromJSONInString.getImageMedium());
                Log.i("IMAGE_URL_SUPERJUMBO", dataFromJSONInString.getImageSuperjumbo());
                Log.i("ARTICLE_URL", dataFromJSONInString.getArticleUrl());

            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
}



