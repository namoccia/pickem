package com.feedthewolf.nhlpickem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by nmoccia on 1/4/2017.
 */

public class Game implements Parcelable {
    private Date date;
    private String status;
    private Team awayTeam;
    private Team homeTeam;
    private String currentPeriodOrdinal;
    private String currentPeriodTimeRemaining;

    public Game(Date date, String status, Team awayTeam, Team homeTeam, String currentPeriodOrdinal, String currentPeriodTimeRemaining) {
        this.date = date;
        this.status = status;
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
        this.currentPeriodOrdinal = currentPeriodOrdinal;
        this.currentPeriodTimeRemaining = currentPeriodTimeRemaining;
    }

    public Game(Parcel in) {
        this.awayTeam = in.readParcelable(Team.class.getClassLoader());
        this.homeTeam = in.readParcelable(Team.class.getClassLoader());
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.status = in.readString();
        this.currentPeriodOrdinal = in.readString();
        this.currentPeriodTimeRemaining = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(awayTeam, flags);
        dest.writeParcelable(homeTeam, flags);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeString(status);
        dest.writeString(currentPeriodOrdinal);
        dest.writeString(currentPeriodTimeRemaining);
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

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

    public String getCurrentPeriodOrdinal() {
        return currentPeriodOrdinal;
    }

    public String getCurrentPeriodTimeRemaining() {
        return currentPeriodTimeRemaining;
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

    public void setCurrentPeriodOrdinal(String currentPeriodOrdinal) {
        this.currentPeriodOrdinal = currentPeriodOrdinal;
    }

    public void setCurrentPeriodTimeRemaining(String currentPeriodTimeRemaining) {
        this.currentPeriodTimeRemaining = currentPeriodTimeRemaining;
    }
    //endregion
}


