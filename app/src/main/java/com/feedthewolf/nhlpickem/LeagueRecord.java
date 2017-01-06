package com.feedthewolf.nhlpickem;

/**
 * Created by nmoccia on 1/4/2017.
 */

public class LeagueRecord {
    private int wins;
    private int losses;
    private int ot;

    public LeagueRecord(int wins, int losses, int ot) {
        this.wins = wins;
        this.losses = losses;
        this.ot = ot;
    }

    @Override
    public String toString() {
        return String.format("%d-%d-%d", wins, losses, ot);
    }

    //region Getters
    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getOt() {
        return ot;
    }
    //endregion


    //region Setters
    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setOt(int ot) {
        this.ot = ot;
    }
    //endregion
}
