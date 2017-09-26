package ru.ijava.tracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.StringBuilderPrinter;

import ru.ijava.tracker.model.Preferences;

/**
 * Created by rele on 7/10/17.
 */

public class TrackerService  extends Service {

    private TrackerTask trackerTask;
    private ServerTask serverTask;

    protected IBinder mBinder;

    public TrackerService() {
        mBinder = new LocalBinder();
    }

    public class LocalBinder extends Binder {
        public TrackerService getService(){
            return TrackerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Проверяем можно ли запустить Задачи и запускаем их если они не запущены
        Context context = getApplicationContext();
        Preferences preferences = Preferences.getInstance(context);
        if(serverTask == null) {
            serverTask = new ServerTask(context);
        }
        if(trackerTask == null) {
            trackerTask = new TrackerTask(context);
        }

        if(!preferences.isOnlyServer() && trackerTask.getStatus() == false) {
            trackerTask.runTask();
        }

        if(serverTask.getStatus() == false) {
            serverTask.runTask();
        }

        return START_STICKY;
    }
}
