package ru.ijava.tracker.model;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import ru.ijava.tracker.activity.MenuActivity;

/**
 * Created by rele on 7/7/17.
 */

public class PositionSystem {
    Activity activity;
    Location currentLocation;
    //TODO Сюда же должен быть передан Device позицию которого мы определяем,
    // свойство currentLocation будет ненужно, определенную широту и долготу будем сохранять в устройство
    // за которым следим
    // свойство currentLocation должно переехать в Device и если оно не нулл значит текущее место положение
    // устройство определено


    public PositionSystem(Activity activity) {
        this.activity = activity;

        determineCurrentLocation();

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
                    if (location != null) {
                        currentLocation = new Location();
                        currentLocation.setLatitude(location.getLatitude());
                        currentLocation.setLongitude(location.getLongitude());
                        currentLocation.setTime(location.getTime());
                    }
                }
            });

        }
    }

}
