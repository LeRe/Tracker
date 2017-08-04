package ru.ijava.tracker.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by levchenko on 04.08.2017.
 */

public class Preferences {
    private static Preferences preferences;

    private String nickname;
    private String remoteServerAddress;
    private boolean onlyServer;

    private Preferences(Context context) {
        if(context != null) {
            readPreferences(context);
        }
    }

    private void readPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        nickname = sharedPreferences.getString("edit_text_preference_nickname", "NO_PREFERENCE");
        remoteServerAddress = sharedPreferences.getString("edit_text_preference_server", "NO_PREFERENCE");
        onlyServer = sharedPreferences.getBoolean("check_box_preference_only_server",false);
    }

    public static Preferences getInstance(Context context) {
        if(preferences == null) {
            preferences = new Preferences(context);
        }
        return preferences;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRemoteServerAddress() {
        return remoteServerAddress;
    }

    public boolean isOnlyServer() {
        return onlyServer;
    }
}
