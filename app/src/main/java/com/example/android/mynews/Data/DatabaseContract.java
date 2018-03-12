package com.example.android.mynews.Data;

import android.provider.BaseColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Diego Fajardo on 02/03/2018.
 */

public class DatabaseContract {

    public DatabaseContract () {

    }

    public static class Database implements BaseColumns {

        //TABLE NAMES
        public static final String TOP_STORIES_TABLE_NAME = "top_stories_table";
        public static final String MOST_POPULAR_TABLE_NAME = "most_popular_table";
        public static final String BUSINESS_TABLE_NAME = "business_table";
        public static final String SPORTS_TABLE_NAME = "sports_table";

        //SHARED COLUMNS, name of columns
        //Image urls size are based on NYTimes API
        public static final String RESULT_ID = "id";
        public static final String SECTION = "section";
        public static final String TITLE = "title";
        public static final String UPDATE_DATE = "update_date";
        public static final String IMAGE_URL_THUMBNAIL = "image_url_thumbnail";
        public static final String IMAGE_URL_THUMBLARGE = "image_url_thumb_arge";
        public static final String IMAGE_URL_NORMAL = "image_url_normal";
        public static final String IMAGE_URL_MEDIUM = "image_url_medium";
        public static final String IMAGE_URL_SUPERJUMBO = "image_url_super_umbo";
        public static final String ARTICLE_URL = "article_url";




    }

}
