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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_articles_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        cb_arts = (CheckBox) findViewById(R.id.search_checkBox_arts);
        cb_business = (CheckBox) findViewById(R.id.search_checkBox_business);
        cb_entrepeneurs = (CheckBox) findViewById(R.id.search_checkBox_entrepeneurs);
        cb_politics = (CheckBox) findViewById(R.id.search_checkBox_politics);
        cb_sports = (CheckBox) findViewById(R.id.search_checkBox_sports);
        cb_travel = (CheckBox) findViewById(R.id.search_checkBox_travel);

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
                if (checked) { Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show(); }
                // Put some meat on the sandwich
            else { Toast.makeText(this, "Not Checked", Toast.LENGTH_SHORT).show(); }
                // Remove the meat
                break;
            case R.id.search_checkBox_business:
                if (checked) { Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show(); }
                // Put some meat on the sandwich
            else
                // Remove the meat
                break;
            case R.id.search_checkBox_entrepeneurs:
                if (checked) { Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show(); }
                // Put some meat on the sandwich
            else
                // Remove the meat
                break;
            case R.id.search_checkBox_politics:
                if (checked) { Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show(); }
                // Put some meat on the sandwich
                else
                    // Remove the meat
                    break;
            case R.id.search_checkBox_sports:
                if (checked) { Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show(); }
                // Put some meat on the sandwich
                else
                    // Remove the meat
                    break;
            case R.id.search_checkBox_travel:
                if (checked) { Toast.makeText(this, "Checked", Toast.LENGTH_SHORT).show(); }
                // Put some meat on the sandwich
                else
                    // Remove the meat
                    break;
            // TODO: Veggie sandwich
        }
    }

}
