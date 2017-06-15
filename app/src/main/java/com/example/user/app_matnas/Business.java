package com.example.user.app_matnas;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Arrays;

public class Business implements Serializable {

    private String businessName;
    private String businessCategory;
    private String businessDes;
    private String businessPhone;
    private String businessMail;
    private String businessImage;
    private Double latitude;
    private Double longitude;

    public Business() {
    }

    public Business(String businessName, String businessCategory, String businessDes, String businessPhone, String businessMail, String businessImage, double latitude, double longitude) {
        this.businessName = businessName;
        this.businessCategory = businessCategory;
        this.businessDes = businessDes;
        this.businessPhone = businessPhone;
        this.businessMail = businessMail;
        this.businessImage = businessImage;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public String getBusinessDes() {
        return businessDes;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public String getBusinessMail() {
        return businessMail;
    }

    public String getBusinessImage() {
        return businessImage;
    }


    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}

