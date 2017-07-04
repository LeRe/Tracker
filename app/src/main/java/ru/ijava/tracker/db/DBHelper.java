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

        Log.i("RELE", "-------------");
        Log.i("RELE","Insert row id = " + rowId);
        Log.i("RELE","device id: " + device.getId());
        Log.i("RELE", "timestamp: " + Long.toString(device.getLocation().getTime()));
        Log.i("RELE", "latitude: " + location.getLatitude());
        Log.i("RELE", "longitude: " + location.getLongitude());
        Log.i("RELE", "-------------");

        db.close();
    }

    public int countLocationRecords() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.
                rawQuery("select count(*) from " + DBContract.Location.TABLE_NAME, null);

        int result = -1;
        if(cursor != null && cursor.moveToFirst()) {
            Log.i("RELE","select count(*)");
            Log.i("RELE", "Column count: " + cursor.getColumnCount());
            Log.i("RELE", "Rows in cursor: " + cursor.getCount());

            result = cursor.getInt(0);
        }


      cursor = db.
                rawQuery("select * from " + DBContract.Location.TABLE_NAME, null);

        if(cursor != null && cursor.moveToFirst()) {
            Log.i("RELE","select *");
            Log.i("RELE", "Column count: " + cursor.getColumnCount());
            Log.i("RELE", "Rows in cursor: " + cursor.getCount());
        }
        else {
            Log.i("RELE", "cursor is null or move to first record false");
        }

        db.close();
        return result;
    }
}
