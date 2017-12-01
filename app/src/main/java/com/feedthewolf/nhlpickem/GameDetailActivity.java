package com.feedthewolf.nhlpickem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GameDetailActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int gameId;
    private String apiDate;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        Intent intent = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Game game = intent.getParcelableExtra("TEST_PAR_ABLE");
        apiDate = new SimpleDateFormat("yyyy-MM-dd").format(game.getDate());
        gameId = game.getGameId();

        dbHelper = DatabaseHelper.getInstance(getBaseContext());

        setUpperTextViews(game);
        setAwayTeamBox(game);
        setHomeTeamBox(game);
        setPickButtons(game);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshGameDetail);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Log.i("REFRESH", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refreshGameDetails();
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshGameDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpperTextViews(Game game) {
        TextView upperLeftTextView = (TextView) findViewById(R.id.upperLeftTextView);
        TextView upperCenterTextView = (TextView) findViewById(R.id.upperCenterTextView);
        TextView upperRightTextView = (TextView) findViewById(R.id.upperRightTextView);

        SimpleDateFormat gameDate = new SimpleDateFormat("MMM d");
        String leftText = gameDate.format(game.getDate());

        String middleText = "";
        if (!game.getStatus().equalsIgnoreCase("preview")) {
            middleText = game.getStatus();
        }

        String rightText = "";
        if (game.hasGameStarted()) {
            if (!game.getStatus().equalsIgnoreCase("Final")) {
                rightText = String.format("%s %s", game.getCurrentPeriodOrdinal(), game.getCurrentPeriodTimeRemaining());
            }
        }
        else {
            SimpleDateFormat gameStartTime = new SimpleDateFormat("h:mm a");
            rightText = gameStartTime.format(game.getDate());
        }

        upperLeftTextView.setText(leftText);
        upperCenterTextView.setText(middleText);
        upperRightTextView.setText(rightText);
    }

    private void setAwayTeamBox(Game game) {
        ImageView awayLogo = (ImageView) findViewById(R.id.awayTeamImageView);
        TextView awayTeamNameView = (TextView) findViewById(R.id.awayTeamNameTextView);
        TextView awayTeamRecordView = (TextView) findViewById(R.id.awayTeamRecordTextView);
        TextView awayTeamScore = (TextView) findViewById(R.id.awayTeamScoreTextView);

        awayLogo.setImageResource(GameAdapter.getImageResourceIdByTeamId(game.getAwayTeam().getId()));
        awayTeamNameView.setText(game.getAwayTeam().getName());
        awayTeamRecordView.setText(game.getAwayTeam().getLeagueRecord().toString());

        //awayLogo.setVisibility(View.INVISIBLE);

        if (game.getStatus().equalsIgnoreCase("Preview")) {
            awayTeamScore.setText("");
        }
        else {
            awayTeamScore.setText(Integer.toString(game.getAwayTeam().getScore()));
        }

    }

    private void setHomeTeamBox(Game game) {
        ImageView homeLogo = (ImageView) findViewById(R.id.homeTeamImageView);
        TextView homeTeamNameView = (TextView) findViewById(R.id.homeTeamNameTextView);
        TextView homeTeamRecordView = (TextView) findViewById(R.id.homeTeamRecordTextView);
        TextView homeTeamScore = (TextView) findViewById(R.id.homeTeamScoreTextView);

        homeLogo.setImageResource(GameAdapter.getImageResourceIdByTeamId(game.getHomeTeam().getId()));
        homeTeamNameView.setText(game.getHomeTeam().getName());
        homeTeamRecordView.setText(game.getHomeTeam().getLeagueRecord().toString());

        //homeLogo.setVisibility(View.INVISIBLE);

        if (game.getStatus().equalsIgnoreCase("Preview")) {
            homeTeamScore.setText("");
        }
        else {
            homeTeamScore.setText(Integer.toString(game.getHomeTeam().getScore()));
        }
    }

    private void refreshGameDetails() {
        //Date date = new Date();
        //String apiDate = "2017-01-12";
        //String apiDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String currentUrlString = String.format("https://statsapi.web.nhl.com/api/v1/schedule?startDate=%s&endDate=%s&expand=schedule.teams,schedule.linescore", apiDate, apiDate);

        GameFeedJsonParser parser = new GameFeedJsonParser();
        parser.execute(currentUrlString);
    }

    private void setPickButtons(final Game game) {
        final ToggleButton awayTeamButton = (ToggleButton) findViewById(R.id.awayTeamToggleButton);
        final ToggleButton homeTeamButton = (ToggleButton) findViewById(R.id.homeTeamToggleButton);
        final ImageView lockImageView = (ImageView) findViewById(R.id.lockImageView);
        final boolean hasGameStarted = game.hasGameStarted();

        awayTeamButton.setText(game.getAwayTeam().getName());
        awayTeamButton.setTextOff(game.getAwayTeam().getName());
        awayTeamButton.setTextOn(game.getAwayTeam().getName());
        homeTeamButton.setText(game.getHomeTeam().getName());
        homeTeamButton.setTextOff(game.getHomeTeam().getName());
        homeTeamButton.setTextOn(game.getHomeTeam().getName());

        if(dbHelper.pickEntryAlreadyExistsForGameId(gameId, dbHelper)) {
            Cursor cursor;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "SELECT * FROM picks WHERE gameId=" + gameId;
            cursor = db.rawQuery(sql, null);

            cursor.moveToFirst();
            String currentSelection = cursor.getString(cursor.getColumnIndex("selection"));
            cursor.close();

            if (currentSelection.equalsIgnoreCase("away")) {
                awayTeamButton.setChecked(true);
                awayTeamButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                awayTeamButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.textColorPrimary));
            }
            else if (currentSelection.equalsIgnoreCase("home")) {
                homeTeamButton.setChecked(true);
                homeTeamButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                homeTeamButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.textColorPrimary));
            }
        }

        awayTeamButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    homeTeamButton.setChecked(false);
                    awayTeamButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                    awayTeamButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.textColorPrimary));

                    dbHelper.updatePickInDatabase(game.getGameId(), "away", "none", dbHelper);
                }
                else {
                    awayTeamButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.windowBackground));
                    awayTeamButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.navigationBarColor));
                }
            }
        });

        homeTeamButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    awayTeamButton.setChecked(false);
                    homeTeamButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                    homeTeamButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.textColorPrimary));

                    dbHelper.updatePickInDatabase(game.getGameId(), "home", "none", dbHelper);
                }
                else {
                    homeTeamButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.windowBackground));
                    homeTeamButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.navigationBarColor));
                }
            }
        });

        // Lock in picks
        if (hasGameStarted) {
            awayTeamButton.setEnabled(false);
            homeTeamButton.setEnabled(false);
            lockImageView.setVisibility(ImageView.VISIBLE);
        }

        // Update result in database once game is over
        if (game.getStatus().equalsIgnoreCase("Final")) {
            if(dbHelper.pickEntryAlreadyExistsForGameId(gameId, dbHelper)) {
                Cursor cursor;
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sql = "SELECT * FROM picks WHERE gameId=" + gameId;
                cursor = db.rawQuery(sql, null);

                cursor.moveToFirst();
                String currentSelection = cursor.getString(cursor.getColumnIndex("selection"));
                cursor.close();

                String winner = game.getWinnerForDatabase();

                if (currentSelection.equalsIgnoreCase("away")) {
                    dbHelper.updatePickInDatabase(game.getGameId(), "away", winner, dbHelper);
                }
                else if (currentSelection.equalsIgnoreCase("home")) {
                    dbHelper.updatePickInDatabase(game.getGameId(), "home", winner, dbHelper);
                }
            }
        }
    }

    private class GameFeedJsonParser extends AsyncTask<String, Void, String> {

        final String TAG = "GameFeedJsonParser";

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do something with the JSON string

            try {
                JSONObject jObj = new JSONObject(result);
                //Toast.makeText(MainActivity.this, "From API: " + jObj.getJSONArray("teams").getJSONObject(0).getString("name"), Toast.LENGTH_LONG).show();
                int numberOfGames = jObj.getInt("totalGames");

                ArrayList<Game> gameList = new ArrayList<>();
                for(int currentGame = 0; currentGame < numberOfGames; currentGame++) {
                    JSONObject currentGameJSON = jObj.getJSONArray("dates").getJSONObject(0).getJSONArray("games").getJSONObject(currentGame);
                    gameList.add(Game.gameFromJSON(currentGameJSON));
                }

                Game game = findGameInGameListByGameId(gameList, gameId);

                setUpperTextViews(game);
                setAwayTeamBox(game);
                setHomeTeamBox(game);
                setPickButtons(game);

                mSwipeRefreshLayout.setRefreshing(false);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }
        }

        Game findGameInGameListByGameId(ArrayList<Game> gameArrayList, int gameId) {

            for(Game game: gameArrayList) {
                if (game.getGameId() == gameId) {
                    return game;
                }
            }

            return gameArrayList.get(0);
        }
    }
}
