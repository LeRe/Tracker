package ru.ijava.tracker.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import ru.ijava.tracker.activitys.MenuActivity;
import ru.ijava.tracker.db.DBHelper;
import android.location.Location;

/**
 * Created by rele on 7/7/17.
 */

public class PositionSystem {
    public static final String TRACKER_PROVIDER = "tracker";

    private Activity activity;
    Device device;

    public PositionSystem(Activity activity, Device device) {
        this.activity = activity;
        this.device = device;

        this.determineCurrentLocation();
    }

    public void determineCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MenuActivity.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            FusedLocationProviderClient fusedLocationClient;
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null && device != null) {
                        device.setCurrentLocation(location);
                        saveDeviceLocation(activity.getApplicationContext(), device);
                    }
                }
            });
        }
    }

    private void saveDeviceLocation(Context context, Device device) {
        DBHelper sqliteDB = new DBHelper(context);
        sqliteDB.saveDeviceLocation(device);
        sqliteDB.close();
    }
}
