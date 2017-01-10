package com.feedthewolf.nhlpickem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nmoccia on 1/4/2017.
 */

public class LeagueRecord implements Parcelable {
    private int wins;
    private int losses;
    private int ot;

    public LeagueRecord(int wins, int losses, int ot) {
        this.wins = wins;
        this.losses = losses;
        this.ot = ot;
    }

    public LeagueRecord(Parcel in) {
        this.wins = in.readInt();
        this.losses = in.readInt();
        this.ot = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(wins);
        dest.writeInt(losses);
        dest.writeInt(ot);
    }

    public static final Parcelable.Creator<LeagueRecord> CREATOR = new Parcelable.Creator<LeagueRecord>() {

        public LeagueRecord createFromParcel(Parcel in) {
            return new LeagueRecord(in);
        }

        public LeagueRecord[] newArray(int size) {
            return new LeagueRecord[size];
        }
    };

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
