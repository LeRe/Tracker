package ru.ijava.tracker.model;

import android.provider.BaseColumns;

/**
 * Created by rele on 6/27/17.
 */

public class DBContract {
    public static final String DB_NAME = "tracker.db";
    public static final int DB_VERSION = 1;

    private DBContract(){}

    public static class Device implements BaseColumns {
        public static final String TABLE_NAME = "device";

        public static final String COLUMN_TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";

        public static final String COLUMN_NAME_NICKNAME = "nickname";
        public static final String COLUMN_TYPE_NICKNAME = "TEXT";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" +
                _ID + " " + COLUMN_TYPE_ID + ", " +
                COLUMN_NAME_NICKNAME + " " + COLUMN_TYPE_NICKNAME +
                ");";
    }

    public static class Location implements BaseColumns {
        public static final String TABLE_NAME = "location";

        public static final String COLUMN_TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";

        public static final String COLUMN_NAME_DEVICE_ID = "device_id";
        public static final String COLUMN_TYPE_DEVICE_ID = "INTEGER";

        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_TYPE_TIMESTAMP = "INTEGER";

        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_TYPE_LATITUDE = "REAL";

        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_TYPE_LONGITUDE = "REAL";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" +
                _ID + " " + COLUMN_TYPE_ID + ", " +
                COLUMN_NAME_DEVICE_ID + " " + COLUMN_TYPE_DEVICE_ID + ", " +
                COLUMN_NAME_TIMESTAMP + " " + COLUMN_TYPE_TIMESTAMP + ", " +
                COLUMN_NAME_LATITUDE + " " + COLUMN_TYPE_LATITUDE + ", " +
                COLUMN_NAME_LONGITUDE + " " + COLUMN_TYPE_LONGITUDE +
                ");";
    }

}
