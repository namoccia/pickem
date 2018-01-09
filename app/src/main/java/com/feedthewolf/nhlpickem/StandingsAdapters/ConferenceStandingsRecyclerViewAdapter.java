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

public class ConferenceStandingsRecyclerViewAdapter extends RecyclerView.Adapter<ConferenceStandingsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<TeamStanding> mEastConf;
    private ArrayList<TeamStanding> mWestConf;

    public ConferenceStandingsRecyclerViewAdapter(ArrayList<TeamStanding> items) {
        mEastConf = getListByConference(items, "Eastern");
        mWestConf = getListByConference(items, "Western");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_standings_conference, parent, false);

        ListView mEasternListView = (ListView) testView.findViewById(R.id.eastern_conf_list);
        ListView mWesternListView = (ListView) testView.findViewById(R.id.western_conf_list);

        StandingsListAdapter eastStandingsListAdapter = new StandingsListAdapter(testView.getContext(), mEastConf);
        StandingsListAdapter westStandingsListAdapter = new StandingsListAdapter(testView.getContext(), mWestConf);

        mEasternListView.setAdapter(eastStandingsListAdapter);
        mWesternListView.setAdapter(westStandingsListAdapter);

        mEasternListView.setFocusable(false);
        mWesternListView.setFocusable(false);
        testView.setFocusable(false);

        return new ViewHolder(testView);
    }

    @Override
    public void onBindViewHolder(ConferenceStandingsRecyclerViewAdapter.ViewHolder holder, int position) {

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

    private ArrayList<TeamStanding> getListByConference(ArrayList<TeamStanding> originalList, String conferenceName) {
        ArrayList<TeamStanding> retVal = new ArrayList<TeamStanding>();

        for (TeamStanding standing : originalList) {
            if (standing.conferenceName.equalsIgnoreCase(conferenceName)) {
                retVal.add(standing);
            }
        }
        Collections.sort(retVal, TeamStanding.ConferenceRankComparator);

        return retVal;
    }
}
