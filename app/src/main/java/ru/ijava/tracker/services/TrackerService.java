package ru.ijava.tracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import ru.ijava.tracker.model.LogSystem;
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
        LogSystem logSystem = LogSystem.getInstance(getApplicationContext());
        logSystem.save("TrackerService.onBind(...) runned now, something bind to TrackerService", LogSystem.DebugLevel.DEBUG, LogSystem.OutputDirection.All);
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogSystem logSystem = LogSystem.getInstance(getApplicationContext());
        logSystem.save("TrackerService.onStartCommand(...) runned now, TrackerService cold start", LogSystem.DebugLevel.DEBUG, LogSystem.OutputDirection.All);
        //Проверяем можно ли запустить Задачи и запускаем их если они не запущены

        checkPreferenceStartStopTask();

        return START_STICKY;
    }

    //проверяет настройки и в зависимости от их состояния запускает или останавливает задачи,
    private void checkPreferenceStartStopTask() {
        Context context = getApplicationContext();
        LogSystem logSystem = LogSystem.getInstance(context);

        Preferences preferences = Preferences.getInstance(context);
        if(serverTask == null) {
            serverTask = new ServerTask(context);
        }
        if(trackerTask == null) {
            trackerTask = new TrackerTask(context);
        }

        if(!preferences.isOnlyServer() && !trackerTask.isRunning()) {
            trackerTask.runTask();
            logSystem.save("trackerTask runned now", LogSystem.DebugLevel.DEBUG, LogSystem.OutputDirection.All);
        }
        else {
            trackerTask.stopTask();
            logSystem.save("trackerTask stopped", LogSystem.DebugLevel.DEBUG, LogSystem.OutputDirection.All);
        }

        // univocal start
        if(!serverTask.isRunning()) {
            serverTask.runTask();
            logSystem.save("serverTask runned now", LogSystem.DebugLevel.DEBUG, LogSystem.OutputDirection.All);
        }
        else {
            //serverTask.stopTask();
            logSystem.save("serverTask is running now, impossible to stop", LogSystem.DebugLevel.DEBUG, LogSystem.OutputDirection.All);
        }
    }

    //Этот метод необходимо запустить если изменились настройки (Это будет делать PrimaryDevice)
    public void changeTask() {
        checkPreferenceStartStopTask();
    }

    public List<AbstractTask> getAbstractTasks() {
        List<AbstractTask> abstractTaskList = new ArrayList<AbstractTask>();

        if(trackerTask != null) {
            abstractTaskList.add(trackerTask);
        }
        if(serverTask != null) {
            abstractTaskList.add(serverTask);
        }

        return abstractTaskList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogSystem logSystem = LogSystem.getInstance(getApplicationContext());
        logSystem.save("TrackerService.onDestroy(...) runned now, TrackerService was destroyed", LogSystem.DebugLevel.DEBUG, LogSystem.OutputDirection.All);
    }
}
