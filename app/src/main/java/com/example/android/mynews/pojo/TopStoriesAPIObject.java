package com.example.android.mynews.pojo;

/**
 * Created by Diego Fajardo on 12/03/2018.
 */

/** POJO: object used to gather all the needed information
 * from a request to Top Stories API
 * */
public class TopStoriesAPIObject {

    private String section;
    private String subsection;
    private String title;
    private String updated_date;
    private String image_url_thumblarge;
    private String article_url;

    public TopStoriesAPIObject() { }

    public TopStoriesAPIObject(String section,
                               String subsection,
                               String title,
                               String updated_date,
                               String image_url_thumblarge,
                               String article_url) {
        this.section = section;
        this.subsection = subsection;
        this.title = title;
        this.updated_date = updated_date;
        this.image_url_thumblarge = image_url_thumblarge;
        this.article_url = article_url;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setSubsection (String subsection) { this.subsection = subsection; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdatedDate(String updated_date) {
        this.updated_date = updated_date;
    }

    public void setImageThumblarge(String image) {
        this.image_url_thumblarge = image;
    }

    public void setArticleUrl(String article_url) {
        this.article_url = article_url;
    }

    public String getSection() {
        return this.section;
    }

    public String getSubsection() {
        return this.subsection;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUpdatedDate() {
        return this.updated_date;
    }

    public String getImageThumblarge() {
        return this.image_url_thumblarge;
    }

    public String getArticleUrl() {
        return this.article_url;
    }

}