package com.example.android.mynews.pojo;

import java.io.Serializable;

/**
 * Created by Diego Fajardo on 17/03/2018.
 */

/** POJO: object used to gather all the needed information
 * from a request to Articles Search API
 * */
public class ArticlesSearchAPIObject implements Serializable {

    private String webUrl = "webUrl";
    private String snippet = "snippet";
    private String imageUrl = "url";
    private String newDesk = "newDesk";
    private String pubDate = "pubDate";

    public ArticlesSearchAPIObject() { }

    public ArticlesSearchAPIObject(String webUrl, String snippet, String imageUrl, String pubDate) {
        this.webUrl = webUrl;
        this.snippet = snippet;
        this.imageUrl = imageUrl;
        this.pubDate = pubDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewDesk() {
        return newDesk;
    }

    public void setNewDesk(String newDesk) {
        this.newDesk = newDesk;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
