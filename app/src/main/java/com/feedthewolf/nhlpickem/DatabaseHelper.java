package com.feedthewolf.nhlpickem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by nmoccia on 1/12/2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {

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

    static synchronized DatabaseHelper getInstance(Context context) {

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

    boolean pickEntryAlreadyExistsForGameId(int gameId, DatabaseHelper dbHelper) {
        Cursor cursor;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT gameId FROM picks WHERE gameId=" + gameId;
        cursor = db.rawQuery(sql, null);
        int cursorCount = cursor.getCount();
        cursor.close();

        return cursorCount>0;
    }

    int getTotalNumberOfPicks(DatabaseHelper dbHelper) {
        Cursor cursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT count(*) FROM picks WHERE selection<>'none' AND " +
                        "(result='home' OR result='away')";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int cursorCount = cursor.getInt(0);
        cursor.close();

        return cursorCount;
    }

    int getTotalCorrectPicks(DatabaseHelper dbHelper) {
        Cursor cursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int numberCorrect = 0;

        String sql = "SELECT * FROM picks WHERE selection<>'none' AND result<>'none'";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("selection"))
                    .equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("result")))) {
                numberCorrect++;
            }
            cursor.moveToNext();
        }
        cursor.close();

        return numberCorrect;
    }

    ArrayList<Integer> getGameIdsWithNoneAsResult(DatabaseHelper dbHelper) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        Cursor cursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT gameId FROM picks WHERE selection<>'none' AND result='none'";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            result.add(cursor.getInt(cursor.getColumnIndex("gameId")));
            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }

    void updatePickInDatabase(int gameId, String currentSelection, String resultOfGame, DatabaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(dbHelper.pickEntryAlreadyExistsForGameId(gameId, dbHelper)){
            //PID Found

            ContentValues values = new ContentValues();
            values.put("selection", currentSelection);
            values.put("result", resultOfGame);

            // Which row to update, based on the title
            String selection = "gameId=" + gameId;

            db.update("picks", values, selection, null);
        }else{
            //PID Not Found

            ContentValues values = new ContentValues();
            values.put("gameId", gameId);
            values.put("selection", currentSelection);
            values.put("result", resultOfGame);

            db.insert("picks", null, values);
        }
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
