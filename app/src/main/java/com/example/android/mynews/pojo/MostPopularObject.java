package com.example.android.mynews.pojo;

import com.example.android.mynews.extras.Keys;

/**
 * Created by Diego Fajardo on 13/03/2018.
 */

public class MostPopularObject {

    private String section;
    private String title;
    private String published_date;
    private String image_thumbnail;
    private String article_url;

    public MostPopularObject () {}

    public MostPopularObject(String section, String title, String published_date, String image_thumbnail, String article_url) {
        this.section = section;
        this.title = title;
        this.published_date = published_date;
        this.image_thumbnail = image_thumbnail;
        this.article_url = article_url;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getImage_thumbnail() {
        return image_thumbnail;
    }

    public void setImage_thumbnail(String image_thumbnail) {
        this.image_thumbnail = image_thumbnail;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }
}
