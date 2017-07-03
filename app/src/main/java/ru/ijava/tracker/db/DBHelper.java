package ru.ijava.tracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        db.insert(DBContract.Location.TABLE_NAME, null, values);
    }
}
