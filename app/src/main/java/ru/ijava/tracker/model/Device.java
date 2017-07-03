package ru.ijava.tracker.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.iid.InstanceID;

import java.io.Serializable;

import ru.ijava.tracker.activity.MenuActivity;
import ru.ijava.tracker.db.DBHelper;

/**
 * Created by levchenko on 22.06.2017.
 */

public class Device implements Serializable {
    private String id;
    private String nickName;
    private Location location;

    public Device(Context context) {
        String iid = InstanceID.getInstance(context).getId();
        setId(iid);

        //TODO read nickName from preferences

    }

    public Device(String id, String nickName) {
        setId(id);
        setNickName(nickName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void spotLocation(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MenuActivity.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            final Device device = this;

            FusedLocationProviderClient fusedLocationClient;
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null) {
                        ru.ijava.tracker.model.Location deviceLocation = new ru.ijava.tracker.model.Location();
                        deviceLocation.setLatitude(location.getLatitude());
                        deviceLocation.setLongitude(location.getLongitude());
                        deviceLocation.setTime(location.getTime());
                        device.setLocation(deviceLocation);
                    }
                }
            });
        }
    }
}
