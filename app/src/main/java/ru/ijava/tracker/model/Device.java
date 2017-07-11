package ru.ijava.tracker.model;

import android.content.Context;

import com.google.android.gms.iid.InstanceID;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by levchenko on 22.06.2017.
 */

public class Device implements Serializable {
    private String id;
    private String nickName;
    private Location currentLocation;
    private ArrayList<Location> locationsHistory;

    public void putLocation2History(Location location) {
        if(this.locationsHistory == null) {
            initializeLocationsHistory();
        }
        this.locationsHistory.add(location);
    }

    public void initializeLocationsHistory() {
        this.locationsHistory = new ArrayList<Location>();
    }

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

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
