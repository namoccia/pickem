package com.feedthewolf.nhlpickem;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Comparator;

/**
 * Created by Nick on 12/16/2017.
 */

public class TeamStanding implements Parcelable{
    public int divisionId;
    public String divisionName;
    public int conferenceId;
    public String conferenceName;
    public LeagueRecord teamRecord;
    public int teamId;
    public String teamName;
    public int goalsAgainst;
    public int goalsScored;
    public int points;
    public int divisionRank;
    public int conferenceRank;
    public int leagueRank;
    public int wildCardRank;
    public int row;
    public int gamesPlayed;

    public TeamStanding(int divisionId, String divisionName, int conferenceId, String conferenceName, LeagueRecord teamRecord, int teamId, String teamName,
                        int goalsAgainst, int goalsScored, int points, int divisionRank, int conferenceRank,
                        int leagueRank, int wildCardRank, int row, int gamesPlayed) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.conferenceId = conferenceId;
        this.conferenceName = conferenceName;
        this.teamRecord = teamRecord;
        this.teamId = teamId;
        this.teamName = teamName;
        this.goalsAgainst = goalsAgainst;
        this.goalsScored = goalsScored;
        this.points = points;
        this.divisionRank = divisionRank;
        this.conferenceRank = conferenceRank;
        this.leagueRank = leagueRank;
        this.wildCardRank = wildCardRank;
        this.row = row;
        this.gamesPlayed = gamesPlayed;
    }

    public TeamStanding(Parcel in) {
        this.divisionId = in.readInt();
        this.divisionName = in.readString();
        this.conferenceId = in.readInt();
        this.conferenceName = in.readString();
        this.teamRecord = in.readParcelable(LeagueRecord.class.getClassLoader());
        this.teamId = in.readInt();
        this.teamName = in.readString();
        this.goalsAgainst = in.readInt();
        this.goalsScored = in.readInt();
        this.points = in.readInt();
        this.divisionRank = in.readInt();
        this.conferenceRank = in.readInt();
        this.leagueRank = in.readInt();
        this.wildCardRank = in.readInt();
        this.row = in.readInt();
        this.gamesPlayed = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(divisionId);
        parcel.writeString(divisionName);
        parcel.writeInt(conferenceId);
        parcel.writeString(conferenceName);
        parcel.writeParcelable(teamRecord, flags);
        parcel.writeInt(teamId);
        parcel.writeString(teamName);
        parcel.writeInt(goalsAgainst);
        parcel.writeInt(goalsScored);
        parcel.writeInt(points);
        parcel.writeInt(divisionRank);
        parcel.writeInt(conferenceRank);
        parcel.writeInt(wildCardRank);
        parcel.writeInt(row);
        parcel.writeInt(gamesPlayed);
    }

    public static final Parcelable.Creator<TeamStanding> CREATOR = new Parcelable.Creator<TeamStanding>() {
        public TeamStanding createFromParcel(Parcel in) {
            return new TeamStanding(in);
        }

        public TeamStanding[] newArray(int size) {
            return new TeamStanding[size];
        }
    };

    public static Comparator<TeamStanding> ConferenceRankComparator = new Comparator<TeamStanding>() {
        @Override
        public int compare(TeamStanding t1, TeamStanding t2) {
            int ConfRank1 = t1.conferenceRank;
            int ConfRank2 = t2.conferenceRank;

            //ascending order
            return ConfRank1-ConfRank2;
        }
    };

    public static Comparator<TeamStanding> LeagueRankComparator = new Comparator<TeamStanding>() {
        @Override
        public int compare(TeamStanding t1, TeamStanding t2) {
            int leagueRank1 = t1.leagueRank;
            int leagueRank2 = t2.leagueRank;

            //ascending order
            return leagueRank1-leagueRank2;
        }
    };
}
