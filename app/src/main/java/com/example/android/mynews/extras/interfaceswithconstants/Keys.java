package com.example.android.mynews.extras.interfaceswithconstants;

/**
 * Created by Diego Fajardo on 12/03/2018.
 */

public interface Keys {

    interface ApiFetcher {
        String ARTICLES_API_REFERENCE = "Articles";
        String TOPSTORIES_API_REFERENCE = "TopStories";
        String MOSTPOPULAR_API_REFERENCE = "MostPopular";
    }

    interface PutExtras {

        String ARTICLE_URL_SENT = "article_url";
        String LIST_OF_API_OBJECTS = "list_of_api_objects";
        String INTENT_SA_PAGE1 = "page1";
        String INTENT_SA_PAGE2 = "page2";
        String INTENT_SA_PAGE3 = "page3";

    }

    interface TopStoriesAPIKeys {

        String KEY_RESULTS = "results";
        String KEY_SECTION = "section";
        String KEY_SUBSECTION = "subsection";
        String KEY_TITLE = "title";
        String KEY_ARTICLE_URL = "url";
        String KEY_UPDATED_DATE = "updated_date";
        String KEY_MULTIMEDIA = "multimedia";
        String KEY_IMAGE_URL = "url";
    }

    interface TopStoriesKeys {

        String KEY_RESULTS = "results";
        String KEY_SECTION = "section";
        String KEY_TITLE = "title";
        String KEY_ARTICLE_URL = "url";
        String KEY_UPDATED_DATE = "updated_date";
        String KEY_MULTIMEDIA = "multimedia";
        String KEY_IMAGE_URL = "url";

    }

    interface MostPopularKeys {

        String KEY_RESULTS = "results";
        String KEY_ARTICLE_URL = "url";
        String KEY_SECTION = "section";
        String KEY_TITLE = "title";
        String KEY_PUBLISHED_DATE = "published_date";
        String KEY_MEDIA = "media";
        String KEY_MEDIA_METADATA = "media-metadata";
        String KEY_IMAGE_URL = "url";

    }

    interface Business {

        String KEY_RESULTS = "results";
        String KEY_SUBSECTION = "subsection";
        String KEY_TITLE = "title";
        String KEY_ARTICLE_URL = "url";
        String KEY_UPDATED_DATE = "updated_date";
        String KEY_MULTIMEDIA = "multimedia";
        String KEY_IMAGE_URL = "url";

    }

    interface Sports {

        String KEY_RESULTS = "results";
        String KEY_SUBSECTION = "subsection";
        String KEY_TITLE = "title";
        String KEY_ARTICLE_URL = "url";
        String KEY_UPDATED_DATE = "updated_date";
        String KEY_MULTIMEDIA = "multimedia";
        String KEY_IMAGE_URL = "url";

    }

    interface SearchArticles {

        String KEY_RESPONSE = "response";
        String KEY_WEB_URL = "web_url";
        String KEY_SNIPPET = "snippet";
        String KEY_IMAGE_URL = "url";
        String KEY_NEW_DESK = "new_desk";
        String KEY_PUB_DATE = "pub_date";
        String KEY_DOCS = "docs";
        String KEY_MULTIMEDIA = "multimedia";

    }

    interface CheckboxFields {

        String CB_ARTS = "arts";
        String CB_BUSINESS = "business";
        String CB_ENTREPRENEURS = "entrepreneurs";
        String CB_POLITICS = "politics";
        String CB_SPORTS = "sports";
        String CB_TRAVEL = "travel";

    }

}
