package com.feedthewolf.nhlpickem;

/**
 * Created by nmoccia on 1/4/2017.
 */

public class Team {
    private LeagueRecord leagueRecord;
    private int score;
    private int id;
    private String name;

    public Team(LeagueRecord leagueRecord, int score, int id, String name) {
        this.leagueRecord = leagueRecord;
        this.score = score;
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, leagueRecord.toString());
    }

    //region Getters
    public LeagueRecord getLeagueRecord() {
        return leagueRecord;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    //endregion


    //region Setters
    public void setLeagueRecord(LeagueRecord leagueRecord) {
        this.leagueRecord = leagueRecord;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion
}
