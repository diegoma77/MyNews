package com.example.android.mynews.data;

import android.provider.BaseColumns;

/**
 * Created by Diego Fajardo on 02/03/2018.
 */

/** DATABASE CONTRACT
 * */
public class DatabaseContract {

    public DatabaseContract () {

    }

    public static class Database implements BaseColumns {

        /** READ ARTICLES' TABLE
         * Table that stores the urls of those articles
         * that have been already read
         * */
        //TABLE NAME for keeping track of the read articles
        public static final String ALREADY_READ_ARTICLES_TABLE_NAME = "articles_read_table";

        //TABLE COLUMNS
        public static final String ARTICLE_ID = "id";
        public static final String ARTICLE_URL = "article_url";

        /** QUERY AND SECTIONS TABLE
         * Stores the information inputted in NotificationsActivity.
         * It will be used when the notification is created
         * */
        //TABLE NAME for information to search articles
        public static final String QUERY_AND_SECTIONS_TABLE_NAME = "query_and_sections_table";

        //TABLE COLUMNS
        public static final String QUERY_OR_SECTION_ID = "id";
        public static final String QUERY_OR_SECTION = "query_or_section";

        /** SWITCH TABLE
         * Table that stores the
         * switch state of NotificationsActivity
         * (we can also used SharedPreferences)
         * */
        //TABLE NAME for Switch of notifications
        public static final String NOTIFICATIONS_SWITCH_TABLE_NAME = "switch_table";

        //TABLE COLUMNS
        public static final String SWITCH_ID = "id";
        public static final String SWITCH_STATE = "state";

        /** ARTICLES FOR SEARCH ARTICLES TABLE
         * Table that stores the articles found when doing an API request to
         * Articles Search API in SearchArticlesActivity
         * */
        //TABLE NAME for Notifications Activity
        public static final String ARTICLES_FOR_SEARCH_ARTICLES_TABLE_NAME = "articles_for_search_articles";

        //TABLE COLUMNS
        public static final String SA_ARTICLES_ID = "id";
        public static final String SA_WEB_URL = "web_url";
        public static final String SA_SNIPPET = "snippet";
        public static final String SA_IMAGE_URL = "image_url";
        public static final String SA_NEW_DESK = "new_desk";
        public static final String SA_PUB_DATE = "pub_date";

        /** ARTICLES FOR NOTIFICATIONS TABLE
         * Table that stores the articles found when doing an API request to
         * Articles Search API using Notifications functionality
         * */
        //TABLE NAME for Notifications Activity
        public static final String ARTICLES_FOR_NOTIFICATION_TABLE_NAME = "articles_for_notifications";

        //TABLE COLUMNS
        public static final String NOTIF_ARTICLES_ID = "id";
        public static final String NOTIF_WEB_URL = "web_url";
        public static final String NOTIF_SNIPPET = "snippet";
        public static final String NOTIF_IMAGE_URL = "image_url";
        public static final String NOTIF_NEW_DESK = "new_desk";
        public static final String NOTIF_PUB_DATE = "pub_date";

    }

}
