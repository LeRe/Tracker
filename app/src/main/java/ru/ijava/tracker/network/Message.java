package ru.ijava.tracker.network;

import android.location.Location;

import java.io.Serializable;

import ru.ijava.tracker.model.Device;

/**
 * Created by rele on 7/25/17.
 */

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    enum Action {CLOSE_CONECTION, SAVE_LOCATION, LOG_I}

    public static final int ACTION_CLOSE_CONECTION = 0;
    public static final int ACTION_SAVE_LOCATION = 1;
    public static final int ACTION_LOG_I = 100;

    public int action;
    public String content;

    public Message(Enum<Action> action, Device device, Location location){

    }

}
