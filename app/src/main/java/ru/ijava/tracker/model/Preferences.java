package ru.ijava.tracker.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by levchenko on 04.08.2017.
 */
public class Preferences implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String KEY_PREF_NICKNAME = "edit_text_preference_nickname";
    private static final String KEY_PREF_REMOTE_SERVER_ADDRESS = "edit_text_preference_server";
    private static final String KEY_PREF_ONLY_SERVER_FUNCTIONS = "check_box_preference_only_server";

    private static Preferences preferences;

    private String nickname;
    private String remoteServerAddress;
    private boolean onlyServer;

    private Preferences(Context context) {
        if(context != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            readPreferences(sharedPreferences);
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }
    }

    private void readPreferences(SharedPreferences sharedPreferences) {
        nickname = sharedPreferences.getString(KEY_PREF_NICKNAME, "NO_PREFERENCE");
        remoteServerAddress = sharedPreferences.getString(KEY_PREF_REMOTE_SERVER_ADDRESS, "NO_PREFERENCE");
        onlyServer = sharedPreferences.getBoolean(KEY_PREF_ONLY_SERVER_FUNCTIONS,false);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case KEY_PREF_NICKNAME:
                nickname = sharedPreferences.getString(KEY_PREF_NICKNAME, "NO_PREFERENCE");
                break;
            case KEY_PREF_REMOTE_SERVER_ADDRESS:
                remoteServerAddress = sharedPreferences.getString(KEY_PREF_REMOTE_SERVER_ADDRESS, "NO_PREFERENCE");
                break;
            case KEY_PREF_ONLY_SERVER_FUNCTIONS:
                onlyServer = sharedPreferences.getBoolean(KEY_PREF_ONLY_SERVER_FUNCTIONS,false);
                break;
        }
    }
}
