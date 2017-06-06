package com.example.user.app_matnas;

import java.io.Serializable;
import java.util.Arrays;

public class News {

    private String newsDate;
    private String newsContent;
    private String newsImage;
    private boolean newsPush;

    public News()
    {

    }

    public News(String newsDate, String newsContent, String newsImage, boolean newsPush) {
        this.newsDate = newsDate;
        this.newsContent = newsContent;
        this.newsImage = newsImage;
        this.newsPush = newsPush;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public boolean getNewsPush() {
        return newsPush;
    }


}

