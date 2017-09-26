package ru.ijava.tracker.services;

/**
 * Created by rele on 8/21/17.
 */

public abstract class AbstractTask {
    protected String taskName = "Abstract task";
    protected boolean taskEnable = false;

    public String getServiceName() {
        return taskName;
    }
    protected abstract void checkStatus();
    public boolean getStatus() {
        return taskEnable;
    }
    public abstract void runTask();
    public abstract void stopTask();

}
