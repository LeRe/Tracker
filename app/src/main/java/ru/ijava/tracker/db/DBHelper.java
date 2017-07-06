package ru.ijava.tracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.ijava.tracker.model.Device;
import ru.ijava.tracker.model.Location;

/**
 * Created by levchenko on 27.06.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    Context mContext;

    public DBHelper(Context context) {
        super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.Device.CREATE_TABLE);
        db.execSQL(DBContract.Location.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.Device.DELETE_TABLE);
        db.execSQL(DBContract.Location.DELETE_TABLE);
        onCreate(db);
    }

    public void saveDeviceLocation(Device device, Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Location.COLUMN_NAME_DEVICE_ID, device.getId());
        values.put(DBContract.Location.COLUMN_NAME_TIMESTAMP, device.getLocation().getTime());
        values.put(DBContract.Location.COLUMN_NAME_LATITUDE, location.getLatitude());
        values.put(DBContract.Location.COLUMN_NAME_LONGITUDE, location.getLongitude());

        long rowId = db.insert(DBContract.Location.TABLE_NAME, null, values);

        db.close();
    }

    public int countRecords(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.
                rawQuery("select count(*) from " + tableName, null);

        int result = -1;
        if(cursor != null && cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }

        db.close();
        return result;
    }

    public void dumpTables() {
        SQLiteDatabase db = this.getReadableDatabase();

        dumpTable2Log(db, DBContract.Device.TABLE_NAME);
        dumpTable2Log(db, DBContract.Location.TABLE_NAME);

        db.close();
    }

    private void dumpTable2Log(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select * from " + tableName, null);
        if (cursor != null && cursor.moveToFirst()) {
            int countRows = cursor.getCount();
            int countColumns = cursor.getColumnCount();

            cursor.moveToFirst();
            for( int i = 0; i < countRows; i++ ) {
                StringBuilder str = new StringBuilder();
                for (int j = 0; j < countColumns; j++)
                {
                    str.append(cursor.getString(j) + " ");
                }
                Log.i("RELE", str.toString());
                cursor.moveToNext();
            }
        }
        else {
            Log.i("RELE", "Table " + tableName + " empty");
        }
    }

    public void loadData() {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 0, 0); //Clear tables

        String[][] arrRecords = {
            {"1", "fbhnWIJJCyY", "1499137826465", "55.4219", "37.7711"},
            {"2", "fbhnWIJJCyY", "1499137826465", "55.4219", "37.7711"},
            {"3", "fbhnWIJJCyY", "1499137826465", "55.4219", "37.7711"},
            {"4", "fbhnWIJJCyY", "1499138180917", "55.4219", "37.771"},
            {"5", "fbhnWIJJCyY", "1499140074791", "55.4259", "37.7673"},
            {"6", "fbhnWIJJCyY", "1499140167422", "55.4219", "37.771"},
            {"7", "fbhnWIJJCyY", "1499140377727", "55.4219", "37.771"},
            {"8", "fbhnWIJJCyY", "1499143442071", "55.4397", "37.7496"},
            {"9", "fbhnWIJJCyY", "1499145574256", "55.428", "37.7708"},
            {"10", "fbhnWIJJCyY", "1499145877576", "55.4048", "37.784"},
            {"11", "fbhnWIJJCyY", "1499146447615", "55.3338", "37.7298"},
            {"12", "fbhnWIJJCyY", "1499152267238", "55.171", "37.3443"},
            {"13", "fbhnWIJJCyY", "1499153647243", "55.2162", "36.984"},
            {"14", "fbhnWIJJCyY", "1499154189734", "55.2095", "36.9609"},
            {"15", "fbhnWIJJCyY", "1499154189734", "55.2095", "36.9609"},
            {"16", "fbhnWIJJCyY", "1499167567159", "55.2095", "36.9609"}
        };

        for (int i = 0; i < arrRecords.length; i++) {
            ContentValues values = new ContentValues();

            values.put(DBContract.Location._ID, arrRecords[i][0]);
            values.put(DBContract.Location.COLUMN_NAME_DEVICE_ID, arrRecords[i][1]);
            values.put(DBContract.Location.COLUMN_NAME_TIMESTAMP, arrRecords[i][2]);
            values.put(DBContract.Location.COLUMN_NAME_LATITUDE, arrRecords[i][3]);
            values.put(DBContract.Location.COLUMN_NAME_LONGITUDE, arrRecords[i][4]);

            db.insert(DBContract.Location.TABLE_NAME, null, values);

        }
/*
1 fbhnWIJJCyY 1499137826465 55.4219 37.7711
2 fbhnWIJJCyY 1499137826465 55.4219 37.7711
3 fbhnWIJJCyY 1499137826465 55.4219 37.7711
4 fbhnWIJJCyY 1499138180917 55.4219 37.771
5 fbhnWIJJCyY 1499140074791 55.4259 37.7673
6 fbhnWIJJCyY 1499140167422 55.4219 37.771
7 fbhnWIJJCyY 1499140377727 55.4219 37.771
8 fbhnWIJJCyY 1499143442071 55.4397 37.7496
9 fbhnWIJJCyY 1499145574256 55.428 37.7708
10 fbhnWIJJCyY 1499145877576 55.4048 37.784
11 fbhnWIJJCyY 1499146447615 55.3338 37.7298
12 fbhnWIJJCyY 1499152267238 55.171 37.3443
13 fbhnWIJJCyY 1499153647243 55.2162 36.984
14 fbhnWIJJCyY 1499154189734 55.2095 36.9609
15 fbhnWIJJCyY 1499154189734 55.2095 36.9609
16 fbhnWIJJCyY 1499167567159 55.2095 36.9609
*/
    }

    public String getStatistics() {
        StringBuilder result = new StringBuilder();

        result.append("\n\n");
        result.append("Колличество записей в таблице " + DBContract.Device.TABLE_NAME + ": "
                + countRecords(DBContract.Device.TABLE_NAME));
        result.append("\n\n");
        result.append("Колличество записей в таблице " + DBContract.Location.TABLE_NAME + ": "
                + countRecords(DBContract.Location.TABLE_NAME));

        return result.toString();
    }
}
