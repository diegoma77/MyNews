package com.example.android.mynews.pojo;

import com.example.android.mynews.activities.SearchArticlesActivity;

/**
 * Created by Diego Fajardo on 17/03/2018.
 */

public class SearchArticlesObject {

    private String web_url = "web_url";
    private String snippet = "snippet";
    private String image_url = "url";
    private String pub_date = "pub_date";

    public SearchArticlesObject () { }

    public SearchArticlesObject(String web_url, String snippet, String image_url, String pub_date) {
        this.web_url = web_url;
        this.snippet = snippet;
        this.image_url = image_url;
        this.pub_date = pub_date;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }
}
