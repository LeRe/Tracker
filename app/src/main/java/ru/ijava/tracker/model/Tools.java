package ru.ijava.tracker.model;

import android.location.Location;
import java.util.Comparator;


/**
 * Created by rele on 7/16/17.
 */

public class Tools {


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
}
