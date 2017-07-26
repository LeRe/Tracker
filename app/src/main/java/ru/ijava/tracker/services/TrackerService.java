package ru.ijava.tracker.services;

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
import ru.ijava.tracker.model.Device;
import ru.ijava.tracker.model.PositionSystem;

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

                new PositionSystem(context, device);
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
