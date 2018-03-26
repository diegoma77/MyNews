package com.example.android.mynews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mynews.R;
import com.example.android.mynews.alertdialog.PickBeginDateDialog;
import com.example.android.mynews.alertdialog.PickEndDateDialog;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class SearchArticlesActivity extends AppCompatActivity implements
        PickBeginDateDialog.PickBeginDateDialogListener,
        PickEndDateDialog.PickEndDateDialogListener {

    private static final String TAG = "SearchArticlesActivity";
    private static final String SQ_TAG = "Search Query";
    private static final String LOF_TAG = "List of sections";
    private static final String BD_TAG = "Begin Date";
    private static final String ED_TAG = "End Date";
    private static final String URL_TAG = "URL Tag";

    //List for sections
    private List<String> listOfSections;

    //ButtonListener Search variable
    private Button buttonSearch;

    //ButtonListener Dates
    private LinearLayout buttonBeginDate;
    private LinearLayout buttonEndDate;

    //Variables for dates
    private String beginDate = "";
    private String endDate = "";
    private TextView tv_beginDateTextView;
    private TextView tv_endDateTextView;

    //TextInput
    private TextInputEditText mTextInputEditText;

    //Checkboxes variables
    private CheckBox cb_arts;
    private CheckBox cb_business;
    private CheckBox cb_entrepreneurs;
    private CheckBox cb_politics;
    private CheckBox cb_sports;
    private CheckBox cb_travel;

    // TODO: 15/03/2018 Delete these variables when they are not used anymore
    // TODO: 17/03/2018 Delete the TextViews in search_articles_layout.xml
    //TextViews to check if the value of the variables is the correct one according to checkboxes
    private TextView tv_arts;
    private TextView tv_business;
    private TextView tv_entrepreneurs;
    private TextView tv_politics;
    private TextView tv_sports;
    private TextView tv_travel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_articles_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //List
        listOfSections = new ArrayList<>();

        buttonSearch = (Button) findViewById(R.id.search_button);
        buttonBeginDate = (LinearLayout) findViewById(R.id.search_button_beginDate);
        buttonEndDate = (LinearLayout) findViewById(R.id.search_button_endDate);

        //TextInputEditText
        mTextInputEditText = (TextInputEditText) findViewById(R.id.search_text_input_edit_text);

        //Checkboxes
        cb_arts = (CheckBox) findViewById(R.id.search_checkBox_arts);
        cb_business = (CheckBox) findViewById(R.id.search_checkBox_business);
        cb_entrepreneurs = (CheckBox) findViewById(R.id.search_checkBox_entrepeneurs);
        cb_politics = (CheckBox) findViewById(R.id.search_checkBox_politics);
        cb_sports = (CheckBox) findViewById(R.id.search_checkBox_sports);
        cb_travel = (CheckBox) findViewById(R.id.search_checkBox_travel);

        //Text inside datePicker buttons
        tv_beginDateTextView = (TextView) findViewById(R.id.search_beginDate_date);
        tv_endDateTextView = (TextView) findViewById(R.id.search_endDate_date);

        //TextViews
        tv_arts = (TextView) findViewById(R.id.tv_arts);
        tv_business = (TextView) findViewById(R.id.tv_business);
        tv_entrepreneurs = (TextView) findViewById(R.id.tv_entrepeneurs);
        tv_politics = (TextView) findViewById(R.id.tv_politics);
        tv_sports = (TextView) findViewById(R.id.tv_sports);
        tv_travel = (TextView) findViewById(R.id.tv_travel);

        // TODO: 17/03/2018 BeginDate cannot be higher than EndDate and otherwise. Can be done passing a Date to openXDialog and modifying the constructor
        // TODO: 17/03/2018 so it will modify a property of the class and this one modify the date options of the datePicker

        /** Listeners */

        //BeginDate ButtonListener onClick
        buttonBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBeginDialog();
            }
        });

        //EndDate ButtonListener onClick
        buttonEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEndDialog();
            }
        });

        /**
         * Search button sends the JSONRequest using sendJSONRequestMethod.
         * It uses the url obtained through 2 methods and 2 variables:
         * getSearchArticlesUrl(
         * getSearchQueryAndAdaptForUrl(method),
         * getNewDeskValuesAndAdaptForUrl(method),
         * beginDate,
         * endDate)
         * */
        // TODO: 16/03/2018 Delete part of the code related to textViews that are not necessary
        //Search ButtonListener onClick
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the checkbox is checked, check that there is an elements (String) with that name in the list
                //if there is any, set that name to the text view

                //ARTS
                if (cb_arts.isChecked()){
                    if (listOfSections.contains(Keys.CheckboxFields.CB_ARTS)) {
                        tv_arts.setText(listOfSections.get((listOfSections.indexOf(Keys.CheckboxFields.CB_ARTS))));
                    }
                }
                else tv_arts.setText("false");

                //BUSINESS
                if (cb_business.isChecked()){
                    if (listOfSections.contains(Keys.CheckboxFields.CB_BUSINESS)) {
                        tv_business.setText(listOfSections.get((listOfSections.indexOf(Keys.CheckboxFields.CB_BUSINESS))));
                    }
                }
                else tv_business.setText("false");

                //CB_ENTREPRENEURS
                if (cb_entrepreneurs.isChecked()){
                    if (listOfSections.contains(Keys.CheckboxFields.CB_ENTREPRENEURS)) {
                        tv_entrepreneurs.setText(listOfSections.get((listOfSections.indexOf(Keys.CheckboxFields.CB_ENTREPRENEURS))));
                    }
                }
                else tv_entrepreneurs.setText("false");

                //POLITICS
                if (cb_politics.isChecked()){
                    if (listOfSections.contains(Keys.CheckboxFields.CB_POLITICS)) {
                        tv_politics.setText(listOfSections.get((listOfSections.indexOf(Keys.CheckboxFields.CB_POLITICS))));
                    }
                }
                else tv_politics.setText("false");

                //SPORTS
                if (cb_sports.isChecked()){
                    if (listOfSections.contains(Keys.CheckboxFields.CB_SPORTS)) {
                        tv_sports.setText(listOfSections.get((listOfSections.indexOf(Keys.CheckboxFields.CB_SPORTS))));
                    }
                }
                else tv_sports.setText("false");

                //TRAVEL
                if (cb_travel.isChecked()){
                    if (listOfSections.contains(Keys.CheckboxFields.CB_TRAVEL)) {
                        tv_travel.setText(listOfSections.get((listOfSections.indexOf(Keys.CheckboxFields.CB_TRAVEL))));
                    }
                }
                else tv_travel.setText("false");


                if (mTextInputEditText.getText().toString().equals("")) { Log.i(SQ_TAG, "searchQuery is EMPTY"); }
                else { Log.i(SQ_TAG, mTextInputEditText.getText().toString()); }

                Log.i(LOF_TAG, "size -> " + listOfSections.size() + "");
                for (int i = 0; i < listOfSections.size(); i++) {
                    Log.i(LOF_TAG, listOfSections.get(i));
                }
                Log.i(LOF_TAG, getNewDeskValuesAndAdaptForUrl(listOfSections));

                if (beginDate == "") Log.i(BD_TAG, "is EMPTY");
                else { Log.i(BD_TAG, beginDate); }

                if (endDate == "") Log.i(ED_TAG, "is EMPTY");
                else { Log.i(ED_TAG, endDate); }

                Log.i(TAG, getSearchArticlesUrl(
                        getSearchQueryAndAdaptForUrl(),
                        getNewDeskValuesAndAdaptForUrl(listOfSections),
                        beginDate,
                        endDate,
                        Url.ArticleSearchUrl.PAGE_ONE));

                //At least one checkbox must be checked
                if (!cb_arts.isChecked()
                        && !cb_business.isChecked()
                        && !cb_entrepreneurs.isChecked()
                        && !cb_politics.isChecked()
                        && !cb_sports.isChecked()
                        && !cb_travel.isChecked()) {

                    Toast.makeText(SearchArticlesActivity.this, "You have to choose at least one category", Toast.LENGTH_SHORT).show();
                }
                else {
                    //We call the intent to change activity. This method calls the necessary method for building the URL in the next activity
                    createIntentForDisplayingSearchArticlesActivity();
                }
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

    /** This method is used to know
     * if a checkbox is checked or not*/
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

    /**
     * This method builds the Url used to send the JSON request
     * using the strings created (modified) by other methods
     * */
    public String getSearchArticlesUrl (String searchQuery, String newsSearchQuery, String beginDate, String endDate, String page) {

        /** Seems that there are faster ways than += to append Strings, like StringBuffer */

        String searchArticleUrl = Url.ArticleSearchUrl.BASE_URL;

        if (!searchQuery.equals("")) {
            searchArticleUrl += Url.ArticleSearchUrl.Q
                    + Url.GeneralTokens.EQUAL
                    + searchQuery
                    + Url.GeneralTokens.AMPERSAND;
        }

        if (!newsSearchQuery.equals("")) {
            searchArticleUrl += Url.ArticleSearchUrl.FQ
                    + Url.GeneralTokens.EQUAL
                    + newsSearchQuery
                    + Url.GeneralTokens.AMPERSAND;
        }

        if (!beginDate.equals("")) {
            searchArticleUrl += Url.ArticleSearchUrl.BEGIN_DATE
                    + Url.GeneralTokens.EQUAL
                    + beginDate
                    + Url.GeneralTokens.AMPERSAND;
        }

        if (!endDate.equals("")) {
            searchArticleUrl += Url.ArticleSearchUrl.END_DATE
                    + Url.GeneralTokens.EQUAL
                    + endDate
                    + Url.GeneralTokens.AMPERSAND;
        }

        searchArticleUrl += Url.ArticleSearchUrl.SORT_NEWEST + Url.GeneralTokens.AMPERSAND
                + Url.ArticleSearchUrl.PAGE + Url.GeneralTokens.EQUAL + page + Url.GeneralTokens.AMPERSAND
                + Url.ArticleSearchUrl.API_KEY;

        return searchArticleUrl;

    }

    /**
     * This method is used when the SEARCH BUTTON is clicked.
     * It starts the process of searching for the articles according to the information
     * needed from the user.
     * Changes spaces for + symbols.
     * */
    private String getSearchQueryAndAdaptForUrl () {

        String searchQueryAdaptedForUrl = mTextInputEditText.getText().toString();

        if (!searchQueryAdaptedForUrl.equals("")) {
            searchQueryAdaptedForUrl = searchQueryAdaptedForUrl.trim();
            searchQueryAdaptedForUrl = searchQueryAdaptedForUrl.toLowerCase().replace(" ", "+");
        }

        return searchQueryAdaptedForUrl;
    }

    /**
     * This method us used to build the news_desk part of the Url.
     * The news_desk part is related to the checkboxes and allows to filter the
     * searches according to the category
     * */
    private String getNewDeskValuesAndAdaptForUrl(List<String> listOfSections) {

        String temporary_query;
        String news_desk_query = "";

        for (int i = 0; i < listOfSections.size() ; i++) {

            if (i==0) { temporary_query = listOfSections.get(i); }
            else { temporary_query = "+" + listOfSections.get(i); }

            Log.i("NewDeskLOF.size()", this.listOfSections.size() + "");

            news_desk_query += temporary_query;
        }
        return news_desk_query;
    }

    /** Next six methods are used for the dialogs to be shown
     * when begin date and end date buttons are clicked */
    public void openBeginDialog() {
        PickBeginDateDialog dialog = new PickBeginDateDialog();
        dialog.show(getSupportFragmentManager(), "Date Picker Begin Dialog");

    }

    public void openEndDialog () {
        PickEndDateDialog dialog = new PickEndDateDialog();
        dialog.show(getSupportFragmentManager(), "Date Picker End Dialog");
    }


    @Override
    public void onBeginSubmitClicker(String selectedDateForTextView, String selectedDateForUrl) {
        tv_beginDateTextView.setText(selectedDateForTextView);
        beginDate = selectedDateForUrl;
    }

    @Override
    public void onBeginCancelClicker(String string) {
        Toast.makeText(SearchArticlesActivity.this, "No date was selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEndSubmitClicker(String selectedDateForTextView, String selectedDateForUrl) {
        tv_endDateTextView.setText(selectedDateForTextView);
        endDate = selectedDateForUrl;
    }

    @Override
    public void onEndCancelClicker(String cancelMessage) {
        Toast.makeText(SearchArticlesActivity.this, "No date was selected", Toast.LENGTH_SHORT).show();
    }

    /**This method is used to call the Intent to change
     * the Activity displayed (to DisplaySearchArticlesActivity). The intent
     * carries information about the urls than have to be displayed*/
    private void createIntentForDisplayingSearchArticlesActivity() {

        Intent intent = new Intent(SearchArticlesActivity.this, DisplaySearchArticlesActivity.class);
        intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE1, getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_ONE));

        intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE2, getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_TWO));

        intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE3, getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_THREE));

        startActivity(intent);

    }




}

