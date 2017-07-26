package ru.ijava.tracker.network;

import java.io.Serializable;

/**
 * Created by rele on 7/25/17.
 */

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int ACTION_CLOSE_CONECTION = 0;
    public static final int ACTION_LOG_I = 100;

    public int action;
    public String content;

}
