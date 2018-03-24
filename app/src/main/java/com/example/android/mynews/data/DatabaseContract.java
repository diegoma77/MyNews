package com.example.android.mynews.data;

import android.provider.BaseColumns;

/**
 * Created by Diego Fajardo on 02/03/2018.
 */

public class DatabaseContract {

    public DatabaseContract () {

    }

    public static class Database implements BaseColumns {

        //TABLE NAME for keeping track of the read articles
        public static final String ALREADY_READ_ARTICLES_TABLE_NAME = "articles_read_table";

        //TABLE COLUMNS
        public static final String RESULT_ID = "id";
        public static final String ARTICLE_URL = "article_url";

        //TABLE NAME for notifications
        public static final String NOTIFICATIONS_SECTION_TABLE_NAME = "notifications_section";

        //TABLE COLUMNS
        public static final String SECTION_ID = "id";
        public static final String SECTION = "section";

    }

}
