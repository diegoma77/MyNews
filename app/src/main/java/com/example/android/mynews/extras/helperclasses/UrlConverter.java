package com.example.android.mynews.extras.helperclasses;

import android.support.design.widget.TextInputEditText;
import android.util.Log;

import com.example.android.mynews.extras.interfaceswithconstants.Url;

import java.util.List;

/**
 * Created by Diego Fajardo on 02/05/2018.
 */

public class UrlConverter {

    private static final String TAG = "UrlConverter";

    /**
     * This method is used when the SEARCH BUTTON is clicked.
     * It starts the process of searching for the articles according to the information
     * needed from the user.
     * Changes spaces for + symbols.
     */
    public static String getSearchQueryAndAdaptForUrl(TextInputEditText input) {

        String searchQueryAdaptedForUrl = input.getText().toString();

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
    public static String getSectionsAndAdaptForUrl(List<String> listOfStrings) {

        Log.e(TAG, "getSectionsAndAdaptForUrl: THIS IS REACHED(1)");

        for (int i = 0; i < listOfStrings.size(); i++) {

            if (listOfStrings.get(i).equals("")){
                listOfStrings.remove(listOfStrings.get(i));
            }

        }

        Log.e(TAG, "getSectionsAndAdaptForUrl: THIS IS REACHED(2)");

        for (int i = 0; i < listOfStrings.size() ; i++) {
            System.out.println(listOfStrings.get(i));
        }

        Log.e(TAG, "getSectionsAndAdaptForUrl: THIS IS REACHED(3)");

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < listOfStrings.size(); i++) {

            if (!listOfStrings.get(i).equals("")) {

                stringBuilder.append("+").append(listOfStrings.get(i));

            }
        }

        Log.e(TAG, "getSectionsAndAdaptForUrl: THIS IS REACHED(4)");

        if (stringBuilder.length() > 0){
            if (stringBuilder.charAt(0) == '+') {
                stringBuilder.deleteCharAt(0);
            }
        }

        Log.e(TAG, "getSectionsAndAdaptForUrl: THIS IS REACHED(5) --> " + stringBuilder);

        return stringBuilder.toString();
    }

    /**
     * This method builds the Url used to send the JSON request
     * using the strings created (modified) by other methods
     */
    public static String getSearchArticlesUrl(String searchQuery, String newsSearchQuery, String beginDate, String endDate, String page) {

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








}
