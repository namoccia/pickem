package com.feedthewolf.nhlpickem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mTotalPicksNumberView;
    private TextView mCorrectPicksNumberView;
    private TextView mCorrectPercentageNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initializePrivates();

        ArrayList<Integer> gameIdsWithNoneAsResult = dbHelper.getGameIdsWithNoneAsResult(dbHelper);

        TextView numWithNoneAsResultView = (TextView)findViewById(R.id.gameIdsWithNoneAsResultTextView);
        numWithNoneAsResultView.setText("Number with none as result: " + gameIdsWithNoneAsResult.size());

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

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void initializePrivates() {
        dbHelper = DatabaseHelper.getInstance(getBaseContext());

        mTotalPicksNumberView =         (TextView)findViewById(R.id.totalPicksNumberTextView);
        mCorrectPicksNumberView =       (TextView)findViewById(R.id.correctPicksNumberTextView);
        mCorrectPercentageNumberView =  (TextView)findViewById(R.id.correctPercentNumberTextView);

        mTotalPicksNumberView.setText(Integer.toString(dbHelper.getTotalNumberOfPicks(dbHelper)));
        mCorrectPicksNumberView.setText(Integer.toString(dbHelper.getTotalCorrectPicks(dbHelper)));
        mCorrectPercentageNumberView.setText(
                String.format("%.1f",
                            ((double)dbHelper.getTotalCorrectPicks(dbHelper) / (double)dbHelper.getTotalNumberOfPicks(dbHelper))*100 ) + '%');

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshStats);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Log.i("REFRESH", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refreshStats();
                    }
                }
        );
    }

    public void refreshStats() {
        ArrayList<Integer> gameIdsWithNoneAsResult = dbHelper.getGameIdsWithNoneAsResult(dbHelper);
        UpdateNoneResultsInDatabaseAsync updateNoneResults = new UpdateNoneResultsInDatabaseAsync();

        updateNoneResults.execute(gameIdsWithNoneAsResult.toArray(new Integer[0]));
    }

    class UpdateNoneResultsInDatabaseAsync extends AsyncTask<Integer, Void, Void> {

        HttpURLConnection urlConnection;

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //parseing all details

            mTotalPicksNumberView.setText(Integer.toString(dbHelper.getTotalNumberOfPicks(dbHelper)));
            mCorrectPicksNumberView.setText(Integer.toString(dbHelper.getTotalCorrectPicks(dbHelper)));
            mCorrectPercentageNumberView.setText(
                    String.format("%.1f",
                            ((double)dbHelper.getTotalCorrectPicks(dbHelper) / (double)dbHelper.getTotalNumberOfPicks(dbHelper))*100 ) + '%');

            ArrayList<Integer> gameIdsWithNoneAsResult = dbHelper.getGameIdsWithNoneAsResult(dbHelper);
            TextView numWithNoneAsResultView = (TextView)findViewById(R.id.gameIdsWithNoneAsResultTextView);
            numWithNoneAsResultView.setText("Updated number with none as result: " + gameIdsWithNoneAsResult.size());

            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // set up values for required params
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected Void doInBackground(Integer... arg0) {
            //make the web service call here
            //make all of the individual gameId calls
            StringBuilder result = new StringBuilder();
            String currentGameUrl;
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(getBaseContext());

            for (Integer gameId: arg0) {
                try {
                    currentGameUrl = String.format("https://statsapi.web.nhl.com/api/v1/game/%d/feed/live", gameId);
                    URL url = new URL(currentGameUrl);
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
                    try {
                        JSONObject jObj = new JSONObject(result.toString());
                        result.setLength(0);
                        // if the game is over then update the game,
                        // otherwise, do nothing and pass to the next game
                        if (jObj.getJSONObject("gameData").getJSONObject("status").getString("detailedState").equalsIgnoreCase("Final")) {

                            // determine the winner from the JSON
                            int awayTeamScore = jObj
                                    .getJSONObject("liveData")
                                    .getJSONObject("linescore")
                                    .getJSONObject("teams")
                                    .getJSONObject("away")
                                    .getInt("goals");
                            int homeTeamScore = jObj
                                    .getJSONObject("liveData")
                                    .getJSONObject("linescore")
                                    .getJSONObject("teams")
                                    .getJSONObject("home")
                                    .getInt("goals");
                            String winner;
                            if (awayTeamScore > homeTeamScore)
                                winner = "away";
                            else
                                winner = "home";

                            // get currentSelection for the pick to update
                            Cursor cursor;
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            String sql = "SELECT * FROM picks WHERE gameId=" + gameId;
                            cursor = db.rawQuery(sql, null);
                            cursor.moveToFirst();
                            String currentSelection = cursor.getString(cursor.getColumnIndex("selection"));
                            cursor.close();

                            // update the pick
                            dbHelper.updatePickInDatabase(gameId, currentSelection, winner, dbHelper);

                        }
                    } catch (Exception e) {

                    }
                }
            }
            return null;
        }
    }
}
