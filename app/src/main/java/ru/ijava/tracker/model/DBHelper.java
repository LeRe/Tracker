package ru.ijava.tracker.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by levchenko on 27.06.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "CREATE TABLE myTable(...)";
    private static final String DB_NAME = "mySuperDB.db";
    private static final int DB_VERSION = 1;
    Context mContext;

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
