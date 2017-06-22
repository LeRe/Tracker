package ru.ijava.tracker.model;

import java.io.Serializable;

/**
 * Created by levchenko on 22.06.2017.
 */

public class Location implements Serializable {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
