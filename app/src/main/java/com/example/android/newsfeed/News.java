package com.example.android.newsfeed;

public class News {

    //defaultTitle is the title of article
    private String defaultTitle;

    //defaultSection is the name of section
    private String defaultSection;

    //defaultData is the date of publishing the article
    private String defaultData;

    //defaultUrl is the website were we can find the details for that article
    private String defaultUrl;

    private String defaultAuthorName;

    //create a new constructor
    public News(String titleOfArticle, String sectionName, String dataPublished, String url, String authorName) {
        defaultTitle = titleOfArticle;
        defaultSection = sectionName;
        defaultData = dataPublished;
        defaultUrl = url;
        defaultAuthorName = authorName;
    }

    //get the title of article
    public String getTitle() {
        return defaultTitle;
    }

    //get the name of section
    public String getSectionName() {
        return defaultSection;
    }

    //get data of published for the article
    public String getData() {
        return defaultData;
    }

    //get web site for that article
    public String getUrl() {
        return defaultUrl;
    }

    //get author name for that article
    public String getAuthorName(){
        return defaultAuthorName;
    }
}
