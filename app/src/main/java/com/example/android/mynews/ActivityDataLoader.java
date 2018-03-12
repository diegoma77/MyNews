package com.example.android.mynews;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class ActivityDataLoader extends AppCompatActivity {

    ProgressBar progressBar;
    TextView tvLoadingData;

    //URL construction
    private String BASE_URL = "http://api.nytimes.com/svc/topstories/v2/";
    private String news_section = "world";
    private String format = ".json";
    private String QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
    private String URL = BASE_URL + news_section + format + QM_ApiKey;

    //TOP STORIES. String conversions for Data (JSON API structure)
    private String RESULTS = "results";
    private String SECTION = "section";
    private String TITLE = "title";
    private String ARTICLE_URL = "url";
    private String UPDATED_DATE = "updated_date";
    private String MULTIMEDIA = "multimedia";
    private String IMAGE_URL = "url";

    //Strings for storing the received data from the API
    private String table_name_top_stories = DatabaseContract.Database.TOP_STORIES_TABLE_NAME;
    private String table_name_most_popular = DatabaseContract.Database.MOST_POPULAR_TABLE_NAME;
    private String table_name_business = DatabaseContract.Database.BUSINESS_TABLE_NAME;
    private String table_name_sports = DatabaseContract.Database.SPORTS_TABLE_NAME;
    private String section;
    private String title;
    private String update_date;
    private String image_url_thumbnail;
    private String image_url_thumblarge;
    private String image_url_normal;
    private String image_url_medium;
    private String image_url_superjumbo;
    private String article_url;

    //Database
    SQLiteDatabase db;

    //Database Helper
    DatabaseHelper dbH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_loader);

        //Checking if the database is closed
        //db = DatabaseContract.Database.TOP_STORIES_TABLE_NAME.getWritableDatabase();

        dbH = new DatabaseHelper(this);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvLoadingData = (TextView) findViewById(R.id.loading_data_text);

        //Progress bar visibility
        progressBar.setVisibility(View.VISIBLE);

        loadDataTopStories();

        Intent intent = new Intent(ActivityDataLoader.this, MainActivity.class);
        startActivity(intent);

    }

    public void loadDataTopStories() {

        //String request
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        saveDataTopStories(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public void saveDataTopStories(String response) {

        try {

            //We create the object that is going to store all the information
            DataFromJSONInString dataFromJSONInString = new DataFromJSONInString();

            //JSON object that gathers all the objects of the response from the API
            JSONObject jsonObject_response = new JSONObject(response);

            //JSON array made of the objects inside the "result"
            JSONArray results_array =
                    jsonObject_response.getJSONArray(RESULTS);

            //Iterating through "results_array"
            for (int i = 0; i < results_array.length(); i++) {

                //We get the "i results object"
                JSONObject dataObject = results_array.getJSONObject(i);

                //We get the multimedia array from the "i results object".
                JSONArray multimedia_array = dataObject.getJSONArray(MULTIMEDIA);

                for (int j = 0; j < multimedia_array.length(); j++) {

                    JSONObject multimedia_object = multimedia_array.getJSONObject(j);

                    switch (j) {
                        case 0:
                            dataFromJSONInString.setImageThumbnail(multimedia_object.getString(IMAGE_URL));
                            break;

                        case 1:
                            dataFromJSONInString.setImageThumblarge(multimedia_object.getString(IMAGE_URL));
                            break;

                        case 2:
                            dataFromJSONInString.setImageNormal(multimedia_object.getString(IMAGE_URL));
                            break;

                        case 3:
                            dataFromJSONInString.setImageMedium(multimedia_object.getString(IMAGE_URL));
                            break;

                        case 4:
                            dataFromJSONInString.setImageSuperjumbo(multimedia_object.getString(IMAGE_URL));
                            break;

                    }

                }

                //GETS the rest of the data from the dataObject
                dataFromJSONInString.setSection(dataObject.getString(SECTION));
                dataFromJSONInString.setTitle(dataObject.getString(TITLE));
                dataFromJSONInString.setUpdatedDate(dataObject.getString(UPDATED_DATE));
                dataFromJSONInString.setArticleUrl(dataObject.getString(ARTICLE_URL));

                Log.i("SECTION", dataFromJSONInString.getSection());
                Log.i("TITLE", dataFromJSONInString.getTitle());
                Log.i("UPDATE_DATE", dataFromJSONInString.getUpdatedDate());
                Log.i("IMAGE_URL_THUMBNAIL", dataFromJSONInString.getImageThumbnail());
                Log.i("IMAGE_URL_THUMBLARGE", dataFromJSONInString.getImageThumblarge());
                Log.i("IMAGE_URL_NORMAL", dataFromJSONInString.getImageNormal());
                Log.i("IMAGE_URL_MEDIUM", dataFromJSONInString.getImageMedium());
                Log.i("IMAGE_URL_SUPERJUMBO", dataFromJSONInString.getImageSuperjumbo());
                Log.i("ARTICLE_URL", dataFromJSONInString.getArticleUrl());

                section = dataFromJSONInString.getSection();
                title = dataFromJSONInString.getTitle();
                update_date = dataFromJSONInString.getUpdatedDate();
                image_url_thumbnail = dataFromJSONInString.getImageThumbnail();
                image_url_thumblarge = dataFromJSONInString.getImageThumblarge();
                image_url_normal = dataFromJSONInString.getImageNormal();
                image_url_medium = dataFromJSONInString.getImageMedium();
                image_url_superjumbo = dataFromJSONInString.getImageSuperjumbo();
                article_url = dataFromJSONInString.getArticleUrl();

                update_date = update_date.substring(0,10);

                /**
                 * BEFORE PUTTING THE UPDATED_DATE
                 * INFORMATION IN THE DATABASE,
                 * THE DATE FORMAT MUST BE CHANGED
                 * */

                //INSERTS INFORMATION INTO THE DATABASE
                dbH.insertDataWithTableName(
                        DatabaseContract.Database.TOP_STORIES_TABLE_NAME,
                        section,
                        title,
                        update_date,
                        image_url_thumbnail,
                        image_url_thumblarge,
                        image_url_normal,
                        image_url_medium,
                        image_url_superjumbo,
                        article_url
                );
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
}