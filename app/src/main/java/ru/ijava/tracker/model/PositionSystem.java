package ru.ijava.tracker.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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
    private Context context;
    private Device device;

    public PositionSystem(Activity activity, Device device) {
        this.activity = activity;
        this.device = device;
        this.determineCurrentLocation();
    }

    public PositionSystem(Context context, Device device) {
        this.context = context;
        this.device = device;
        this.determineCurrentLocation();
    }


    private void determineCurrentLocationViaFusedClient(){
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

    private void determineCurrentLocationViaLocationService()
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        if(lastKnownLocation != null) {
            ////////PositionSystem positionSystem = new PositionSystem(getApplicationContext(), device);
            device.setCurrentLocation(lastKnownLocation);

            saveDeviceLocation(context, device);
        }
    }

    private void saveDeviceLocation(Context context, Device device) {
        DBHelper sqliteDB = new DBHelper(context);
        sqliteDB.saveDeviceLocation(device);
        sqliteDB.close();
    }

    public void determineCurrentLocation() {
        if(context != null) {
            determineCurrentLocationViaLocationService();
        }
        else if(activity != null) {
            determineCurrentLocationViaFusedClient();
        }
    }

}
