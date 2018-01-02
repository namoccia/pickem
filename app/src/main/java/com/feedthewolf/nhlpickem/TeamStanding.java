package com.feedthewolf.nhlpickem;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Nick on 12/16/2017.
 */

public class TeamStanding implements Parcelable{
    public int divisionId;
    public String divisionName;
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

    public TeamStanding(int divisionId, String divisionName, LeagueRecord teamRecord, int teamId, String teamName,
                        int goalsAgainst, int goalsScored, int points, int divisionRank, int conferenceRank,
                        int leagueRank, int wildCardRank, int row, int gamesPlayed) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
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

    static TeamStanding teamStandingFromJSON(int divId, String divName, JSONObject json) {
        try {
            LeagueRecord record = new LeagueRecord(
                    json.getJSONObject("leagueRecord").getInt("wins"),
                    json.getJSONObject("leagueRecord").getInt("losses"),
                    json.getJSONObject("leagueRecord").getInt("ot")
            );
            int teamId = json.getJSONObject("team").getInt("id");
            String teamName = json.getJSONObject("team").getString("name");
            int ga = json.getInt("goalsAgainst");
            int gf = json.getInt("goalsScored");
            int points = json.getInt("points");
            int divRank = Integer.parseInt(json.getString("divisionRank"));
            int confRank = Integer.parseInt(json.getString("conferenceRank"));
            int leagueRank = Integer.parseInt(json.getString("leagueRank"));
            int wcRank = Integer.parseInt(json.getString("wildCardRank"));
            int row = json.getInt("row");
            int gp = json.getInt("gamesPlayed");

            return new TeamStanding(divId, divName, record, teamId, teamName, ga, gf, points, divRank, confRank, leagueRank, wcRank, row, gp);
        } catch (Exception e) {
            Log.e("teamStandingFromJSON", "Error parsing standings data " + e.toString());
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(divisionId);
        parcel.writeString(divisionName);
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
}
