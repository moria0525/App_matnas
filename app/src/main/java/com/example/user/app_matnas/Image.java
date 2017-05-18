package com.example.user.app_matnas;


import java.io.Serializable;

public class Image{

    public Image() {
    }

    public String name;
    public String url;

    public Image(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }


}