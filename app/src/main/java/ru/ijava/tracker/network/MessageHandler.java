package ru.ijava.tracker.network;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import ru.ijava.tracker.db.DBHelper;
import ru.ijava.tracker.model.Device;

/**
 * Created by levchenko on 26.07.2017.
 */

public class MessageHandler {
    private Context context;
    //private NetworkDevice networkDevice;

    public MessageHandler(Context context) {//, NetworkDevice networkDevice) {
        this.context = context;
        //this.networkDevice = networkDevice;
    }

    public void process(Message message) {
        switch (message.action) {
            case LOG_I:
                Log.i("RELE", "Recive message!!!");
                break;
            case CLOSE_CONECTION:
                //networkDevice.closeConection();
                break;
            case SAVE_LOCATION:
                this.saveLocation2DB(message);
                break;
        }
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
