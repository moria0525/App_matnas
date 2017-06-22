package com.example.user.app_matnas;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Team.java
 * This class represents an object of a Team
 */

public class Team implements Serializable {

    private String teamName;
    private String teamRole;
    private String teamMail;
    private String teamDes;
    private String teamImage;

    public Team() {
    }

    public Team(String teamName, String teamRole, String teamMail, String teamDes, String teamImage) {
        this.teamName = teamName;
        this.teamRole = teamRole;
        this.teamMail = teamMail;
        this.teamDes = teamDes;
        this.teamImage = teamImage;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public String getTeamMail() {
        return teamMail;
    }

    public String getTeamDes() {
        return teamDes;
    }

    public String getTeamImage() {
        return teamImage;
    }


}

