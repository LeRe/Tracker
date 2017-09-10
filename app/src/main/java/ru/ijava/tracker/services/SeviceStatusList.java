package ru.ijava.tracker.services;

import java.util.ArrayList;

/**
 * Created by rele on 8/15/17.
 */

public interface SeviceStatusList {
    ArrayList<ServiceStatus> getServiceStatusList();
    void addService(ServiceStatus serviceStatus);
}
