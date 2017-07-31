package ru.ijava.tracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ru.ijava.tracker.network.MessageHandler;
import ru.ijava.tracker.network.Server;

/**
 * Created by levchenko on 20.07.2017.
 */

public class ServerService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MessageHandler messageHandler = new MessageHandler(getApplicationContext());
        new Thread(new Server(messageHandler)).start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
