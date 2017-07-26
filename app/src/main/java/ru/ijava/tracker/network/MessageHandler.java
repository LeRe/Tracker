package ru.ijava.tracker.network;

import android.util.Log;

/**
 * Created by levchenko on 26.07.2017.
 */

public class MessageHandler {
    private NetworkObject networkObject;

    public MessageHandler(NetworkObject networkObject) {
        this.networkObject = networkObject;
    }

    public void process(Message message) {
        switch (message.action) {
            case Message.ACTION_LOG_I:
                Log.i("RELE", message.content);
                break;
            case Message.ACTION_CLOSE_CONECTION:
                networkObject.closeConection();
                break;
        }
    }

}
