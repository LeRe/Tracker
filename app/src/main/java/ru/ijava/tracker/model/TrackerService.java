package ru.ijava.tracker.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

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
                Device device = new Device(getApplicationContext());

                //PositionSystem positionSystem = new PositionSystem(getApplicationContext(), device);
                device.setLocation(new Location(55.4219, 37.7711, 1499137826465l));

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
