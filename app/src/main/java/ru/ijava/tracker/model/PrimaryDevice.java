package ru.ijava.tracker.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import java.util.ArrayList;
import java.util.List;

import ru.ijava.tracker.services.AbstractTask;
import ru.ijava.tracker.services.ServiceStatus;
import ru.ijava.tracker.services.SeviceStatusList;
import ru.ijava.tracker.services.TrackerService;

/**
 * Created by levchenko on 09.08.2017.
 */

public class PrimaryDevice extends Device implements Preferences.ChangePreferenceListener {
    private static PrimaryDevice primaryDevice;
    //private static ArrayList<ServiceStatus> serviceStatusList = new ArrayList<ServiceStatus>();

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

        context.bindService(intentTracker, mConnection, Context.BIND_ABOVE_CLIENT);
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
        Log.i("RELE", "NickName changed on " + nickName);
    }

    // from interface  SeviceStatusList
//    @Override
//    public ArrayList<ServiceStatus> getServiceStatusList() {
//        return serviceStatusList;
//    }
//
//    @Override
//    public void addService(ServiceStatus serviceStatus) {
//        if(serviceStatus != null) {
//            serviceStatusList.add(serviceStatus);
//        }
//    }

    public List<AbstractTask> getAbstractTaskList() {
        List<AbstractTask> abstractTaskList = null;
        if(mTrackerService != null) {
            abstractTaskList = mTrackerService.getAbstractTasks();
        }
        return abstractTaskList;
    }
}
