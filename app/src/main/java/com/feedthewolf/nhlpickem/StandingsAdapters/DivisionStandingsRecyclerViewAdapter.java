package com.feedthewolf.nhlpickem.StandingsAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.feedthewolf.nhlpickem.R;
import com.feedthewolf.nhlpickem.TeamStanding;

import java.util.ArrayList;

/**
 * Created by Nick on 1/5/2018.
 */

public class DivisionStandingsRecyclerViewAdapter extends RecyclerView.Adapter<DivisionStandingsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<TeamStanding> mMetroDivision;
    private ArrayList<TeamStanding> mAtlanticDivision;
    private ArrayList<TeamStanding> mCentralDivision;
    private ArrayList<TeamStanding> mPacificDivision;

    public DivisionStandingsRecyclerViewAdapter(ArrayList<TeamStanding> items) {
        mMetroDivision = getListByDivision(items, "Metropolitan");
        mAtlanticDivision = getListByDivision(items, "Atlantic");
        mCentralDivision = getListByDivision(items, "Central");
        mPacificDivision = getListByDivision(items, "Pacific");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_standings_division, parent, false);

        ListView mDivisionOneListView = (ListView) testView.findViewById(R.id.standing_list_1);
        ListView mDivisionTwoListView = (ListView) testView.findViewById(R.id.standing_list_2);
        ListView mDivisionThreeListView = (ListView) testView.findViewById(R.id.standing_list_3);
        ListView mDivisionFourListView = (ListView) testView.findViewById(R.id.standing_list_4);

        StandingsListAdapter metroStandingsListAdapter = new StandingsListAdapter(testView.getContext(), mMetroDivision);
        StandingsListAdapter atlanticStandingsListAdapter = new StandingsListAdapter(testView.getContext(), mAtlanticDivision);
        StandingsListAdapter centralStandingsListAdapter = new StandingsListAdapter(testView.getContext(), mCentralDivision);
        StandingsListAdapter pacificStandingsListAdapter = new StandingsListAdapter(testView.getContext(), mPacificDivision);

        mDivisionOneListView.setAdapter(metroStandingsListAdapter);
        mDivisionTwoListView.setAdapter(atlanticStandingsListAdapter);
        mDivisionThreeListView.setAdapter(centralStandingsListAdapter);
        mDivisionFourListView.setAdapter(pacificStandingsListAdapter);

        mDivisionOneListView.setFocusable(false);
        mDivisionTwoListView.setFocusable(false);
        mDivisionThreeListView.setFocusable(false);
        mDivisionFourListView.setFocusable(false);
        testView.setFocusable(false);

        return new ViewHolder(testView);
    }

    @Override
    public void onBindViewHolder(DivisionStandingsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mDivisionHeaderTextView1.setText(mMetroDivision.get(0).divisionName);
        holder.mDivisionHeaderTextView2.setText(mAtlanticDivision.get(0).divisionName);
        holder.mDivisionHeaderTextView3.setText(mCentralDivision.get(0).divisionName);
        holder.mDivisionHeaderTextView4.setText(mPacificDivision.get(0).divisionName);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mDivisionHeaderTextView1;
        private final TextView mDivisionHeaderTextView2;
        private final TextView mDivisionHeaderTextView3;
        private final TextView mDivisionHeaderTextView4;

        private ViewHolder(View view) {
            super(view);
            mDivisionHeaderTextView1 = (TextView) view.findViewById(R.id.divNameHeading1);
            mDivisionHeaderTextView2 = (TextView) view.findViewById(R.id.divNameHeading2);
            mDivisionHeaderTextView3 = (TextView) view.findViewById(R.id.divNameHeading3);
            mDivisionHeaderTextView4 = (TextView) view.findViewById(R.id.divNameHeading4);
        }
    }

    private ArrayList<TeamStanding> getListByDivision(ArrayList<TeamStanding> originalList, String divisionName) {
        ArrayList<TeamStanding> retVal = new ArrayList<TeamStanding>();

        for (TeamStanding standing : originalList) {
            if (standing.divisionName.equalsIgnoreCase(divisionName)) {
                retVal.add(standing);
            }
        }

        return retVal;
    }
}
