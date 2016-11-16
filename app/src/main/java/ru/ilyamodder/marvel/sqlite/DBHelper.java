package ru.ilyamodder.marvel.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ilya on 02.11.16.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, "cache.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RequestsTable.NAME + " (" + RequestsTable.Columns._ID +
            " INTEGER PRIMARY KEY, " + RequestsTable.Columns.RESP_CODE + " INTEGER, " +
            RequestsTable.Columns.URL + " TEXT)");
        db.execSQL("CREATE TABLE " + ComicsTable.NAME + " (" + ComicsTable.Columns._ID +
                " INTEGER PRIMARY KEY, " + ComicsTable.Columns.TITLE + " VARCHAR, " +
                ComicsTable.Columns.DATE_MODIFIED + " DATE, " + ComicsTable.Columns.FORMAT +
                " VARCHAR, " + ComicsTable.Columns.DESCRIPTION + " VARCHAR, " +
                ComicsTable.Columns.URL + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
