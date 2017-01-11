package com.feedthewolf.nhlpickem;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ListView mGameList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private String currentUrlString;
    private JSONObject jObj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //currentUrlString = "https://statsapi.web.nhl.com/api/v1/teams/30";
        Date date = new Date();
        //String apiDateToday = "2017-01-09";
        String apiDateToday = new SimpleDateFormat("yyyy-MM-dd").format(date);
        currentUrlString = String.format("https://statsapi.web.nhl.com/api/v1/schedule?startDate=%s&endDate=%s&expand=schedule.teams,schedule.linescore", apiDateToday, apiDateToday);

        JsonParser parser = new JsonParser();
        parser.execute(currentUrlString);


        // add hamburger menu to top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        setupDrawer();
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // set up drawer
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        addDrawerItems();

        mGameList = (ListView)findViewById(R.id.game_list);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addGamesToList(ArrayList<Game> objects) {
        GameAdapter gameAdapter = new GameAdapter(this, objects);
        mGameList.setAdapter(gameAdapter);
    }

    private class JsonParser extends AsyncTask<String, Void, String> {

        final String TAG = "JsonParser";

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
                jObj = new JSONObject(result);
                //Toast.makeText(MainActivity.this, "From API: " + jObj.getJSONArray("teams").getJSONObject(0).getString("name"), Toast.LENGTH_LONG).show();
                int numberOfGames = jObj.getInt("totalGames");

                ArrayList<Game> gameList = new ArrayList<>();
                for(int currentGame = 0; currentGame < numberOfGames; currentGame++) {
                    JSONObject currentGameJSON = jObj.getJSONArray("dates").getJSONObject(0).getJSONArray("games").getJSONObject(currentGame);
                    gameList.add(gameFromJSON(currentGameJSON));
                }

                addGamesToList(gameList);

            } catch (Exception e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }
        }

        private Game gameFromJSON (JSONObject gameJSON) {

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

                return new Game(gameDate, gameStatus, awayTeam, homeTeam, currentPeriodOrdinal, currentPeriodTimeRemaining);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing game data " + e.toString());
                return null;
            }
        }
    }

}
