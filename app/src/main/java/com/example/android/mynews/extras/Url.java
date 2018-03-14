package com.example.android.mynews.extras;

/**
 * Created by Diego Fajardo on 12/03/2018.
 */

public class Url {

    // TODO: 12/03/2018 Add all the different possibilities when requesting info from the "same" API

    //TOP STORIES URL construction
    private String TS_BASE_URL = "http://api.nytimes.com/svc/topstories/v2/";
    private String TS_news_section = "world";
    private String TS_format = ".json";
    private String TS_QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
    private String TS_FINAL_URL = TS_BASE_URL + TS_news_section + TS_format + TS_QM_ApiKey;

    //MOST POPULAR URL construction
    private String MP_BASE_URL = "https://api.nytimes.com/svc/mostpopular/v2/mostviewed/";
    private String MP_news_section = "all-sections/";
    private String MP_time_period = "7";
    private String MP_format = ".json";
    private String MP_QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
    private String MP_FINAL_URL = MP_BASE_URL + MP_news_section + MP_time_period + MP_format + MP_QM_ApiKey;

    //BUSINESS URL construction
    private String B_BASE_URL = "http://api.nytimes.com/svc/topstories/v2/";
    private String B_news_section = "business";
    private String B_format = ".json";
    private String B_QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
    private String B_FINAL_URL = B_BASE_URL + B_news_section + B_format + B_QM_ApiKey;

    //SPORTS URL construction
    private String S_BASE_URL = "http://api.nytimes.com/svc/topstories/v2/";
    private String S_news_section = "sports";
    private String S_format = ".json";
    private String S_QM_ApiKey = "?api-key=a27a66145d4542d28a719cecee6de859";
    private String S_FINAL_URL = S_BASE_URL + S_news_section + S_format + S_QM_ApiKey;

    //ARTICLE SEARCH URL construction




    // TODO: 12/03/2018 Add all the different APIs




    // TODO: 12/03/2018 Add to the method that it can call a different API (parameter)
    public String getTopStoriesApiUrl () {
        return TS_FINAL_URL;
    }

    public String getMostPopularApiUrl () { return MP_FINAL_URL; }

    public String getBusinessApiUrl () { return B_FINAL_URL; }

    public String getSportsApiUrl () { return S_FINAL_URL; }


}
