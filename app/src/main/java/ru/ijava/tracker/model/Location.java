package ru.ijava.tracker.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by levchenko on 22.06.2017.
 */

public class Location implements Serializable {
    private double latitude;
    private double longitude;
    private long time;

    public Location(double latitude, double longitude, long time) {
        setLatitude(latitude);
        setLongitude(longitude);
        setTime(time);
    }

    public static Comparator<Location> comparator = new Comparator<Location>() {
        @Override
        public int compare(Location o1, Location o2) {
            long time1 = o1.getTime();
            long time2 = o2.getTime();

            if(time1 < time2) {
                return -1;
            }
            if (time1 > time2) {
                return 1;
            }

            return 0;
        }
    };

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

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
