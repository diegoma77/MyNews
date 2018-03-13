package com.example.android.mynews.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mynews.R;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.adapters.RvAdapterTopStories;

import org.json.JSONObject;

/**
 * Created by Diego Fajardo on 22/02/2018.
 */

public class PageFragmentTopStories extends android.support.v4.app.Fragment {

    //Logs
    private String Log_info = "INFORMATION TOPSTORIES";
    private String Log_error = "ERROR TOPSTORIES";

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

        //response.getJSONArray()

    }

    
}


