package com.example.user.app_matnas;

public class DataSource {
    private String nameText;

    public DataSource(){}

    public DataSource(String nameText)
    {
        this.nameText=nameText;
    }


    public void setNameText(String nameText)
    {
        this.nameText=nameText;
    }

    public String getNameText()
    {
        return nameText;
    }


}