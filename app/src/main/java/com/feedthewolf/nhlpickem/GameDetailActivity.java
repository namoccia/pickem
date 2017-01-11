package com.feedthewolf.nhlpickem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class GameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        Intent intent = getIntent();

        Game game = intent.getParcelableExtra("TEST_PAR_ABLE");

        setAwayTeamBox(game);
        setHomeTeamBox(game);
        /*
        String message = intent.getStringExtra("TEST_EXTRA");
        message = game.getAwayTeam().toString();
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_game_detail);
        layout.addView(textView);
        */


    }

    protected void setAwayTeamBox (Game game) {
        ImageView awayLogo = (ImageView) findViewById(R.id.awayTeamImageView);
        TextView awayTeamNameView = (TextView) findViewById(R.id.awayTeamNameTextView);
        TextView awayTeamRecordView = (TextView) findViewById(R.id.awayTeamRecordTextView);

        TextView awayTeamScore = (TextView) findViewById(R.id.awayTeamScoreTextView);


        awayLogo.setImageResource(GameAdapter.getImageResourceIdByTeamId(game.getAwayTeam().getId()));
        awayTeamNameView.setText(game.getAwayTeam().getName());
        awayTeamRecordView.setText(game.getAwayTeam().getLeagueRecord().toString());

        if (game.getStatus().equalsIgnoreCase("Preview")) {
            awayTeamScore.setText("");
        }
        else {
            awayTeamScore.setText(Integer.toString(game.getAwayTeam().getScore()));
        }

    }

    protected void setHomeTeamBox(Game game) {
        ImageView homeLogo = (ImageView) findViewById(R.id.homeTeamImageView);
        TextView homeTeamNameView = (TextView) findViewById(R.id.homeTeamNameTextView);
        TextView homeTeamRecordView = (TextView) findViewById(R.id.homeTeamRecordTextView);

        TextView homeTeamScore = (TextView) findViewById(R.id.homeTeamScoreTextView);


        homeLogo.setImageResource(GameAdapter.getImageResourceIdByTeamId(game.getHomeTeam().getId()));
        homeTeamNameView.setText(game.getHomeTeam().getName());
        homeTeamRecordView.setText(game.getHomeTeam().getLeagueRecord().toString());

        if (game.getStatus().equalsIgnoreCase("Preview")) {
            homeTeamScore.setText("");
        }
        else {
            homeTeamScore.setText(Integer.toString(game.getHomeTeam().getScore()));
        }
    }
}
