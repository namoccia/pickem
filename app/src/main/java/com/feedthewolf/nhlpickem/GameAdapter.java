package com.feedthewolf.nhlpickem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
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

        // Get time element
        TextView timeTextView = (TextView) rowView.findViewById(R.id.game_list_time_text);

        // Get image elements
        ImageView awayLogo = (ImageView) rowView.findViewById(R.id.game_list_away_image);
        ImageView homeLogo = (ImageView) rowView.findViewById(R.id.game_list_home_image);

        Game game = (Game) getItem(position);

        SimpleDateFormat df = new SimpleDateFormat("h:mm a");
        String timeString = df.format(game.getDate());
        //test.

        awayTextView.setText(game.getAwayTeam().toString());
        homeTextView.setText(game.getHomeTeam().toString());
        timeTextView.setText(timeString);
        awayLogo.setImageResource(getImageResourceIdByTeamId(game.getAwayTeam().getId()));
        homeLogo.setImageResource(getImageResourceIdByTeamId(game.getHomeTeam().getId()));

        return rowView;
    }

    public int getImageResourceIdByTeamId(int teamId) {
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
}
