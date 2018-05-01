package com.example.android.mynews.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.mynews.R;
import com.example.android.mynews.asynctaskloaders.atl.atlrequest.ATLSearchArticlesAPIRequestAndFillArticlesForSearchArticlesTable;
import com.example.android.mynews.extras.helperclasses.ToastHelper;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;
import com.example.android.mynews.extras.interfaceswithconstants.Url;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

/** Activity that allows the user to search articles according to different
 * criteria. The user can insert a query and check different checkboxes.
 * When the user clicks the Search Button, the search begins. If the search
 * is successful, the user will be brought to next Activity. If not, a toast
 * will be displayed indicating the user no articles were found.
 * */
public class SearchArticlesActivity extends AppCompatActivity {

    //Tag
    private static final String TAG = "SearchArticlesActivityT";

    //Loader ID
    private static final int LOADER_ARTICLES_SEARCH_API_REQUEST = 33;

    //List for sections
    private List<String> listOfSections;

    //ButtonListener Search variable
    private Button buttonSearch;

    //ButtonListener Dates
    private LinearLayout buttonBeginDate;
    private LinearLayout buttonEndDate;

    //Variables for dates
    private TextView tv_beginDateTextView;
    private TextView tv_endDateTextView;

    //Calendars to control dates
    Calendar calendarNow;
    Calendar calendarBeginDate;
    Calendar calendarEndDate;

    //Strings with beginDate and endDate
    String beginDate = "";
    String endDate = "";

    //Date Picker Dialog variables
    private android.app.DatePickerDialog.OnDateSetListener mBeginDateSetListener;
    private android.app.DatePickerDialog.OnDateSetListener mEndDateSetListener;

