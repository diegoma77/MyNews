package com.example.android.mynews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mynews.R;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class SearchArticlesActivity extends AppCompatActivity {

    private Button button_search;

    //Checkboxes
    private CheckBox cb_arts;
    private CheckBox cb_business;
    private CheckBox cb_entrepeneurs;
    private CheckBox cb_politics;
    private CheckBox cb_sports;
    private CheckBox cb_travel;

    //Textviews to check if the value of the variables is the correct one according to checkboxes
    private TextView tv_arts;
    private TextView tv_business;
    private TextView tv_entrepeneurs;
    private TextView tv_politics;
    private TextView tv_sports;
    private TextView tv_travel;

    //Variables for storing changes (related to Chechboxes)
    private boolean arts_checked = false;
    private boolean business_checked = false;
    private boolean entrepeneurs_checked = false;
    private boolean politics_checked = false;
    private boolean sports_checked = false;
    private boolean travel_checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_articles_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Checkboxes
        cb_arts = (CheckBox) findViewById(R.id.search_checkBox_arts);
        cb_business = (CheckBox) findViewById(R.id.search_checkBox_business);
        cb_entrepeneurs = (CheckBox) findViewById(R.id.search_checkBox_entrepeneurs);
        cb_politics = (CheckBox) findViewById(R.id.search_checkBox_politics);
        cb_sports = (CheckBox) findViewById(R.id.search_checkBox_sports);
        cb_travel = (CheckBox) findViewById(R.id.search_checkBox_travel);

        //TextViews
        tv_arts = (TextView) findViewById(R.id.tv_arts);
        tv_business = (TextView) findViewById(R.id.tv_business);
        tv_entrepeneurs = (TextView) findViewById(R.id.tv_entrepeneurs);
        tv_politics = (TextView) findViewById(R.id.tv_politics);
        tv_sports = (TextView) findViewById(R.id.tv_sports);
        tv_travel = (TextView) findViewById(R.id.tv_travel);

        /**


         */

        button_search = (Button) findViewById(R.id.search_button);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_arts.isChecked()) tv_arts.setText("true");
                else tv_arts.setText("false");

                if (business_checked) tv_business.setText("true");
                else tv_business.setText("false");

                if (entrepeneurs_checked) tv_entrepeneurs.setText("true");
                else tv_entrepeneurs.setText("false");

                if (politics_checked) tv_politics.setText("true");
                else tv_politics.setText("false");

                if (sports_checked) tv_sports.setText("true");
                else tv_sports.setText("false");

                if (travel_checked) tv_travel.setText("true");
                else tv_travel.setText("false");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SearchArticlesActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.search_checkBox_arts:
                if (checked) { arts_checked = true; }
                else { arts_checked = false; }
                break;
            case R.id.search_checkBox_business:
                if (checked) { business_checked = true; }
                else { business_checked = false; }
                break;
            case R.id.search_checkBox_entrepeneurs:
                if (checked) { entrepeneurs_checked = true; }
                else { entrepeneurs_checked =  false; }
                break;
            case R.id.search_checkBox_politics:
                if (checked) { politics_checked = true; }
                else { politics_checked = false; }
                break;
            case R.id.search_checkBox_sports:
                if (checked) { sports_checked = true; }
                else { sports_checked = false; }
                break;
            case R.id.search_checkBox_travel:
                if (checked) { travel_checked = true; }
                else { travel_checked = false; }
                break;
            // TODO: Veggie sandwich
        }
    }

}
