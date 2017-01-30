package com.feedthewolf.nhlpickem;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nmoccia on 1/4/2017.
 */

class Game implements Parcelable {
    private Date date;
    private String status;
    private Team awayTeam;
    private Team homeTeam;
    private String currentPeriodOrdinal;
    private String currentPeriodTimeRemaining;
    private int gameId;

    private Game(Date date, String status, Team awayTeam, Team homeTeam, String currentPeriodOrdinal, String currentPeriodTimeRemaining, int gameId) {
        this.date = date;
        this.status = status;
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
        this.currentPeriodOrdinal = currentPeriodOrdinal;
        this.currentPeriodTimeRemaining = currentPeriodTimeRemaining;
        this.gameId = gameId;
    }

    private Game(Parcel in) {
        this.awayTeam = in.readParcelable(Team.class.getClassLoader());
        this.homeTeam = in.readParcelable(Team.class.getClassLoader());
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.status = in.readString();
        this.currentPeriodOrdinal = in.readString();
        this.currentPeriodTimeRemaining = in.readString();
        this.gameId = in.readInt();
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
        dest.writeInt(gameId);
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
    Date getDate() {
        return date;
    }

    String getStatus() {
        return status;
    }

    Team getAwayTeam() {
        return awayTeam;
    }

    Team getHomeTeam() {
        return homeTeam;
    }

    String getCurrentPeriodOrdinal() {
        return currentPeriodOrdinal;
    }

    String getCurrentPeriodTimeRemaining() {
        return currentPeriodTimeRemaining;
    }

    int getGameId() {
        return gameId;
    }

    String getWinnerForDatabase() {
        if (getAwayTeam().getScore() > getHomeTeam().getScore())
            return "away";
        else
            return "home";
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

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
    //endregion

    boolean hasGameFinished() {
        return getStatus().equalsIgnoreCase("Final");
    }

    boolean hasGameStarted() {
        return getDate().compareTo(new Date()) < 0;
    }

    static Game gameFromJSON(JSONObject gameJSON) {

        try {

            //region Set up awayTeam object
            //----------------------------------------------------------------------------------
            int awayWins = gameJSON.getJSONObject("teams").getJSONObject("away").getJSONObject("leagueRecord").getInt("wins");
            int awayLosses = gameJSON.getJSONObject("teams").getJSONObject("away").getJSONObject("leagueRecord").getInt("losses");
            int awayOt = gameJSON.getJSONObject("teams").getJSONObject("away").getJSONObject("leagueRecord").getInt("ot");
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
            int homeOt = gameJSON.getJSONObject("teams").getJSONObject("home").getJSONObject("leagueRecord").getInt("ot");
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
}


