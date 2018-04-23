package com.example.android.mynews.data;

import android.provider.BaseColumns;

/**
 * Created by Diego Fajardo on 02/03/2018.
 */

public class DatabaseContract {

    public DatabaseContract () {

    }

    public static class Database implements BaseColumns {

        /** READ ARTICLES' TABLE
         * (for keeping track of which have been read)
         * */
        //TABLE NAME for keeping track of the read articles
        public static final String ALREADY_READ_ARTICLES_TABLE_NAME = "articles_read_table";

        //TABLE COLUMNS
        public static final String ARTICLE_ID = "id";
        public static final String ARTICLE_URL = "article_url";

        /** INFORMATION TABLE (for saving the information
         * and pass it to the activity
         * called by the notification
         * */
        //TABLE NAME for information to search articles
        public static final String QUERY_AND_SECTIONS_TABLE_NAME = "query_and_sections_table";

        //TABLE COLUMNS
        public static final String QUERY_OR_SECTION_ID = "id";
        public static final String QUERY_OR_SECTION = "query_or_section";

        /** Table that stores the
         * switch state of NotificationsActivity
         * */
        //TABLE NAME for Switch of notifications
        public static final String NOTIFICATIONS_SWITCH_TABLE_NAME = "switch_table";

        //TABLE COLUMNS
        public static final String SWITCH_ID = "id";
        public static final String SWITCH_STATE = "state";

        // TODO: 23/04/2018 Delete this if needed
        /*****
         * NEW STUFF
         * ****/

        //TABLE NAME for Notifications Activity
        public static final String ARTICLES_FOR_NOTIFICATION_TABLE_NAME = "switch_table";

        //TABLE COLUMNS
        public static final String NOTIF_ARTICLES_ID = "id";
        public static final String WEB_URL = "web_url";
        public static final String SNIPPET = "snippet";
        public static final String IMAGE_URL = "image_url";
        public static final String NEW_DESK = "new_desk";
        public static final String PUB_DATE = "pub_date";

    }

}
