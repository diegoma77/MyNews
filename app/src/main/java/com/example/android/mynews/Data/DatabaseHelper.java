package com.example.android.mynews.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.android.mynews.Data.DatabaseContract;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 02/03/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //LOG helper
    private static final String LOG = "DATABASEHELPER";

    //DATABASE name
    private static final String DATABASE_NAME = "MyNews.db";

    //DATABASE version
    private static final int DATABASE_VERSION = 1;

    //DATABASE HELPER constructor
    public DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    //METHOD to create all tables from the table name
    private String createTableWithName (String nameOfTheTable) {

        return
        "CREATE TABLE " + nameOfTheTable
                + " ("
                + DatabaseContract.Database.RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseContract.Database.SECTION + " TEXT,"
                + DatabaseContract.Database.TITLE + " TEXT,"
                + DatabaseContract.Database.UPDATE_DATE + " TEXT,"
                + DatabaseContract.Database.IMAGE_URL_THUMBNAIL + " TEXT,"
                + DatabaseContract.Database.IMAGE_URL_THUMBLARGE + " TEXT,"
                + DatabaseContract.Database.IMAGE_URL_NORMAL + " TEXT,"
                + DatabaseContract.Database.IMAGE_URL_MEDIUM + " TEXT,"
                + DatabaseContract.Database.IMAGE_URL_SUPERJUMBO + " TEXT,"
                + DatabaseContract.Database.ARTICLE_URL + " TEXT"
                + ")";
    }

    //CREATING the tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //CREATING the tables
        sqLiteDatabase.execSQL(createTableWithName(DatabaseContract.Database.TOP_STORIES_TABLE_NAME));
        sqLiteDatabase.execSQL(createTableWithName(DatabaseContract.Database.MOST_POPULAR_TABLE_NAME));
        sqLiteDatabase.execSQL(createTableWithName(DatabaseContract.Database.BUSINESS_TABLE_NAME));
        sqLiteDatabase.execSQL(createTableWithName(DatabaseContract.Database.SPORTS_TABLE_NAME));

    }

    //UPGRADING the tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //On upgrade drop older versions
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Database.TOP_STORIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Database.MOST_POPULAR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Database.BUSINESS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Database.SPORTS_TABLE_NAME);

        //Create new table
        onCreate(db);

    }

    //METHOD FOR INSERTING data in every table
    public boolean insertDataWithTableName (String table_name, String section, String title, String update_date,
                               String image_url_thumbnail, String image_url_thumblarge,
                               String image_url_normal, String image_url_medium,
                               String image_url_superjumbo,
                               String article_url) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Database.SECTION, section);
        contentValues.put(DatabaseContract.Database.TITLE, title);
        contentValues.put(DatabaseContract.Database.UPDATE_DATE, update_date);
        contentValues.put(DatabaseContract.Database.IMAGE_URL_THUMBNAIL, image_url_thumbnail);
        contentValues.put(DatabaseContract.Database.IMAGE_URL_THUMBLARGE, image_url_thumblarge);
        contentValues.put(DatabaseContract.Database.IMAGE_URL_NORMAL, image_url_normal);
        contentValues.put(DatabaseContract.Database.IMAGE_URL_MEDIUM, image_url_medium);
        contentValues.put(DatabaseContract.Database.IMAGE_URL_SUPERJUMBO, image_url_superjumbo);
        contentValues.put(DatabaseContract.Database.ARTICLE_URL, article_url);

        long result = db.insert(
                table_name,
                null,
                contentValues);

        db.close();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getAllDataFromTableName(String table_name) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + table_name;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

    public void deleteAllRows (String table_name) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(" \" " + table_name + " \" ", null, null);

    }

    public void resetAutoIncrement (String table_name) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + table_name + "'");

    }



    //METHOD USED TO CHECK if a table is empty)
    //Used to avoid the app to insert data when it doesn't have to
    public boolean isTableEmpty(String table_name) {

        boolean flag;
        String quString = "select exists(select 1 from " + table_name + ");";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(quString, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count == 1) {
            flag = false;
        } else {
            flag = true;
        }
        cursor.close();
        db.close();

        return flag;
    }

    //METHOD FOR DATABASE MANAGER --> CAN BE REMOVED
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }



}
