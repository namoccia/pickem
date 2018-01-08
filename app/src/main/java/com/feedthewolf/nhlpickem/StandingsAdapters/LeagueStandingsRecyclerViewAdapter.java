package com.feedthewolf.nhlpickem.StandingsAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.feedthewolf.nhlpickem.R;
import com.feedthewolf.nhlpickem.TeamStanding;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Nick on 1/5/2018.
 */

public class LeagueStandingsRecyclerViewAdapter extends RecyclerView.Adapter<LeagueStandingsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<TeamStanding> mLeagueStandings;

    public LeagueStandingsRecyclerViewAdapter(ArrayList<TeamStanding> items) {
        mLeagueStandings = items;
        Collections.sort(mLeagueStandings, TeamStanding.LeagueRankComparator);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_standings_league, parent, false);

        ListView mLeagueStandingsListView = (ListView) testView.findViewById(R.id.league_standings_list);

        StandingsListAdapter leagueStandingsListAdapter = new StandingsListAdapter(testView.getContext(), mLeagueStandings);

        mLeagueStandingsListView.setAdapter(leagueStandingsListAdapter);

        return new ViewHolder(testView);
    }

    @Override
    public void onBindViewHolder(LeagueStandingsRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewHolder(View view) {
            super(view);
        }
    }
}
