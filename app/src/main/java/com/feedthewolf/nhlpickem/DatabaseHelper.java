package com.feedthewolf.nhlpickem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nmoccia on 1/12/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "pick_em_db";
    private static final String DATABASE_TABLE_PICKS = "picks";
    private static final String DATABASE_TABLE_STATS = "stats";

    private static final String PICKS_GAME_ID = "gameId";
    private static final String PICKS_SELECTION = "selection";
    private static final String PICKS_RESULT = "result";

    private static final String SQL_CREATE_PICKS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_PICKS + " (" +
            PICKS_GAME_ID + " INTEGER PRIMARY KEY, " +
            PICKS_SELECTION + " TEXT, " +
            PICKS_RESULT + " TEXT)";

    private static final String SQL_DELETE_PICKS_TABLE =
            "DROP TABLE IF EXISTS " + DATABASE_TABLE_PICKS;

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PICKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PICKS_TABLE);
        onCreate(db);
    }
}
