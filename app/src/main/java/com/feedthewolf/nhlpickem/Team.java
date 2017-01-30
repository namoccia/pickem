package com.feedthewolf.nhlpickem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nmoccia on 1/4/2017.
 */

class Team implements Parcelable {
    private LeagueRecord leagueRecord;
    private int score;
    private int id;
    private String name;

    private Team(LeagueRecord leagueRecord, int score, int id, String name) {
        this.leagueRecord = leagueRecord;
        this.score = score;
        this.id = id;
        this.name = name;
    }

    private Team(Parcel in) {
        this.leagueRecord = in.readParcelable(LeagueRecord.class.getClassLoader());
        this.score = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(leagueRecord, flags);
        dest.writeInt(score);
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    @Override
    public String toString() {
        return String.format("%s (%s)", name, leagueRecord.toString());
    }

    //region Getters
    LeagueRecord getLeagueRecord() {
        return leagueRecord;
    }

    int getScore() {
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
