package com.feedthewolf.nhlpickem;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Nick on 12/16/2017.
 */

public class TeamStanding {
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

    public TeamStanding teamStandingFromJSON(int divId, String divName, JSONObject json) {
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
}
