package com.example.android.mynews.Data;

/**
 * Created by Diego Fajardo on 02/03/2018.
 */

public class DataFromJSONInString {

    String section;
    String title;
    String updated_date;
    String image_url_thumbnail;
    String image_url_thumblarge;
    String image_url_normal;
    String image_url_medium;
    String image_url_superjumbo;
    String article_url;

    public void setSection (String section) {
        this.section = section;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public void setUpdatedDate (String updated_date) {
        this.updated_date = updated_date;
    }

    public void setImageThumbnail (String image) {
        this.image_url_thumbnail = image;
    }

    public void setImageThumblarge (String image) {
        this.image_url_thumblarge = image;
    }

    public void setImageNormal (String image) {
        this.image_url_normal = image;
    }

    public void setImageMedium (String image) {
        this.image_url_medium = image;
    }

    public void setImageSuperjumbo (String image) {
        this.image_url_superjumbo = image;
    }

    public void setArticleUrl (String article_url){
        this.article_url = article_url;
    }

    public String getSection () {
        return this.section;
    }

    public String getTitle () {
        return this.title;
    }

    public String getUpdatedDate () {
        return this.updated_date;
    }

    public String getImageThumbnail () {
        return this.image_url_thumbnail;
    }

    public String getImageThumblarge () {
        return this.image_url_thumblarge;
    }

    public String getImageNormal () {
        return this.image_url_normal;
    }

    public String getImageMedium () {
        return this.image_url_medium;
    }

    public String getImageSuperjumbo () {
        return this.image_url_superjumbo;
    }

    public String getArticleUrl (){
        return this.article_url;
    }




}