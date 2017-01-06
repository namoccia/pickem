package com.feedthewolf.nhlpickem;

import java.util.Date;

/**
 * Created by nmoccia on 1/4/2017.
 */

public class Game {
    private Date date;
    private String status;
    private Team awayTeam;
    private Team homeTeam;

    public Game(Date date, String status, Team awayTeam, Team homeTeam) {
        this.date = date;
        this.status = status;
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
    }

    @Override
    public String toString() {
        return String.format("%s at %s", awayTeam.toString(), homeTeam.toString());
    }

    //region Getters
    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }
    //endregion

    //region Setters
    public void setDate(Date date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }
    //endregion
}


