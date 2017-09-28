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

        //TODO Об изменении настроек оповещение приходит примари девайсу, он дергает сервис(вызывает метод) сервис проверяет настройки и в соответствии с ними запускает или останавливает таски

        //TODO на основе нижеидущей порнографии родится приватный метод о котором речь идет ниже...
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

    //TODO тут будет метод который будет проверять настройки и в зависимости от их состояния запускать или останавливать задачи, вызываться будет при старте сервера и из нижестоящего публичного метода который будет дергать примари девайс при изменении настроек (при получении соответствующего оповещения)

    //TODO тут будет публичный метод который будет вызывать примари девайс при получении оповещения об изменении настроек, он будет вызывать выщестоящий приватный метод


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
