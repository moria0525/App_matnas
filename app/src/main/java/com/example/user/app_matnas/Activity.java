package com.example.user.app_matnas;

import java.io.Serializable;
import java.util.Arrays;

public class Activity implements Serializable {

    private String activityName;
    private String activityType;
    private String activityAge;
    private String activityDays;
    private String activityStart;
    private String activityEnd;
    private String activityDes;

    private boolean[] activityBType;
    private boolean[] activityBDays;


    public Activity() {

    }

    public Activity(String activityName, String activityType, String activityDes, String activityAge, String activityDays, String activityStart, String activityEnd) {
        this.activityName = activityName;
        this.activityType = activityType;
        this.activityAge = activityAge;
        this.activityDays = activityDays;
        this.activityStart = activityStart;
        this.activityEnd = activityEnd;
        this.activityDes = activityDes;
    }


    public String getActivityName() {
        return activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getActivityAge() {
        return activityAge;
    }

    public String getActivityDays() {
        return activityDays;
    }

    public String getActivityStart() {
        return activityStart;
    }

    public String getActivityEnd() {
        return activityEnd;
    }

    public String getActivityDes() {
        return activityDes;
    }

    public boolean[] getActivityBType() {
        return activityBType;
    }

    public boolean[] getActivityBDays() {
        return activityBDays;

    }
    public void setActivityBType(boolean[] activityBType) {
        this.activityBType = activityBType;
    }

    public void setActivityBDays(boolean[] activityBDays) {
        this.activityBDays = activityBDays;
    }

}

