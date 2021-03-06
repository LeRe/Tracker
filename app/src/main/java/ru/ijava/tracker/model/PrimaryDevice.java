package ru.ijava.tracker.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;
import java.util.List;
import ru.ijava.tracker.services.AbstractTask;
import ru.ijava.tracker.services.TrackerService;

/**
 * Created by levchenko on 09.08.2017.
 */
public class PrimaryDevice extends Device implements Preferences.ChangePreferenceListener {
    private static PrimaryDevice primaryDevice;

    private TrackerService mTrackerService;

    private PrimaryDevice(Context context) {
        super("", "");
        String iid = InstanceID.getInstance(context).getId();
        setId(iid);
        Preferences preferences = Preferences.getInstance(context);
        setNickName(preferences.getNickname());

        primaryDevice = this;

        Intent intentTracker = new Intent(context, TrackerService.class);
        context.startService(intentTracker);
        //context.bindService(intentTracker, mConnection, Context.BIND_ABOVE_CLIENT);
        context.bindService(intentTracker, mConnection, 0);

//        if(mConnection!=null) {
//            boolean bindSuccessfully = context.bindService(intentTracker, mConnection, Context.BIND_ABOVE_CLIENT);
//            if(!bindSuccessfully) {
//                context.startService(intentTracker);
//            }
//        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mTrackerService = ((TrackerService.LocalBinder) service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            mTrackerService = null;
        }
    };

    public static PrimaryDevice getInstance() throws Exception {
        if(primaryDevice == null) {
            throw new Exception("Primary device not exist now");
        }
        else return primaryDevice;
    }

    public static PrimaryDevice getInstance(Context context) {
        if(primaryDevice == null) {
            primaryDevice = new PrimaryDevice(context);
        }
        return primaryDevice;
    }

    @Override
    public void updatePreference(Preferences preferences) {
        nickName = preferences.getNickname();
        // Сообщаем сервису об изменении настроек, пусть проверит задачи, может что-то надо запустить или остановить
        if(mTrackerService != null){
            mTrackerService.changeTask();
        }
    }

    public List<AbstractTask> getAbstractTaskList() {
        List<AbstractTask> abstractTaskList = null;
        if(mTrackerService != null) {
            abstractTaskList = mTrackerService.getAbstractTasks();
        }
        return abstractTaskList;
    }
}
