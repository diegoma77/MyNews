package com.example.android.mynews.data;

import android.provider.BaseColumns;

/**
 * Created by Diego Fajardo on 02/03/2018.
 */

public class DatabaseContract {

    public DatabaseContract () {

    }

    public static class Database implements BaseColumns {

        //TABLE NAME
        public static final String ALREADY_READ_ARTICLES_TABLE_NAME = "top_stories_table";

        //TABLE COLUMNS
        public static final String RESULT_ID = "id";
        public static final String ARTICLE_URL = "article_url";
        
    }

}
