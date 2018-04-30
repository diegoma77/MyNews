package com.example.android.mynews.extras.interfaceswithconstants;

/**
 * Created by Diego Fajardo on 15/03/2018.
 */

/** This interface is used to build the urls
 * */
public interface Url {

    /** Tokens that are common to different parts of the urls
     * */
    interface GeneralTokens {

        String EQUAL = "=";
        String AMPERSAND = "&";
        String QM = "?";
        String PLUS = "+";

    }

    /** Used to build the Top Stories API request
     * */
    interface TopStoriesUrl {

        String TS_BASE_URL = "http://api.nytimes.com/svc/topstories/v2/";
        String TS_news_section = "world";
        String TS_format = ".json";
        String TS_QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
        String TS_FINAL_URL = TS_BASE_URL + TS_news_section + TS_format + TS_QM_ApiKey;

    }

    /** Used to build the Most Popular API request
     * */
    interface MostPopularUrl {

        String MP_BASE_URL = "https://api.nytimes.com/svc/mostpopular/v2/mostviewed/";
        String MP_news_section = "all-sections/";
        String MP_time_period = "7";
        String MP_format = ".json";
        String MP_QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
        String MP_FINAL_URL = MP_BASE_URL + MP_news_section + MP_time_period + MP_format + MP_QM_ApiKey;

    }

    /** Used to build the Top Stories API request(only for business articles)
     * */
    interface BusinessUrl {

        String B_BASE_URL = "http://api.nytimes.com/svc/topstories/v2/";
        String B_news_section = "business";
        String B_format = ".json";
        String B_QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
        String B_FINAL_URL = B_BASE_URL + B_news_section + B_format + B_QM_ApiKey;

    }

    /** Used to build the Top Stories API request (only for sports articles)
     * */
    interface SportsUrl {

        String S_BASE_URL = "http://api.nytimes.com/svc/topstories/v2/";
        String S_news_section = "sports";
        String S_format = ".json";
        String S_QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
        String S_FINAL_URL = S_BASE_URL + S_news_section + S_format + S_QM_ApiKey;

    }

    /** Used to build the Articles Search API request
     * */
    interface ArticleSearchUrl {

        String BASE_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json?";
        String Q = "q";
        String FQ = "fq";
        String NEWS_DESK = "news_desk";
        String ARTS = "arts";
        String BUSINESS = "business";
        String ENTREPRENEURS = "entrepreneurs";
        String POLITICS = "politics";
        String SPORTS = "sports";
        String TRAVEL = "travel";
        String BEGIN_DATE = "begin_date";
        String END_DATE = "end_date";
        String SORT_NEWEST = "sort=newest";
        String SORT_OLDEST = "sort=oldest";
        String PAGE = "page";
        String PAGE_ONE = "1";
        String PAGE_TWO = "2";
        String PAGE_THREE = "3";
        String API_KEY = "api-key=a27a66145d4542d28a719cecee6de859";
        String IMAGE_URL_BASE = "https://www.nytimes.com/";

    }

}
