package com.feedthewolf.nhlpickem;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nmoccia on 1/6/2017.
 */

class GameAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Game> mDataSource;
    private DatabaseHelper dbHelper;

    GameAdapter(Context context, ArrayList<Game> items) {
        mContext = context;
        mDataSource = items;
        dbHelper = DatabaseHelper.getInstance(mContext);
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

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        if(width < 1080) {
            awayTextView.setText(game.getAwayTeam().getName());
            homeTextView.setText(game.getHomeTeam().getName());
        }
        else {
            awayTextView.setText(game.getAwayTeam().toString());
            homeTextView.setText(game.getHomeTeam().toString());
        }

        scoreTextView.setText(scoreText(game));
        timeTextView.setText(timeText(game));
        awayLogo.setImageResource(getImageResourceIdByTeamId(game.getAwayTeam().getId()));
        homeLogo.setImageResource(getImageResourceIdByTeamId(game.getHomeTeam().getId()));

        //awayLogo.setVisibility(View.GONE);
        //homeLogo.setVisibility(View.GONE);

        rowView.setBackgroundColor(ContextCompat.getColor(mContext, getColorResourceByPickStatus(game)));

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

    private int getColorResourceByPickStatus(Game game) {
        if(dbHelper.pickEntryAlreadyExistsForGameId(game.getGameId(), dbHelper) && game.hasGameFinished()) {
            Cursor cursor;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "SELECT * FROM picks WHERE gameId=" + game.getGameId();
            cursor = db.rawQuery(sql, null);

            cursor.moveToFirst();
            String currentSelection = cursor.getString(cursor.getColumnIndex("selection"));
            cursor.close();

            String winner = game.getWinnerForDatabase();

            if (!currentSelection.equalsIgnoreCase("none") && !winner.equalsIgnoreCase("none")) {
                dbHelper.updatePickInDatabase(game.getGameId(), currentSelection, winner, dbHelper);

                if (currentSelection.equalsIgnoreCase(winner)) {
                    return R.color.correctPickListBackground;
                } else {
                    return R.color.incorrectPickListBackground;
                }
            }
            else {
                return R.color.windowBackground;
            }
        }
        else {
            return R.color.windowBackground;
        }
    }

    static int getImageResourceIdByTeamId(int teamId) {
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
            case 54:
                return R.drawable.ic_team_golden_knights;
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
            case 87:
                return R.drawable.ic_team_all_star_atlantic;
            case 88:
                return R.drawable.ic_team_all_star_metro;
            case 89:
                return R.drawable.ic_team_all_star_central;
            case 90:
                return R.drawable.ic_team_all_star_pacific;
            default:
                return R.drawable.ic_team_wild;
        }
    }

    private String timeText(Game game) {
        String output;

        // if game date is in the future then show the game time
        if (game.getDate().compareTo(new Date()) > 0) {
            SimpleDateFormat df = new SimpleDateFormat("h:mm a");
            output = df.format(game.getDate());
        }
        else {
            if (game.getStatus().compareToIgnoreCase("Final") == 0)
            {
                if (game.getCurrentPeriodOrdinal().contains("OT") || game.getCurrentPeriodOrdinal().contains("SO")) {
                    output = String.format("%s\n%s", "Final", game.getCurrentPeriodOrdinal());
                }
                else {
                    output = "Final";
                }
            }
            else {
                if (game.getCurrentPeriodOrdinal().contains("SO")) {
                    output = game.getCurrentPeriodOrdinal();
                }
                else {
                    output = String.format("%s\n%s", game.getCurrentPeriodOrdinal(), game.getCurrentPeriodTimeRemaining());
                }
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
                str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.toString().indexOf('\n'), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if (game.getAwayTeam().getScore() < game.getHomeTeam().getScore()) {
                str.setSpan(new StyleSpan(Typeface.BOLD), 2, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            output = str;
        }

        return output;
    }
}
