package ru.ijava.tracker.services;

import android.content.Context;
import java.util.Timer;
import java.util.TimerTask;
import ru.ijava.tracker.model.PositionSystem;
import ru.ijava.tracker.model.PrimaryDevice;

/**
 * Created by rele on 8/30/17.
 */

public class TrackerTask extends AbstractTask {
    private Timer timer;
    private TimerTask timerTask;
    private static final long TIMER_DELAY = 1000;
    private static final long TIMER_PERIOD = 10 * 60 * 1000;
    PrimaryDevice primaryDevice;

    Context context;

    public TrackerTask(Context context) {
        taskName = "Tracker task";
        this.context = context;

        //TODO оно надо?... Да, судя по всему надо, т.к. используется в PositionSystem, хотя можно перенести этот код туда, в PositionSystem, тут экземпляр PrimaryDevice никто не пользует
        try {
            primaryDevice = PrimaryDevice.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            primaryDevice = PrimaryDevice.getInstance(context);
        }
    }

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
    protected void checkStatus() {
        //nothing to do...
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
