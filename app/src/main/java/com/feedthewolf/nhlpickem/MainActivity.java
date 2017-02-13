package com.feedthewolf.nhlpickem;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private ListView mGameList;
    private TextView mEmptyListView;
    private TextView mDateHeading;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private String mActivityTitle;
    private String mCurrentUrlString;
    private String mApiDate;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePrivates();

        GameListJsonParser parser = new GameListJsonParser();
        mSwipeRefreshLayout.setRefreshing(true);
        parser.execute(mCurrentUrlString);

        updateDateHeading();
        setupDrawer();
        createNotificationManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Date date = new Date();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String todaysDateString = new SimpleDateFormat("yyyy-MM-dd").format(date);

        if(!mSharedPreferences.getString(getString(R.string.preference_today_date), "null").equalsIgnoreCase(todaysDateString)) {
            // if app has not been destroyed before a new date occurs, then the app update the date
            // and reminder preferences during onStart
            editor.putString(getString(R.string.preference_today_date), todaysDateString);
            editor.putBoolean(getString(R.string.preference_has_pick_reminder_been_sent_today), false);
        }
        editor.apply();

        updateDateHeading();
        refreshGameList();
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        mApiDate = String.format("%d-%02d-%d", year, month+1, day);
        updateDateHeading();
        refreshGameList();
    }

    private void initializePrivates() {
        //mCurrentUrlString = "https://statsapi.web.nhl.com/api/v1/teams/30";
        Date date = new Date();
        //mApiDate = "2017-01-14";
        mApiDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        if(mSharedPreferences.getString(getString(R.string.preference_today_date), "null").equalsIgnoreCase("null")) {
            // if todayDate preference does not exist, initialize it and hasRemindedToday
            editor.putString(getString(R.string.preference_today_date), mApiDate);
            editor.putBoolean(getString(R.string.preference_has_pick_reminder_been_sent_today), false);
        }
        else if(mSharedPreferences.getString(getString(R.string.preference_today_date), "null").equalsIgnoreCase(mApiDate)){
            // if todayDate has already been set to today

            /*
            boolean test = !mSharedPreferences.getBoolean(getString(R.string.preference_has_pick_reminder_been_sent_today), false);
            if(!mSharedPreferences.getBoolean(getString(R.string.preference_has_pick_reminder_been_sent_today), false)) {

            }
            */
        }
        else {
            // if todayDate has been initialized and does not equal today, then set hasRemindedToday to false
            editor.putString(getString(R.string.preference_today_date), mApiDate);
            editor.putBoolean(getString(R.string.preference_has_pick_reminder_been_sent_today), false);
        }
        editor.apply();

        mCurrentUrlString = String.format("https://statsapi.web.nhl.com/api/v1/schedule?startDate=%s&endDate=%s&expand=schedule.teams,schedule.linescore", mApiDate, mApiDate);
        mDateHeading = (TextView) findViewById(R.id.date_heading_text_view);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mGameList = (ListView)findViewById(R.id.game_list);
        mEmptyListView = (TextView)findViewById(R.id.empty_game_list);

        mGameList.setEmptyView(mEmptyListView);

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

    private void createNotificationManager() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 25);

        Intent intent = new Intent(getApplicationContext(), Notification_Reciever.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void addDrawerItems() {
        String[] osArray = { "Stats", "Settings", "About" };
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        // add hamburger menu to top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        addDrawerItems();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Intent statsIntent = new Intent(MainActivity.this, StatsActivity.class);
                        MainActivity.this.startActivity(statsIntent);
                        break;
                    case 1:
                        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        MainActivity.this.startActivity(settingsIntent);
                        break;
                    case 2:
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        MainActivity.this.startActivity(aboutIntent);
                        break;
                }

            }
        });
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void addGamesToList(ArrayList<Game> objects) {
        GameAdapter gameAdapter = new GameAdapter(this, objects);
        mGameList.setAdapter(gameAdapter);
    }

    private void refreshGameList() {
        mCurrentUrlString = String.format("https://statsapi.web.nhl.com/api/v1/schedule?startDate=%s&endDate=%s&expand=schedule.teams,schedule.linescore", mApiDate, mApiDate);

        GameListJsonParser parser = new GameListJsonParser();
        mSwipeRefreshLayout.setRefreshing(true);
        parser.execute(mCurrentUrlString);
    }

    private void updateDateHeading() {
        DateFormat apiDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd");
        try {
            Date selectedListDate = apiDateFormat.parse(mApiDate);
            Date dateToday = new Date();
            String headingDate = new SimpleDateFormat("MMM, d").format(selectedListDate);
            String headingDateToday = new SimpleDateFormat("MMM, d").format(dateToday);

            if( headingDate.equalsIgnoreCase(headingDateToday)) {
                mDateHeading.setText(R.string.today);
            }
            else {
                mDateHeading.setText(headingDate);
            }
        } catch (ParseException e) {
            mDateHeading.setText(mApiDate);
            e.printStackTrace();
        }
    }

    public void onLeftChevronClick(View view) {
        //Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();

        DateFormat apiDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd");
        try {
            Date selectedListDate = apiDateFormat.parse(mApiDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedListDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            mApiDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
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
            Date selectedListDate = apiDateFormat.parse(mApiDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedListDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

            mApiDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
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
                if( mSharedPreferences.getString(getString(R.string.preference_today_date), "null").equalsIgnoreCase(mApiDate)) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    if (numberOfGames == 0)
                        editor.putBoolean(getString(R.string.preference_are_there_games_today), false);
                    else
                        editor.putBoolean(getString(R.string.preference_are_there_games_today), true);

                    editor.apply();
                }

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
