package ru.ijava.tracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import ru.ijava.tracker.model.PositionSystem;
import ru.ijava.tracker.model.Preferences;
import ru.ijava.tracker.model.PrimaryDevice;

/**
 * Created by rele on 7/10/17.
 */

public class TrackerService extends Service implements Preferences.ChangePreferenceListener {
    private Timer timer;
    private TimerTask timerTask;
    private static final long TIMER_DELAY = 1000;
    private static final long TIMER_PERIOD = 10 * 60 * 1000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Preferences preferences = Preferences.getInstance(getApplicationContext());
        preferences.addChangePreferenceListener(this);

        if(!preferences.isOnlyServer()) {
            prepareTask();
            timer.schedule(timerTask, TIMER_DELAY, TIMER_PERIOD);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void updatePreference(Preferences preferences) {
        if(preferences.isOnlyServer()) {
            //Отключаем систему определения координат
            if(timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            Log.i("RELE", "Timer canseled...");
        }
        else {
            //Включаем определение координат
            prepareTask();
            timer.schedule(timerTask, TIMER_DELAY, TIMER_PERIOD);
            Log.i("RELE", "Timer scheduled!!!");
        }
    }

    private void prepareTask() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Context context = getApplicationContext();

                PrimaryDevice primaryDevice;
                try {
                    primaryDevice = PrimaryDevice.getInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    primaryDevice = PrimaryDevice.getInstance(context);
                }
                new PositionSystem(context, primaryDevice);
            }
        };
    }
}
