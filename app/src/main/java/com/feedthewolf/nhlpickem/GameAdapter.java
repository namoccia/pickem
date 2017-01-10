package com.feedthewolf.nhlpickem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nmoccia on 1/6/2017.
 */

public class GameAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Game> mDataSource;

    public GameAdapter(Context context, ArrayList<Game> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_game, parent, false);

        // Get away element
        TextView awayTextView = (TextView) rowView.findViewById(R.id.game_list_away_text);

        // Get home element
        TextView homeTextView = (TextView) rowView.findViewById(R.id.game_list_home_text);

        // Get score element
        TextView scoreTextView = (TextView) rowView.findViewById(R.id.game_list_score_text);

        // Get time element
        TextView timeTextView = (TextView) rowView.findViewById(R.id.game_list_time_text);

        // Get image elements
        ImageView awayLogo = (ImageView) rowView.findViewById(R.id.game_list_away_image);
        ImageView homeLogo = (ImageView) rowView.findViewById(R.id.game_list_home_image);

        final Game game = (Game) getItem(position);

        awayTextView.setText(game.getAwayTeam().toString());
        homeTextView.setText(game.getHomeTeam().toString());
        scoreTextView.setText(scoreText(game));
        timeTextView.setText(timeText(game));
        awayLogo.setImageResource(getImageResourceIdByTeamId(game.getAwayTeam().getId()));
        homeLogo.setImageResource(getImageResourceIdByTeamId(game.getHomeTeam().getId()));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do your stuff
                Intent intent = new Intent(view.getContext(), GameDetailActivity.class);
                intent.putExtra("TEST_EXTRA", game.toString());
                intent.putExtra("TEST_PAR_ABLE", game);
                mContext.startActivity(intent);
            }
        });

        return rowView;
    }

    public static int getImageResourceIdByTeamId(int teamId) {
        switch (teamId) {
            case 21:
                return R.drawable.ic_team_avalanche;
            case 16:
                return R.drawable.ic_team_blackhawks;
            case 29:
                return R.drawable.ic_team_blue_jackets;
            case 19:
                return R.drawable.ic_team_blues;
            case 6:
                return R.drawable.ic_team_bruins;
            case 8:
                return R.drawable.ic_team_canadiens;
            case 23:
                return R.drawable.ic_team_canucks;
            case 15:
                return R.drawable.ic_team_capitals;
            case 53:
                return R.drawable.ic_team_coyotes;
            case 1:
                return R.drawable.ic_team_devils;
            case 24:
                return R.drawable.ic_team_ducks;
            case 20:
                return R.drawable.ic_team_flames;
            case 4:
                return R.drawable.ic_team_flyers;
            case 12:
                return R.drawable.ic_team_hurricanes;
            case 2:
                return R.drawable.ic_team_islanders;
            case 52:
                return R.drawable.ic_team_jets;
            case 26:
                return R.drawable.ic_team_kings;
            case 14:
                return R.drawable.ic_team_lightning;
            case 10:
                return R.drawable.ic_team_maple_leafs;
            case 22:
                return R.drawable.ic_team_oilers;
            case 13:
                return R.drawable.ic_team_panthers;
            case 5:
                return R.drawable.ic_team_penguins;
            case 18:
                return R.drawable.ic_team_predators;
            case 3:
                return R.drawable.ic_team_rangers;
            case 17:
                return R.drawable.ic_team_red_wings;
            case 7:
                return R.drawable.ic_team_sabres;
            case 9:
                return R.drawable.ic_team_senators;
            case 28:
                return R.drawable.ic_team_sharks;
            case 25:
                return R.drawable.ic_team_stars;
            case 30:
                return R.drawable.ic_team_wild;
            default:
                return R.drawable.ic_team_wild;
        }
    }

    private String timeText(Game game) {
        String output = "";

        // if game date is in the future then show the game time
        if (game.getDate().compareTo(new Date()) > 0) {
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
            output = df.format(game.getDate());
        }
        else {
            if (game.getStatus().compareToIgnoreCase("Final") == 0)
            {
                output = "Final";
            }
            else {
                output = "Playing";
            }

        }

        return output;
    }

    private SpannableString scoreText(Game game) {
        SpannableString output = new SpannableString("");

        // if game date is in the future then show no score
        if (game.getDate().compareTo(new Date()) > 0) {
            return output;
        }
        else {
            SpannableString str = new SpannableString(String.format("%d\n%d", game.getAwayTeam().getScore(), game.getHomeTeam().getScore()));

            if (game.getAwayTeam().getScore() > game.getHomeTeam().getScore()) {
                str.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if (game.getAwayTeam().getScore() < game.getHomeTeam().getScore()) {
                str.setSpan(new StyleSpan(Typeface.BOLD), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            output = str;
        }

        return output;
    }
}
