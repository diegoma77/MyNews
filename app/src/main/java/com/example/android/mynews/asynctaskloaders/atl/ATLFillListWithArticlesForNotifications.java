package com.example.android.mynews.asynctaskloaders.atl;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/04/2018.
 */

/** This ATL fills a list with all the information from the table
 * Articles for notification. Additionally, it deletes from the list
 * all the articles that have been read so the user won't see them
 * as new articles
 * */
public class ATLFillListWithArticlesForNotifications extends AsyncTaskLoader<List<ArticlesSearchAPIObject>> {

    private DatabaseHelper dbH;
    private Cursor cursorNotificationTable;
    private Cursor cursorReadArticlesTable;

    public ATLFillListWithArticlesForNotifications(
            Context context) {
        super(context);
        dbH = new DatabaseHelper(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ArticlesSearchAPIObject> loadInBackground() {

        List<ArticlesSearchAPIObject> listOfObjects = new ArrayList<>();

        if (!dbH.isTableEmpty(DatabaseContract.Database.ARTICLES_FOR_NOTIFICATION_TABLE_NAME)){

            cursorNotificationTable = dbH.getAllDataFromTableName(DatabaseContract.Database.ARTICLES_FOR_NOTIFICATION_TABLE_NAME);
            cursorNotificationTable.moveToFirst();

            for (int i = 0; i < cursorNotificationTable.getCount(); i++) {

                ArticlesSearchAPIObject object = new ArticlesSearchAPIObject();

                object.setWebUrl(cursorNotificationTable.getString(cursorNotificationTable.getColumnIndex(DatabaseContract.Database.SA_WEB_URL)));
                object.setSnippet(cursorNotificationTable.getString(cursorNotificationTable.getColumnIndex(DatabaseContract.Database.SA_SNIPPET)));
                object.setImageUrl(cursorNotificationTable.getString(cursorNotificationTable.getColumnIndex(DatabaseContract.Database.SA_IMAGE_URL)));
                object.setNewDesk(cursorNotificationTable.getString(cursorNotificationTable.getColumnIndex(DatabaseContract.Database.SA_NEW_DESK)));
                object.setPubDate(cursorNotificationTable.getString(cursorNotificationTable.getColumnIndex(DatabaseContract.Database.SA_PUB_DATE)));

                listOfObjects.add(object);

                if (i != cursorNotificationTable.getCount()) {
                    cursorNotificationTable.moveToNext();
                }
            }

            /** If the list has at least one item and
             * the Articles Read Database is not empty, then...
             * */
            if ((!dbH.isTableEmpty(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME) && (listOfObjects.size()!= 0))){

                cursorReadArticlesTable = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

                List<String> listOfReadArticlesUrls = new ArrayList<>();

                cursorReadArticlesTable.moveToFirst();

                /** We fill a list with the urls of the read articles
                 * */
                for (int i = 0; i < cursorReadArticlesTable.getCount(); i++) {

                    listOfReadArticlesUrls.add(cursorReadArticlesTable.getString(cursorReadArticlesTable.getColumnIndex(DatabaseContract.Database.ARTICLE_URL)));

                    if(i != cursorReadArticlesTable.getCount()) {
                        cursorReadArticlesTable.moveToNext();
                    }

                }

                /** We check if the article has been read.
                 * If it has been read, we remove it.
                 * */
                for (int i = 0; i < listOfObjects.size(); i++) {

                    for (int j = 0; j < listOfReadArticlesUrls.size(); j++) {

                        if (listOfObjects.get(i).getWebUrl().equals(listOfReadArticlesUrls.get(j))){
                            listOfObjects.remove(i);
                            i = 0;
                        }
                    }
                }

            }
        }

        if (listOfObjects.size() != 0) {
            return listOfObjects;
        } else {
            return null; }

    }
}