    //TextInput
    private TextInputEditText mTextInputEditText;

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
        setContentView(R.layout.search_articles_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeActionContentDescription(R.string.home_button_search_activity_content_description);

        /** Instantiating Calendars (the order is important because is related to the
         * current system time and endDate must be after beginDate always) */
        calendarBeginDate = Calendar.getInstance();
        calendarBeginDate.add(Calendar.DATE, -100 * 365); //beginDate is 100 years ago
        calendarEndDate = Calendar.getInstance();
        calendarNow = Calendar.getInstance();

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

        /********************
         * LISTENERS ********
         * *****************/

        /**** ONCLICK ******/

        //BeginDate ButtonListener onClick
        buttonBeginDate.setOnClickListener(listenerBeginDate);

        //EndDate ButtonListener onClick
        buttonEndDate.setOnClickListener(listenerEndDate);

        //Search ButtonListener onClick
        buttonSearch.setOnClickListener(listenerButtonSearch);


        /**** ON DATE SET ******/
        /** Used to get the information the user inputted in the dialog
         * */

        mBeginDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendarBeginDate.set(year,month,dayOfMonth);

                beginDate = null;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (calendarBeginDate != null){
                    beginDate = sdf.format(calendarBeginDate.getTime());
                }

                tv_beginDateTextView.setText(beginDate);

            }
        };

        mEndDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendarEndDate.set(year,month,dayOfMonth);

                endDate = null;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (calendarEndDate != null){
                    endDate = sdf.format(calendarEndDate.getTime());
                }

                tv_endDateTextView.setText(endDate);

            }
        };
    }

    /*****************************
     * BUTTON ONCLICK LISTENERS **
     ****************************/

    /** Dialog creation to set begin date
     * */
    View.OnClickListener listenerBeginDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(
                    SearchArticlesActivity.this,
                    mBeginDateSetListener,
                    year, month, day);
            dialog.show();

        }
    };

    /** Dialog creation to set end date
     * */
    View.OnClickListener listenerEndDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(
                    SearchArticlesActivity.this,
                    mEndDateSetListener,
                    year, month, day);
            dialog.show();

        }
    };

    /** Listener for when Search Button is clicked
     * */
    View.OnClickListener listenerButtonSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /** At least one checkbox must be checked,
             * otherwise we cannot proceed to the Search
             * */
            if (!cb_arts.isChecked()
                    && !cb_business.isChecked()
                    && !cb_entrepreneurs.isChecked()
                    && !cb_politics.isChecked()
                    && !cb_sports.isChecked()
                    && !cb_travel.isChecked()) {

                ToastHelper.toastShort(
                        SearchArticlesActivity.this,
                        getResources().getString(R.string.search_articles_toast_choose_one_category));
                hideKeyboard(SearchArticlesActivity.this);

                /** The endDate must be
                 * after beginDate
                 * */
            } else if (calendarEndDate.getTimeInMillis() < calendarBeginDate.getTimeInMillis()){

                Log.i(TAG, "onClick: endDate must be after beginDate");

                ToastHelper.toastShort(
                        SearchArticlesActivity.this,
                        getResources().getString(R.string.search_articles_toast_end_and_begin_dates));
                hideKeyboard(SearchArticlesActivity.this);

                /** The endDate must not be
                 * after today
                 * */
            } else if (calendarEndDate.getTimeInMillis() > calendarNow.getTimeInMillis()){

                Log.i(TAG, "onClick: endDate must not be after today");

                ToastHelper.toastShort(
                        SearchArticlesActivity.this,
                        getResources().getString(R.string.search_articles_toast_end_and_today_dates));
                hideKeyboard(SearchArticlesActivity.this);

                /** If the rest of if and if else statements does not
                 * run, then everything is fine and we can start
                 * the API Request
                 * */
            } else {

                Log.i(TAG, "onClick last else beginDate: " + beginDate);
                Log.i(TAG, "onClick last else endDate: " + endDate);
                hideKeyboard(SearchArticlesActivity.this);

                //We start the API Request
                loadLoaderArticlesSearchAPIRequest(LOADER_ARTICLES_SEARCH_API_REQUEST);
            }

        }
    };

    /*********************
     * MENU OPTIONS ******
     ********************/

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

    @Override
    protected void onPause() {
        super.onPause();
    }

    /*************************
     * ON CHECKBOX CLICKED ***
     ************************/

    /**
     * This method is used to know
     * if a checkbox is checked or not
     */
    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.search_checkBox_arts:
                ///When checked, add a String with the name of the checkbox to the list
                if (checked) {
                    listOfSections.add(Keys.CheckboxFields.CB_ARTS);
                }
                //When unchecked, check if there is an element with the name of the Checkbox in the array
                //if there is one, remove it from the list
                else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_ARTS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_ARTS));
                }
                break;

            case R.id.search_checkBox_business:
                if (checked) {
                    listOfSections.add(Keys.CheckboxFields.CB_BUSINESS);
                } else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_BUSINESS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_BUSINESS));
                }
                break;

            case R.id.search_checkBox_entrepeneurs:
                if (checked) {
                    listOfSections.add(Keys.CheckboxFields.CB_ENTREPRENEURS);
                } else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_ENTREPRENEURS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_ENTREPRENEURS));
                }
                break;

            case R.id.search_checkBox_politics:
                if (checked) {
                    listOfSections.add(Keys.CheckboxFields.CB_POLITICS);
                } else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_POLITICS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_POLITICS));
                }
                break;

            case R.id.search_checkBox_sports:
                if (checked) {
                    listOfSections.add(Keys.CheckboxFields.CB_SPORTS);
                } else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_SPORTS))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_SPORTS));
                }
                break;

            case R.id.search_checkBox_travel:
                if (checked) {
                    listOfSections.add(Keys.CheckboxFields.CB_TRAVEL);
                } else {
                    if (listOfSections.contains(Keys.CheckboxFields.CB_TRAVEL))
                        listOfSections.remove(listOfSections.indexOf(Keys.CheckboxFields.CB_TRAVEL));
                }
                break;
        }
    }


    /**
     * This method builds the Url used to send the JSON request
     * using the strings created (modified) by other methods
     */
    public String getSearchArticlesUrl(String searchQuery, String newsSearchQuery, String beginDate, String endDate, String page) {

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
     */
    public String getSearchQueryAndAdaptForUrl() {

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
     */
    public String getNewDeskValuesAndAdaptForUrl(List<String> listOfSections) {

        String temporary_query;
        String news_desk_query = "";

        for (int i = 0; i < listOfSections.size(); i++) {

            if (i == 0) {
                temporary_query = listOfSections.get(i);
            } else {
                temporary_query = "+" + listOfSections.get(i);
            }

            Log.i("NewDeskLOF.size()", this.listOfSections.size() + "");

            news_desk_query += temporary_query;
        }
        return news_desk_query;
    }

    /** Method used to create the Lists of urls that will be used
     * by the loaders to do the API requests
     */
    private List<String> createListOfUrls() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (calendarBeginDate != null){
            beginDate = sdf.format(calendarBeginDate.getTime());
        }

        if (calendarEndDate != null){
            endDate = sdf.format(calendarEndDate.getTime());
        }

        List <String> listOfUrls = new ArrayList<>();

        listOfUrls.add(getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_ONE));

        listOfUrls.add(getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_TWO));

        listOfUrls.add(getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_THREE));

        Log.i(TAG, "createListOfUrls: " + listOfUrls.get(0));
        Log.i(TAG, "createListOfUrls: " + listOfUrls.get(1));
        Log.i(TAG, "createListOfUrls: " + listOfUrls.get(2));

        return listOfUrls;

    }

    /** Method that hides the keyboard
     * */
    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /** Method that shows the progress information
     */
    private void showProgressInfo() {

        LinearLayout layout = findViewById(R.id.progress_info);
        layout.setVisibility(View.VISIBLE);

    }

    /** Method that hides the progress information
     */
    private void hideProgressInfo() {

        LinearLayout layout = findViewById(R.id.progress_info);
        layout.setVisibility(View.INVISIBLE);

    }

    /** METHOD that returns the listOfQueryAndSections
     * (test purposes)
     * */
    public List<String> getListOfSections() {
        return listOfSections;
    }


    /**************************
     *** LOADERS **************
     **************************/

    private void loadLoaderArticlesSearchAPIRequest(int id) {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<ArticlesSearchAPIObject>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderUpdateList: ");
            loaderManager.initLoader(id, null, loaderArticlesSearchAPIRequest);
        } else {
            Log.i(TAG, "loadLoaderUpdateList: ");
            loaderManager.restartLoader(id, null, loaderArticlesSearchAPIRequest);
        }
    }

    /**************************
     *** LOADER CALLBACKS *****
     **************************/

    private LoaderManager.LoaderCallbacks<List<ArticlesSearchAPIObject>> loaderArticlesSearchAPIRequest =
            new LoaderManager.LoaderCallbacks<List<ArticlesSearchAPIObject>>() {

                @Override
                public Loader<List<ArticlesSearchAPIObject>> onCreateLoader(int id, Bundle args) {
                    Log.i(TAG, "onCreateLoader: called! +++");
                    showProgressInfo();
                    return new ATLSearchArticlesAPIRequestAndFillArticlesForSearchArticlesTable(SearchArticlesActivity.this, createListOfUrls());
                }

                @Override
                public void onLoadFinished(Loader<List<ArticlesSearchAPIObject>> loader, List<ArticlesSearchAPIObject> data) {
                    Log.i(TAG, "onLoadFinished: called! +++");

                    if (data.size() != 0) {
                        Intent intent = new Intent(SearchArticlesActivity.this, DisplaySearchArticlesActivity.class);
                        startActivity(intent);

                    } else {
                        ToastHelper.toastShort(SearchArticlesActivity.this, "No articles were found");
                        hideProgressInfo();
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<ArticlesSearchAPIObject>> loader) {

                }
            };

}

