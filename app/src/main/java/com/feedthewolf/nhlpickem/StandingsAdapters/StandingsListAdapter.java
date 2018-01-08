package com.feedthewolf.nhlpickem.StandingsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feedthewolf.nhlpickem.R;
import com.feedthewolf.nhlpickem.TeamStanding;

import java.util.ArrayList;

/**
 * Created by Nick on 1/5/2018.
 */

public class StandingsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<TeamStanding> mDataSource;

    public StandingsListAdapter(Context context, ArrayList<TeamStanding> items) {
        mDataSource = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.fragment_standings_list_item, viewGroup, false);

        TextView teamNameView =     (TextView) rowView.findViewById(R.id.teamName);
        TextView gamesPlayedView =  (TextView) rowView.findViewById(R.id.gamesPlayed);
        TextView winsView =         (TextView) rowView.findViewById(R.id.wins);
        TextView lossesView =       (TextView) rowView.findViewById(R.id.losses);
        TextView otView =           (TextView) rowView.findViewById(R.id.ot);
        TextView pointsView =       (TextView) rowView.findViewById(R.id.points);

        final TeamStanding teamStanding = (TeamStanding) getItem(i);

        teamNameView.setText(teamStanding.teamName);
        gamesPlayedView.setText(Integer.toString(teamStanding.gamesPlayed));
        winsView.setText(Integer.toString(teamStanding.teamRecord.getWins()));
        lossesView.setText(Integer.toString(teamStanding.teamRecord.getLosses()));
        otView.setText(Integer.toString(teamStanding.teamRecord.getOt()));
        pointsView.setText(Integer.toString(teamStanding.points));

        return rowView;
    }
}
