package com.example.android.mynews.broadcastreceiver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.mynews.R;

/**
 * Created by Diego Fajardo on 24/03/2018.
 */

// TODO: 24/03/2018 Convert this activity into DisplayNotificationsSearch
public class RepeatingActivity extends AppCompatActivity {

    private int sizeOfListSections;

    private TextView tv1;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeating_activity_layout);

        tv1 = findViewById(R.id.repeat_act_tv1);
        tv2 = findViewById(R.id.repeat_act_tv2);

        Intent intent = getIntent();

        tv1.setText(sizeOfListSections + " / " + intent.getExtras().getString("ListOfSections0"));
        tv2.setText(intent.getExtras().getString("ListOfSections1"));

    }
}
