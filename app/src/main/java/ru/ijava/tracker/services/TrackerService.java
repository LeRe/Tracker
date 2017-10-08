package ru.ijava.tracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
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

        checkPreferenceStartStopTask();

        return START_STICKY;
    }

    //проверяет настройки и в зависимости от их состояния запускает или останавливает задачи,
    private void checkPreferenceStartStopTask() {
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
        else {
            trackerTask.stopTask();
        }

        if(serverTask.getStatus() == false) {
            serverTask.runTask();
        }
        else {
            serverTask.stopTask();
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
}
