package ru.ijava.tracker.network;

/**
 * Created by levchenko on 26.07.2017.
 */

public interface NetworkDevice {
    void closeConection();
    void setNetworkDevice(NetworkDevice networkDevice);
}
