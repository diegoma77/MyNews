package com.example.android.mynews.garbage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.mynews.R;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;

/**
 * Created by Diego Fajardo on 24/03/2018.
 */

public class NotificationsArticleActivity extends AppCompatActivity {

    DatabaseHelper dbH;
    Cursor mCursor;

    TextView tv1;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeating_activity_layout);

        dbH = new DatabaseHelper(this);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME);

        tv1 = (TextView) findViewById(R.id.repeat_act_tv1);
        tv2 = (TextView) findViewById(R.id.repeat_act_tv2);

        mCursor.moveToFirst();
        tv1.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME)));
        mCursor.moveToNext();
        tv2.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.NOTIFICATIONS_SECTION_TABLE_NAME)));


    }
}
