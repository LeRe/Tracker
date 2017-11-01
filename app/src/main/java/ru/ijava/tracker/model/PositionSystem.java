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
import ru.ijava.tracker.network.Client;
import ru.ijava.tracker.network.MessageHandler;

/**
 * Created by rele on 7/7/17.
 */
public class PositionSystem {
    public static final String TRACKER_PROVIDER = "tracker";

    //Possible Deprecated
//    private Activity activity;
    private Context context;
    private PrimaryDevice primaryDevice;

    //Possible Deprecated
//    public PositionSystem(Activity activity, PrimaryDevice primaryDevice) {
//        this.activity = activity;
//        this.primaryDevice = primaryDevice;
//        this.determineCurrentLocation();
//    }

    public PositionSystem(Context context, PrimaryDevice primaryDevice) {
        this.context = context;
        this.primaryDevice = primaryDevice;
        this.determineCurrentLocation();
        if(context != null) {
            LogSystem logSystem = LogSystem.getInstance(context);
            logSystem.save("PositionSystem object created", LogSystem.DebugLevel.INFO, LogSystem.OutputDirection.All);
        }
    }

//Possible Deprecated
//    private void determineCurrentLocationViaFusedClient(){
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    MenuActivity.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//        else {
//            FusedLocationProviderClient fusedLocationClient;
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
//            fusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<android.location.Location>() {
//                @Override
//                public void onSuccess(android.location.Location location) {
//                    if (location != null && primaryDevice != null) {
//                        primaryDevice.setCurrentLocation(location);
//                        saveDeviceLocation(activity.getApplicationContext(), primaryDevice);
//                    }
//                }
//            });
//        }
//    }

    private void determineCurrentLocationViaLocationService()
    {
        LogSystem logSystem = LogSystem.getInstance(context);

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Consider calling
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
            primaryDevice.setCurrentLocation(lastKnownLocation);
            logSystem.save("Location is determine " + lastKnownLocation.getLatitude() + " " + lastKnownLocation.getLongitude(),
                    LogSystem.DebugLevel.INFO, LogSystem.OutputDirection.All);
            saveDeviceLocation(context, primaryDevice);
        }
    }

    private void saveDeviceLocation(Context context, Device device) {
//        DBHelper sqliteDB = new DBHelper(context);
//        sqliteDB.saveDeviceLocation(device);
//        sqliteDB.close();

        MessageHandler messageHandler = new MessageHandler(context);
        Client client = new Client(messageHandler);
        new Thread(client).start();
        client.saveLocation(device, device.getCurrentLocation());

//        Preferences preferences = Preferences.getInstance(context);
//        String remoteServerAddr = preferences.getRemoteServerAddress();
//
//        MessageHandler messageHandlerRemote = new MessageHandler(context);
//        Client clientRemote = new Client(remoteServerAddr, messageHandler);
//        new Thread(client).start();
//        client.saveLocation(device, device.getCurrentLocation());

    }

    public void determineCurrentLocation() {
        if(context != null) {
            determineCurrentLocationViaLocationService();
        }
        //Possible Deprecated
//        else if(activity != null) {
//            determineCurrentLocationViaFusedClient();
//        }
    }

}
