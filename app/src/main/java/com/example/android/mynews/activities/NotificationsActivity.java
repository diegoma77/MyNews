package com.example.android.mynews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.mynews.R;
import com.example.android.mynews.extras.Keys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 26/02/2018.
 */

public class NotificationsActivity extends AppCompatActivity {

    //List for sections
    private List<String> listOfSections;

    //TextInput
    private TextInputEditText mTextInputEditText;

    //Switch
    private Switch mSwitch;

    //Checkboxes variables
    private CheckBox cb_arts;
    private CheckBox cb_business;
    private CheckBox cb_entrepreneurs;
    private CheckBox cb_politics;
    private CheckBox cb_sports;
    private CheckBox cb_travel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.notif_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //List
        listOfSections = new ArrayList<>();

        //TextInputEditText
        mTextInputEditText = (TextInputEditText) findViewById(R.id.search_text_input_edit_text);

        //Switch
        mSwitch = (Switch) findViewById(R.id.notif_switch);

        //Checkboxes
        cb_arts = (CheckBox) findViewById(R.id.search_checkBox_arts);
        cb_business = (CheckBox) findViewById(R.id.search_checkBox_business);
        cb_entrepreneurs = (CheckBox) findViewById(R.id.search_checkBox_entrepeneurs);
        cb_politics = (CheckBox) findViewById(R.id.search_checkBox_politics);
        cb_sports = (CheckBox) findViewById(R.id.search_checkBox_sports);
        cb_travel = (CheckBox) findViewById(R.id.search_checkBox_travel);


        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSwitch.isChecked()) {
                    Toast.makeText(NotificationsActivity.this, "Now switch is on", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(NotificationsActivity.this, "Now switch is off", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(NotificationsActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: 21/03/2018 At least one checkbox must be selected
    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.search_checkBox_arts:
                ///When checked, add a String with the name of the checkbox to the list
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_ARTS); }
                //When unchecked, check if there is an element with the name of the Checkbox in the array
                //if there is one, remove it from the list
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_ARTS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_ARTS));
                }
                break;

            case R.id.search_checkBox_business:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_BUSINESS); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_BUSINESS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_BUSINESS));
                }
                break;

            case R.id.search_checkBox_entrepeneurs:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_ENTREPRENEURS); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_ENTREPRENEURS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_ENTREPRENEURS));
                }
                break;

            case R.id.search_checkBox_politics:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_POLITICS); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_POLITICS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_POLITICS));
                }
                break;

            case R.id.search_checkBox_sports:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_SPORTS); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_SPORTS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_SPORTS));
                }
                break;

            case R.id.search_checkBox_travel:
                if (checked) { listOfSections.add(Keys.CheckboxFields.CB_TRAVEL); }
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_TRAVEL))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_TRAVEL));
                }
                break;
        }
    }


}
