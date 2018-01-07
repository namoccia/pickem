package com.feedthewolf.nhlpickem.StandingsAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.feedthewolf.nhlpickem.R;
import com.feedthewolf.nhlpickem.TeamStanding;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Nick on 1/5/2018.
 */

public class WildcardStandingsRecyclerViewAdapter extends RecyclerView.Adapter<WildcardStandingsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<TeamStanding> mMetroDivisionLeaders;
    private ArrayList<TeamStanding> mAtlanticDivisionLeaders;
    private ArrayList<TeamStanding> mCentralDivisionLeaders;
    private ArrayList<TeamStanding> mPacificDivisionLeaders;

    private ArrayList<TeamStanding> mEastWildCard;
    private ArrayList<TeamStanding> mWestWildCard;

    private ArrayList<TeamStanding> mEastNoPlayoffs;
    private ArrayList<TeamStanding> mWestNoPlayoffs;

    public WildcardStandingsRecyclerViewAdapter(ArrayList<TeamStanding> items) {
        mMetroDivisionLeaders = getDivisionLeaderListByDivision(items, "Metropolitan");
        mAtlanticDivisionLeaders = getDivisionLeaderListByDivision(items, "Atlantic");
        mCentralDivisionLeaders = getDivisionLeaderListByDivision(items, "Central");
        mPacificDivisionLeaders = getDivisionLeaderListByDivision(items, "Pacific");

        mEastWildCard = getWildCardTeamsByConference(items, "Eastern");
        mWestWildCard = getWildCardTeamsByConference(items, "Western");

        mEastNoPlayoffs = getNoPlayoffTeamsByConference(items, "Eastern");
        mWestNoPlayoffs = getNoPlayoffTeamsByConference(items, "Western");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentFragment = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_standings_wildcard, parent, false);

        ListView mMetroLeadersListView = (ListView) parentFragment.findViewById(R.id.metro_leaders_list);
        ListView mAtlanticLeadersListView = (ListView) parentFragment.findViewById(R.id.atlantic_leaders_list);
        ListView mCentralLeadersListView = (ListView) parentFragment.findViewById(R.id.central_leaders_list);
        ListView mPacificLeadersListView = (ListView) parentFragment.findViewById(R.id.pacific_leaders_list);

        ListView mEastWildCardListView = (ListView) parentFragment.findViewById(R.id.wildcard_east_list);
        ListView mWestWildCardListView = (ListView) parentFragment.findViewById(R.id.wildcard_west_list);

        ListView mEastNoPlayoffsListView = (ListView) parentFragment.findViewById(R.id.no_playoffs_east_list);
        ListView mWestNoPlayoffsListView = (ListView) parentFragment.findViewById(R.id.no_playoffs_west_list);

        StandingsListAdapter metroLeadersListAdapter = new StandingsListAdapter(parentFragment.getContext(), mMetroDivisionLeaders);
        StandingsListAdapter atlanticLeadersListAdapter = new StandingsListAdapter(parentFragment.getContext(), mAtlanticDivisionLeaders);
        StandingsListAdapter centralLeadersListAdapter = new StandingsListAdapter(parentFragment.getContext(), mCentralDivisionLeaders);
        StandingsListAdapter pacificLeadersListAdapter = new StandingsListAdapter(parentFragment.getContext(), mPacificDivisionLeaders);

        StandingsListAdapter eastWildCardListAdapter = new StandingsListAdapter(parentFragment.getContext(), mEastWildCard);
        StandingsListAdapter westWildCardListAdapter = new StandingsListAdapter(parentFragment.getContext(), mWestWildCard);

        StandingsListAdapter eastNoPlayoffsListAdapter = new StandingsListAdapter(parentFragment.getContext(), mEastNoPlayoffs);
        StandingsListAdapter westNoPlayoffsListAdapter = new StandingsListAdapter(parentFragment.getContext(), mWestNoPlayoffs);

        mMetroLeadersListView.setAdapter(metroLeadersListAdapter);
        mAtlanticLeadersListView.setAdapter(atlanticLeadersListAdapter);
        mCentralLeadersListView.setAdapter(centralLeadersListAdapter);
        mPacificLeadersListView.setAdapter(pacificLeadersListAdapter);

        mEastWildCardListView.setAdapter(eastWildCardListAdapter);
        mWestWildCardListView.setAdapter(westWildCardListAdapter);

        mEastNoPlayoffsListView.setAdapter(eastNoPlayoffsListAdapter);
        mWestNoPlayoffsListView.setAdapter(westNoPlayoffsListAdapter);

        return new ViewHolder(parentFragment);
    }

    @Override
    public void onBindViewHolder(WildcardStandingsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mMetroDivisionHeaderTextView.setText(mMetroDivisionLeaders.get(0).divisionName);
        holder.mAtlanticDivisionHeaderTextView.setText(mAtlanticDivisionLeaders.get(0).divisionName);
        holder.mCentralDivisionHeaderTextView.setText(mCentralDivisionLeaders.get(0).divisionName);
        holder.mPacificDivisionHeaderTextView.setText(mPacificDivisionLeaders.get(0).divisionName);

        holder.mEastWildCardHeaderTextView.setText("Wild Card");
        holder.mWestWildCardHeaderTextView.setText("Wild Card");

        holder.mEastNoPlayoffsHeaderTextView.setText("No Playoffs");
        holder.mWestNoPlayoffsHeaderTextView.setText("No Playoffs");
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMetroDivisionHeaderTextView;
        public final TextView mAtlanticDivisionHeaderTextView;
        public final TextView mCentralDivisionHeaderTextView;
        public final TextView mPacificDivisionHeaderTextView;

        public final TextView mEastWildCardHeaderTextView;
        public final TextView mWestWildCardHeaderTextView;

        public final TextView mEastNoPlayoffsHeaderTextView;
        public final TextView mWestNoPlayoffsHeaderTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mMetroDivisionHeaderTextView = (TextView) view.findViewById(R.id.metroHeading);
            mAtlanticDivisionHeaderTextView = (TextView) view.findViewById(R.id.atlanticHeading);
            mCentralDivisionHeaderTextView = (TextView) view.findViewById(R.id.centralHeading);
            mPacificDivisionHeaderTextView = (TextView) view.findViewById(R.id.pacificHeading);

            mEastWildCardHeaderTextView = (TextView) view.findViewById(R.id.wildCardHeadingEast);
            mWestWildCardHeaderTextView = (TextView) view.findViewById(R.id.wildCardHeadingWest);

            mEastNoPlayoffsHeaderTextView = (TextView) view.findViewById(R.id.noPlayoffsHeadingEast);
            mWestNoPlayoffsHeaderTextView = (TextView) view.findViewById(R.id.noPlayoffsHeadingWest);
        }
    }

    private ArrayList<TeamStanding> getDivisionLeaderListByDivision(ArrayList<TeamStanding> originalList, String divisionName) {
        ArrayList<TeamStanding> retVal = new ArrayList<TeamStanding>();
        int teamCounter = 0;

        for (TeamStanding standing : originalList) {
            if (standing.divisionName.equalsIgnoreCase(divisionName)) {
                teamCounter++;
                retVal.add(standing);
            }
            if (teamCounter == 3) {
                break;
            }
        }

        return retVal;
    }

    private ArrayList<TeamStanding> getWildCardTeamsByConference(ArrayList<TeamStanding> originalList, String conferenceName) {
        ArrayList<TeamStanding> retVal = new ArrayList<TeamStanding>();
        int teamCounter = 0;

        for (TeamStanding standing : originalList) {
            if (standing.conferenceName.equalsIgnoreCase(conferenceName) && (standing.wildCardRank == 1 || standing.wildCardRank == 2)) {
                teamCounter++;
                retVal.add(standing);
            }
            if (teamCounter == 2) {
                break;
            }
        }

        return retVal;
    }

    private ArrayList<TeamStanding> getNoPlayoffTeamsByConference(ArrayList<TeamStanding> originalList, String conferenceName) {
        ArrayList<TeamStanding> retVal = new ArrayList<TeamStanding>();

        for (TeamStanding standing : originalList) {
            if (standing.conferenceName.equalsIgnoreCase(conferenceName) && (standing.wildCardRank > 2)) {
                retVal.add(standing);
            }
        }

        return retVal;
    }
}
