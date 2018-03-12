package com.example.android.mynews;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.mynews.Data.DataFromJSONInString;
import com.example.android.mynews.Data.DatabaseContract;
import com.example.android.mynews.Data.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

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

        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);

        dbH = new DatabaseHelper(getActivity());
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.TOP_STORIES_TABLE_NAME);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        rvAdapterTopStories = new RvAdapterTopStories(getActivity(), mCursor);
        recyclerView.setAdapter(rvAdapterTopStories);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

}


