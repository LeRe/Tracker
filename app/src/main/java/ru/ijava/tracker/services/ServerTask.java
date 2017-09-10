package ru.ijava.tracker.services;

import android.content.Context;

import ru.ijava.tracker.network.MessageHandler;
import ru.ijava.tracker.network.Server;

/**
 * Created by rele on 8/30/17.
 */

public class ServerTask extends AbstractTask {
    private Thread thread;
    private Server server;

    public ServerTask(Context context) {
        taskName = "Server task";

        MessageHandler messageHandler = new MessageHandler(context);
        server = new Server(messageHandler);
    }

    @Override
    public boolean getStatus() {
        taskEnable  = (thread.getState() != Thread.State.TERMINATED);
        return taskEnable;
    }

    @Override
    public void runTask() {
        thread = new Thread(server);
        thread.start();
    }

    @Override
    public void stopTask() {
        thread.interrupt();
    }
}
