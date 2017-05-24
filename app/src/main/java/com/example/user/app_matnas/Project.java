package com.example.user.app_matnas;

import java.io.Serializable;
import java.util.Arrays;

public class Project implements Serializable {

    private String projectName;
    private String projectDes;
    private String projectLogo;

    public Project(){}

    public Project(String projectName, String projectDes, String projectLogo) {
        this.projectName = projectName;
        this.projectDes = projectDes;
        this.projectLogo = projectLogo;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDes() {
        return projectDes;
    }

    public String getProjectLogo() {
        return projectLogo;
    }

}

