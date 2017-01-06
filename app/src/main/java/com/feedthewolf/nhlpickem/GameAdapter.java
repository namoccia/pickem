package com.feedthewolf.nhlpickem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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

        Game game = (Game) getItem(position);

        awayTextView.setText(game.getAwayTeam().toString());
        homeTextView.setText(game.getHomeTeam().toString());

        return rowView;
    }
}
