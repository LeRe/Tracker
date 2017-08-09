package ru.ijava.tracker.model;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

/**
 * Created by levchenko on 09.08.2017.
 */

public class PrimaryDevice extends Device implements Preferences.ChangePreferenceListener {
    private static PrimaryDevice primaryDevice;

    private PrimaryDevice(Context context) {
        super("", "");
        String iid = InstanceID.getInstance(context).getId();
        setId(iid);
        Preferences preferences = Preferences.getInstance(context);
        setNickName(preferences.getNickname());

        primaryDevice = this;
    }

    private PrimaryDevice(String id, String nickName) {
        super(id, nickName);
        primaryDevice = this;
    }

    public PrimaryDevice(Parcel in) {
        super(in);
        primaryDevice = this;
    }

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
}
