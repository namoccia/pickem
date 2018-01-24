package com.feedthewolf.nhlpickem;

import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Nick on 1/23/2018.
 */

public class ApiParser {
    static Game gameFromJSON(JSONObject gameJSON) {

        try {

            //region Set up awayTeam object
            //----------------------------------------------------------------------------------
            int awayWins = gameJSON.getJSONObject("teams").getJSONObject("away").getJSONObject("leagueRecord").getInt("wins");
            int awayLosses = gameJSON.getJSONObject("teams").getJSONObject("away").getJSONObject("leagueRecord").getInt("losses");

            int awayOt = 0;
            if (gameJSON.getString("gameType").equalsIgnoreCase("R"))
                awayOt = gameJSON.getJSONObject("teams").getJSONObject("away").getJSONObject("leagueRecord").getInt("ot");

            int awayTeamId = gameJSON.getJSONObject("teams").getJSONObject("away").getJSONObject("team").getInt("id");
            String awayTeamName = gameJSON.getJSONObject("teams").getJSONObject("away").getJSONObject("team").getString("name");
            int awayTeamScore = gameJSON.getJSONObject("teams").getJSONObject("away").getInt("score");

            LeagueRecord awayLeagueRecord = new LeagueRecord(awayWins, awayLosses, awayOt);
            Team awayTeam = new Team(awayLeagueRecord, awayTeamScore, awayTeamId, awayTeamName);
            //----------------------------------------------------------------------------------
            //endregion

            //region Set up homeTeam object
            //----------------------------------------------------------------------------------
            int homeWins = gameJSON.getJSONObject("teams").getJSONObject("home").getJSONObject("leagueRecord").getInt("wins");
            int homeLosses = gameJSON.getJSONObject("teams").getJSONObject("home").getJSONObject("leagueRecord").getInt("losses");

            int homeOt = 0;
            if (gameJSON.getString("gameType").equalsIgnoreCase("R"))
                homeOt = gameJSON.getJSONObject("teams").getJSONObject("home").getJSONObject("leagueRecord").getInt("ot");
            int homeTeamId = gameJSON.getJSONObject("teams").getJSONObject("home").getJSONObject("team").getInt("id");
            String homeTeamName = gameJSON.getJSONObject("teams").getJSONObject("home").getJSONObject("team").getString("name");
            int homeTeamScore = gameJSON.getJSONObject("teams").getJSONObject("home").getInt("score");

            LeagueRecord homeLeagueRecord = new LeagueRecord(homeWins, homeLosses, homeOt);
            Team homeTeam = new Team(homeLeagueRecord, homeTeamScore, homeTeamId, homeTeamName);
            //----------------------------------------------------------------------------------
            //endregion

            DateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            jsonDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date gameDate = jsonDateFormat.parse(gameJSON.getString("gameDate"));

            String gameStatus = gameJSON.getJSONObject("status").getString("abstractGameState");
            String currentPeriodOrdinal = "";
            String currentPeriodTimeRemaining = "";
            if (!gameStatus.equalsIgnoreCase("preview")){
                currentPeriodOrdinal = gameJSON.getJSONObject("linescore").getString("currentPeriodOrdinal");
                currentPeriodTimeRemaining = gameJSON.getJSONObject("linescore").getString("currentPeriodTimeRemaining");
            }
            int gameId = gameJSON.getInt("gamePk");

            return new Game(gameDate, gameStatus, awayTeam, homeTeam, currentPeriodOrdinal, currentPeriodTimeRemaining, gameId);
        } catch (Exception e) {
            Log.e("GameFromJson", "Error parsing game data " + e.toString());
            return null;
        }
    }

    static TeamStanding teamStandingFromJSON(int divId, String divName, int conId, String conName, JSONObject json) {
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

            return new TeamStanding(divId, divName, conId, conName, record, teamId, teamName, ga, gf, points, divRank, confRank, leagueRank, wcRank, row, gp);
        } catch (Exception e) {
            Log.e("teamStandingFromJSON", "Error parsing standings data " + e.toString());
            return null;
        }
    }
}
