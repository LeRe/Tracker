package ru.ijava.tracker.network;

import android.location.Location;

import java.io.Serializable;

import ru.ijava.tracker.model.Device;

/**
 * Created by rele on 7/25/17.
 */

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Action {PING, PONG, CLOSE_CONECTION, SAVE_LOCATION, LOG_I}

    public Action action;

    private String deviceId;
    private String deviceNickName;

    private long locationTime;
    private double locationLatitude;
    private double locationLongitude;
    private String locationProvider;

    public Message(Action action){
        this.action = action;
    }

    public Message(Action action, Device device, Location location){
        this.action = action;
        this.parseDevice(device);
        this.parseLocation(location);
    }

    private void parseDevice(Device device) {
        this.deviceId = device.getId();
        this.deviceNickName = device.getNickName();
    }

    private void parseLocation(Location location) {
        this.locationTime = location.getTime();
        this.locationLatitude = location.getLatitude();
        this.locationLongitude = location.getLongitude();
        this.locationProvider = location.getProvider();
    }

    public Device compileDevice() {
        Device device = null;
        if(deviceId != null && deviceNickName != null)
        {
            device = new Device(deviceId, deviceNickName);
        }
        return device;
    }

    public Location compileLocation() {
        Location location = null;
        if(locationTime != 0l && locationLongitude != 0d && locationLatitude != 0d && locationProvider != null)
        {
            location = new Location(locationProvider);
            location.setTime(locationTime);
            location.setLongitude(locationLongitude);
            location.setLatitude(locationLatitude);
        }
        return location;
    }
}
