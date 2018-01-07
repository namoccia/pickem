package com.feedthewolf.nhlpickem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feedthewolf.nhlpickem.StandingsAdapters.DivisionStandingsRecyclerViewAdapter;
import com.feedthewolf.nhlpickem.StandingsAdapters.WildcardStandingsRecyclerViewAdapter;

import java.util.ArrayList;

public class TeamStandingFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_STANDINGS_LIST = "standings-list";
    private static final String ARG_POSITION = "position";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public ArrayList<TeamStanding> mTeamStandingList;
    public int mStandingsType;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamStandingFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TeamStandingFragment newInstance(ArrayList<TeamStanding> standingsList, int position) {
        TeamStandingFragment fragment = new TeamStandingFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_STANDINGS_LIST, standingsList);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTeamStandingList = getArguments().getParcelableArrayList(ARG_STANDINGS_LIST);
            mStandingsType = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teamstanding_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView && mTeamStandingList != null) {
            RecyclerView recyclerView = (RecyclerView) view;

            switch(mStandingsType){
                case 0:
                    recyclerView.setAdapter(new WildcardStandingsRecyclerViewAdapter(mTeamStandingList));
                    view.setFocusable(false);
                    return view;
                case 1:
                    recyclerView.setAdapter(new DivisionStandingsRecyclerViewAdapter(mTeamStandingList));
                    view.setFocusable(false);
                    return view;
                default:
                    recyclerView.setAdapter(new MyTeamStandingRecyclerViewAdapter(mTeamStandingList));
            }
        }
        view.setFocusable(false);

        return view;
    }
}
