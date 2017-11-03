package ru.ijava.tracker.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import ru.ijava.tracker.R;
import ru.ijava.tracker.model.Preferences;
import ru.ijava.tracker.network.Client;
import ru.ijava.tracker.network.MessageHandler;

/**
 * Created by rele on 8/16/17.
 */

public class ToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        Preferences preferences = Preferences.getInstance(this);
        String remoteServerAddr = preferences.getRemoteServerAddress();

        MessageHandler messageHandlerRemote = new MessageHandler(this);
        Client clientRemote = new Client(remoteServerAddr, messageHandlerRemote);
        new Thread(clientRemote).start();
        clientRemote.sendPing();

    }
}
