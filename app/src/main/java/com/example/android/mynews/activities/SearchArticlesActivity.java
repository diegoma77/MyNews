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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mynews.R;
import com.example.android.mynews.extras.helperclasses.DateHelper;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.extras.Url;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class SearchArticlesActivity extends AppCompatActivity {

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

    //Variables used to check that dates are consistent
    private int beginYear;
    private int beginMonth;
    private int beginDay;
    private int endYear;
    private int endMonth;
    private int endDay;

    //DateHelper
    private DateHelper dH;

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

        dH = new DateHelper();

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

        /** Date Listeners */

        //BeginDate ButtonListener onClick
        buttonBeginDate.setOnClickListener(new View.OnClickListener() {
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
        });

        mBeginDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                tv_beginDateTextView.setText(getDateFromDatePicker(year,month,dayOfMonth, true));
                beginDate = getDateFromDatePicker(year, month, dayOfMonth, false);

                beginYear = year;
                beginMonth = month + 1;
                beginDay = dayOfMonth;
            }
        };

        //EndDate ButtonListener onClick
        buttonEndDate.setOnClickListener(new View.OnClickListener() {
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
        });

        mEndDateSetListener = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                tv_endDateTextView.setText(getDateFromDatePicker(year,month,dayOfMonth, true));
                endDate = getDateFromDatePicker(year,month,dayOfMonth, false);

                endYear = year;
                endMonth = month + 1;
                endDay = dayOfMonth;

            }
        };

        //Search ButtonListener onClick
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //At least one checkbox must be checked
                if (!cb_arts.isChecked()
                        && !cb_business.isChecked()
                        && !cb_entrepreneurs.isChecked()
                        && !cb_politics.isChecked()
                        && !cb_sports.isChecked()
                        && !cb_travel.isChecked()) {
                    
                    Toast.makeText(SearchArticlesActivity.this,
                            getResources().getString(R.string.search_articles_toast_choose_one_category),
                            Toast.LENGTH_SHORT)
                            .show();
                } else if (beginDate.equals("") && endDate.equals("")) {

                    endDate = dH.getTodayDateAndConvertToString();
                    beginDate = dH.getOneMonthAgoDateAndConvertToString();

                    //We call the intent to change activity. This method calls the necessary method for building the URL in the next activity
                    createIntentForDisplayingSearchArticlesActivity();

                } else if (!checkIfEndDateIsAfterBeginDate()) {
                    Toast.makeText(SearchArticlesActivity.this,
                            getResources().getString(R.string.search_articles_toast_end_and_begin_dates),
                            Toast.LENGTH_SHORT)
                            .show();
                } else if (checkIfEndDateIsAfterToday()) {
                    Toast.makeText(SearchArticlesActivity.this,
                            getResources().getString(R.string.search_articles_toast_end_and_today_dates),
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
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

    /**
     * This method is used to call the Intent to change
     * the Activity displayed (to DisplaySearchArticlesActivity). The intent
     * carries information about the urls than have to be displayed
     */
    public void createIntentForDisplayingSearchArticlesActivity() {

        Intent intent = new Intent(SearchArticlesActivity.this, DisplaySearchArticlesActivity.class);
        intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE1,
                getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_ONE));

        intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE2,
                getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_TWO));

        intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE3,
                getSearchArticlesUrl(
                getSearchQueryAndAdaptForUrl(),
                getNewDeskValuesAndAdaptForUrl(listOfSections),
                beginDate,
                endDate,
                Url.ArticleSearchUrl.PAGE_THREE));

        startActivity(intent);

    }

    /**
     * Method to check that the end Date is not after today
     */
    private boolean checkIfEndDateIsAfterToday() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (endYear > year) {
            return true;
        } else if (endYear == year) {
            if (endMonth > month) {
                return true;
            } else if (endMonth == month) {
                if (endDay > day) {
                    return true;
                } else {
                    return false;
                }
            } else return false;

        } else {
            return false;
        }
    }

    /**
     * Method to check that the end Date is after the Begin Date
     */

    private boolean checkIfEndDateIsAfterBeginDate() {

        if (endYear > beginYear) {
            return true;
        } else if (endYear == beginYear) {
            if (endMonth > beginMonth) {
                return true;
            } else if (endMonth == beginMonth) {
                if (endDay > beginDay) {
                    return true;
                } else {
                    return false;
                }
            } else return false;

        } else {
            return false;
        }
    }

    /** Method to transform the DatePicker date into a String to use it for a URL.
     * The modifier is used to choose the return type:
     * True: for the URL
     * False: for the String for the TextView
     * */
    public String getDateFromDatePicker (int year, int month,int dayOfMonth, boolean modifier) {

        String selectedYear;
        String selectedMonth;
        String selectedDay;

        selectedYear = year + "";

        if ((month + 1) < 10) {
            selectedMonth = "0" + (month + 1);
        } else {
            selectedMonth = (month + 1) + "";
        }

        if (dayOfMonth < 10) {
            selectedDay = "0" + (dayOfMonth);
        } else {
            selectedDay = dayOfMonth + "";
        }

        if (modifier) { return selectedDay + "/" + selectedMonth + "/" + selectedYear; }
        else { return selectedYear + selectedMonth + selectedDay; }

    }

}

