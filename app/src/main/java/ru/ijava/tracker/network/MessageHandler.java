package ru.ijava.tracker.network;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import ru.ijava.tracker.db.DBHelper;
import ru.ijava.tracker.model.Device;
import ru.ijava.tracker.model.LogSystem;

/**
 * Created by levchenko on 26.07.2017.
 */

public class MessageHandler {
    private Context context;
    private NetworkDevice networkDevice;

    public MessageHandler(Context context) {
        this.context = context;
        this.networkDevice = networkDevice;
    }

    public void setNetworkDevice(NetworkDevice networkDevice) {
        this.networkDevice = networkDevice;
    }

    public Message process(Message message) {
        Message outMessage = null;
        LogSystem logSystem = LogSystem.getInstance(context);

        switch (message.action) {
            case PING:
                logSystem.save("PING received");
                outMessage = new Message(Message.Action.PONG);
                break;
            case PONG:
                logSystem.save("PONG received");
                outMessage = new Message(Message.Action.CLOSE_CONECTION);
                break;
            case LOG_I:
                Log.i("RELE", "Recive message!!!");
                break;
            case CLOSE_CONECTION:
                if(networkDevice!=null) {
                    networkDevice.closeConection();
                }
                break;
            case SAVE_LOCATION:
                this.saveLocation2DB(message);
                break;
        }

        return outMessage;
    }

    private void saveLocation2DB(Message message){
        DBHelper sqliteDB = new DBHelper(context);
        Device device = message.compileDevice();
        Location location = message.compileLocation();
        if(device != null && location != null) {
            device.setCurrentLocation(location);
            sqliteDB.saveDeviceLocation(device);
        }
    }
}
