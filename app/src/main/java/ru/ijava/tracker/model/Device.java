package ru.ijava.tracker.model;

import android.content.Context;

import com.google.android.gms.iid.InstanceID;

import java.io.Serializable;

/**
 * Created by levchenko on 22.06.2017.
 */

public class Device implements Serializable {
    private String id;
    private String nickName;
    private Location location;

    public Device(Context context) {
        String iid = InstanceID.getInstance(context).getId();
        setId(iid);

        //TODO read nickName from preferences
        setNickName("PreferenceRELE");
    }

    public Device(String id, String nickName) {
        setId(id);
        setNickName(nickName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
