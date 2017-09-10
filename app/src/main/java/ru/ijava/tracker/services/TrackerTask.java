package ru.ijava.tracker.services;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

import ru.ijava.tracker.model.PositionSystem;
import ru.ijava.tracker.model.Preferences;
import ru.ijava.tracker.model.PrimaryDevice;

/**
 * Created by rele on 8/30/17.
 */

public class TrackerTask extends AbstractTask {
    private Timer timer;
    private TimerTask timerTask;
    private static final long TIMER_DELAY = 1000;
    private static final long TIMER_PERIOD = 10 * 60 * 1000;
    private boolean trackerServiceEnable = false;
    PrimaryDevice primaryDevice;

    Context context;


    public TrackerTask(Context context) {

        this.context = context;

        //TODO ненужно, оповещаться об изменении настоек будет примару девайс, а он будет забинжен с сервисом и он будет давать команду на старт и стоп тасков
        //Preferences preferences = Preferences.getInstance(context);
        //preferences.addChangePreferenceListener(this);
//        if(!preferences.isOnlyServer()) {
//            timerTaskEnable();
//        }

        //TODO оно надо?... Да, судя по всему надо, т.к. используется в PositionSystem, хотя можно перенести этот код туда, в PositionSystem, тут экземпляр PrimaryDevice никто не пользует
        try {
            primaryDevice = PrimaryDevice.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            primaryDevice = PrimaryDevice.getInstance(context);
        }
        //primaryDevice.addService(this); TODO ненужно, будем биндиться к сервису и у него брать статусы тасков

        //TODO Старт таск (вероятно тут)... Хотя нет, старт будет осуществляться из TrackerService после проверок есть ли разрешение на запуск в настройках
    }

    //TODO ненужно всвязи со сменой архитектуры
//    @Override
//    public void updatePreference(Preferences preferences) {
//        if(preferences.isOnlyServer()) {
//            //Отключаем систему определения координат
//            if(timer != null) {
//                timerTaskDisable();
//            }
//        }
//        else {
//            //Включаем определение координат
//            timerTaskEnable();
//        }
//    }

    private void prepareTask() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                new PositionSystem(context, primaryDevice);
            }
        };
    }

    @Override
    public void runTask() {
        prepareTask();
        timer.schedule(timerTask, TIMER_DELAY, TIMER_PERIOD);
        taskEnable = true;
    }

    @Override
    public void stopTask() {
        timer.cancel();
        timer.purge();
        timer = null;
        taskEnable = false;
    }
}
