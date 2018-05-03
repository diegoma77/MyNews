package com.example.android.mynews.activities.tests;

import android.database.Cursor;
import android.support.test.rule.ActivityTestRule;

import com.example.android.mynews.activities.MainActivity;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Diego Fajardo on 30/04/2018.
 */

public class DatabaseHelperMethodsTest {

    /**
     * This RULE specifies that this activity is launched
     */

    //Always make this public
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null;

    ArticlesSearchAPIObject articlesSearchAPIObject;

    DatabaseHelper dbH;
    Cursor mCursor;

    @Before
    public void setUp() throws Exception {

        /** With this, we get the context! */
        mActivity = mainActivityActivityTestRule.getActivity();

        //Database
        dbH = new DatabaseHelper(mActivity);

    }

    @Test
    public void testInsertDataToArticlesForSearchArticlesTable() throws Exception {

        //We delete all the rows of the table
        dbH.deleteAllRowsFromTableName(DatabaseContract.Database.ARTICLES_FOR_SEARCH_ARTICLES_TABLE_NAME);
        dbH.resetAutoIncrement(DatabaseContract.Database.ARTICLES_FOR_SEARCH_ARTICLES_TABLE_NAME);

        String testWeb = "testWebUrl";
        String testSnippet = "testSnippet";
        String testImageUrl = "testImageUrl";
        String testNewDesk = "testNewDesk";
        String testPubDate = "testPubDate";

        //We create a test object
        articlesSearchAPIObject = new ArticlesSearchAPIObject(
                testWeb,
                testSnippet,
                testImageUrl,
                testNewDesk,
                testPubDate
        );

        //We check that the insertion in the database returned true
        boolean valueReturned = dbH.insertDataToArticlesForSearchArticlesTable(articlesSearchAPIObject);
        assertTrue(valueReturned);

        //We check that the data (new row) is in every column
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ARTICLES_FOR_SEARCH_ARTICLES_TABLE_NAME);

        //We check what is in the row (every column)
        mCursor.moveToFirst();
        String tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_WEB_URL));
        assertTrue(tempStorage.equals(testWeb));
        tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_SNIPPET));
        assertTrue(tempStorage.equals(testSnippet));
        tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_IMAGE_URL));
        assertTrue(tempStorage.equals(testImageUrl));
        tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_NEW_DESK));
        assertTrue(tempStorage.equals(testNewDesk));
        tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_PUB_DATE));
        assertTrue(tempStorage.equals(testPubDate));

    }

    @Test
    public void testInsertDataToArticlesForNotificationsTable() throws Exception {

        //We delete all the rows of the table
        dbH.deleteAllRowsFromTableName(DatabaseContract.Database.ARTICLES_FOR_NOTIFICATION_TABLE_NAME);
        dbH.resetAutoIncrement(DatabaseContract.Database.ARTICLES_FOR_NOTIFICATION_TABLE_NAME);

        String testWeb = "testWebUrl";
        String testSnippet = "testSnippet";
        String testImageUrl = "testImageUrl";
        String testNewDesk = "testNewDesk";
        String testPubDate = "testPubDate";

        //We create a test object
        articlesSearchAPIObject = new ArticlesSearchAPIObject(
                testWeb,
                testSnippet,
                testImageUrl,
                testNewDesk,
                testPubDate
        );

        //We check that the insertion in the database returned true
        boolean valueReturned = dbH.insertDataToArticlesForNotificationsTable(articlesSearchAPIObject);
        assertTrue(valueReturned);

        //We check that the data (new row) is in every column
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ARTICLES_FOR_NOTIFICATION_TABLE_NAME);

        //We check what is in the row (every column)
        mCursor.moveToFirst();
        String tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_WEB_URL));
        assertTrue(tempStorage.equals(testWeb));
        tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_SNIPPET));
        assertTrue(tempStorage.equals(testSnippet));
        tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_IMAGE_URL));
        assertTrue(tempStorage.equals(testImageUrl));
        tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_NEW_DESK));
        assertTrue(tempStorage.equals(testNewDesk));
        tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SA_PUB_DATE));
        assertTrue(tempStorage.equals(testPubDate));

    }

    @Test
    public void testInsertDataToSearchQueryTable () throws Exception {

        //We delete all the rows of the table
        dbH.deleteAllRowsFromTableName(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);
        dbH.resetAutoIncrement(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);

        //We insert 7 rows of data (1 for query, 6 for sections)
        for (int i = 0; i < 8; i++) {
            dbH.insertDataToSearchQueryTable("");
        }

        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.QUERY_AND_SECTIONS_TABLE_NAME);

        //We check what is in the row
        mCursor.moveToFirst();
        String tempStorage;

        for (int i = 0; i < mCursor.getCount(); i++) {
            tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.QUERY_OR_SECTION));
            assertTrue(tempStorage.equals(""));

            if (i != mCursor.getCount()) {
                mCursor.moveToNext();
            }
        }
    }

    @Test
    public void testInsertDataToAlreadyReadArticlesTable () throws Exception {

        //We delete all the rows of the table
        dbH.deleteAllRowsFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);
        dbH.resetAutoIncrement(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

        //We insert 1 rows of data
        String articleUrl = "article_url";
        dbH.insertDataToAlreadyReadArticlesTable(articleUrl);

        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

        //We check what is in the row
        mCursor.moveToFirst();
        String tempStorage = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.ARTICLE_URL));
        assertTrue(tempStorage.equals(articleUrl));

    }

    @Test
    public void testInsertDataToSwitchTable () throws Exception {

        //We delete all the rows of the table
        dbH.deleteAllRowsFromTableName(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);
        dbH.resetAutoIncrement(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);

        //We insert 1 row of data
        int state = 1;
        dbH.insertDataToSwitchTable(state);

        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);

        //We check what is in the row
        mCursor.moveToFirst();
        int tempStorage = mCursor.getInt(mCursor.getColumnIndex(DatabaseContract.Database.SWITCH_STATE));
        assertTrue(tempStorage == state);

    }

    @Test
    public void testSwitchOnAndOffInDatabase () throws Exception {

        int on = 1;
        int off = 0;
        int tempStorage;

        dbH.setSwitchOnInDatabase();
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);
        mCursor.moveToFirst();
        tempStorage = mCursor.getInt(mCursor.getColumnIndex(DatabaseContract.Database.SWITCH_STATE));
        assertTrue(tempStorage == on);

        dbH.setSwitchOffInDatabase();
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SWITCH_TABLE_NAME);
        mCursor.moveToFirst();
        tempStorage = mCursor.getInt(mCursor.getColumnIndex(DatabaseContract.Database.SWITCH_STATE));
        assertTrue(tempStorage == off);

    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;

    }

}
