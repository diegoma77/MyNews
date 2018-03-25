package com.example.android.mynews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

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

    //TABLE CREATION STATEMENTS
    private static final String CREATE_ARTICLES_READ_TABLE =
            "CREATE TABLE " + DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME
                    + " ("
                    + DatabaseContract.Database.RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DatabaseContract.Database.ARTICLE_URL + " TEXT NOT NULL"
                    + ")";

    private static final String CREATE_NOTIFICATIONS_TABLE =
            "CREATE TABLE " + DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME
            + " ("
            + DatabaseContract.Database.SECTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.Database.SECTION + " TEXT NOT NULL"
            + ")";


    //CREATING the tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //CREATING the tables
        sqLiteDatabase.execSQL(CREATE_ARTICLES_READ_TABLE);
        sqLiteDatabase.execSQL(CREATE_NOTIFICATIONS_TABLE);

    }

    //UPGRADING the tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //On upgrade drop older versions
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME);

        //Create new table
        onCreate(db);

    }

    /**
     * METHOD FOR INSERTING data in Already Read Articles Table
     * */
    public boolean insertDataToAlreadyReadArticlesTable(String article_url) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Database.ARTICLE_URL, article_url);

        long result = db.insert(
                DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME,
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

    /**
     * METHOD FOR INSERTING data in Notifications Sectionf Table
     * */
    public boolean insertDataToNotificationsSectionTable(String section) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Database.SECTION, section);

        long result = db.insert(
                DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME,
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

    /**
     * METHOD FOR GETTING the data from each table
     * */
    public Cursor getAllDataFromTableName(String table_name) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + table_name;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

    /**
     * METHOD FOR DELETING the data from each table
     * */
    public void deleteAllRowsFromTableName (String table_name) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, null, null);

    }

    public void deleteSingleRowFromTableName (String table_name, String key) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                table_name,
                DatabaseContract.Database.SECTION + "=?",
                new String[] {key} );

    }

//    public void deleteAllRows () {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.delete(" \"" + DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME + "\" ", null, null);
//
//    }

    /**
     * METHOD FOR RESETTING AUTOINCREMENT ID from each table
     * */
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
