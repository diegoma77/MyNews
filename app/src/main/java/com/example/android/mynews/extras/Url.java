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

    // TODO: 12/03/2018 Add all the different APIs


    // TODO: 12/03/2018 Add to the method that it can call a different API (parameter)
    public String getTopStoriesApiUrl () {
        return TS_FINAL_URL;
    }


}
