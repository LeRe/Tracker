package ru.ijava.tracker.model;

import java.io.Serializable;

/**
 * Created by levchenko on 22.06.2017.
 */

public class Device implements Serializable {
    private String nickName;
    private Location location;

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
