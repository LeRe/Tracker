package ru.ijava.tracker.model;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

import ru.ijava.tracker.db.DBHelper;

/**
 * Created by rele on 7/10/17.
 */

public class TrackerService extends Service {
    private Timer timer;
    private TimerTask timerTask;
    private static final long TIMER_DELAY = 1000;
    private static final long TIMER_PERIOD = 10 * 60 * 1000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                Device device = new Device(context);

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

                //TODO Везде переходим на android.location.Location, ru.ijava.model.location теперь депрекатед!!!
                //TODO Начинаем сохранять в базе источник места положения String getProvider () Returns the name of the provider that generated this fix.

                ////////PositionSystem positionSystem = new PositionSystem(getApplicationContext(), device);
                device.setCurrentLocation(new Location(
                        lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude(),
                        lastKnownLocation.getTime()));

                DBHelper sqliteDB = new DBHelper(getApplicationContext());
                sqliteDB.saveDeviceLocation(device);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, TIMER_DELAY, TIMER_PERIOD);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
