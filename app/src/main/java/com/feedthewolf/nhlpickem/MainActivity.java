package com.feedthewolf.nhlpickem;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private ListView mDrawerList;
    private ListView mGameList;
    private TextView mDateHeading;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private String currentUrlString;

    private String apiDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //currentUrlString = "https://statsapi.web.nhl.com/api/v1/teams/30";
        Date date = new Date();
        //String apiDate = "2017-01-12";
        apiDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        currentUrlString = String.format("https://statsapi.web.nhl.com/api/v1/schedule?startDate=%s&endDate=%s&expand=schedule.teams,schedule.linescore", apiDate, apiDate);

        GameListJsonParser parser = new GameListJsonParser();
        parser.execute(currentUrlString);

        mDateHeading = (TextView) findViewById(R.id.date_heading_text_view);
        updateDateHeading();

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshGameList);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Log.i("REFRESH", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        refreshGameList();
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshGameList();
    }

    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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

        if (item.getItemId() == R.id.action_date_pick) {
            showDatePickerDialog();
            refreshGameList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void addGamesToList(ArrayList<Game> objects) {
        GameAdapter gameAdapter = new GameAdapter(this, objects);
        mGameList.setAdapter(gameAdapter);
    }

    private void refreshGameList()
    {
        currentUrlString = String.format("https://statsapi.web.nhl.com/api/v1/schedule?startDate=%s&endDate=%s&expand=schedule.teams,schedule.linescore", apiDate, apiDate);

        GameListJsonParser parser = new GameListJsonParser();
        parser.execute(currentUrlString);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        apiDate = String.format("%d-%02d-%d", year, month+1, day);
        updateDateHeading();
        refreshGameList();
    }

    public void updateDateHeading() {
        DateFormat apiDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd");
        try {
            Date selectedListDate = apiDateFormat.parse(apiDate);
            String headingDate = new SimpleDateFormat("MMM, d").format(selectedListDate);

            mDateHeading.setText(headingDate);
        } catch (ParseException e) {
            mDateHeading.setText(apiDate);
            e.printStackTrace();
        }
    }

    public void onLeftChevronClick(View view) {
        //Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();

        DateFormat apiDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd");
        try {
            Date selectedListDate = apiDateFormat.parse(apiDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedListDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            apiDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            updateDateHeading();
            refreshGameList();
            //selectedListDate.
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onRightChevronClick(View view) {
        //Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();

        DateFormat apiDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd");
        try {
            Date selectedListDate = apiDateFormat.parse(apiDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedListDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            apiDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            updateDateHeading();
            refreshGameList();
            //selectedListDate.
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private class GameListJsonParser extends AsyncTask<String, Void, String> {

        final String TAG = "GameListJsonParser";

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

                addGamesToList(gameList);

                mSwipeRefreshLayout.setRefreshing(false);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }
        }
    }

}
