package com.example.user.app_matnas;

import java.io.Serializable;

public class WorkShop implements Serializable {


    private String workShopName;
    private String workShopDate;
    private String workShopDes;

    public WorkShop() {

    }

    public WorkShop(String workShopName, String workShopDate, String workShopDes) {
        this.workShopName = workShopName;
        this.workShopDate = workShopDate;
        this.workShopDes = workShopDes;
    }

    public String getWorkShopName() {
        return workShopName;
    }

    public String getWorkShopDate() {
        return workShopDate;
    }

    public String getWorkShopDes() {
        return workShopDes;
    }


}

