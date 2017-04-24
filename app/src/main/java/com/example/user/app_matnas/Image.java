package com.example.user.app_matnas;


public class Image {

    public Image() {
    }

    public String name;
    public String date;
    public String des;
    public String url;

    public Image(String name, String date, String des, String url) {
        this.name = name;
        this.date = date;
        this.des = des;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDes() {
        return des;
    }

    public String getUrl() {
        return url;
    }


}