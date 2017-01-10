package com.feedthewolf.nhlpickem;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        Intent intent = getIntent();
        String message = intent.getStringExtra("TEST_EXTRA");
        Game par_able = intent.getParcelableExtra("TEST_PAR_ABLE");
        message = par_able.getAwayTeam().toString();
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_game_detail);
        layout.addView(textView);
    }
}
