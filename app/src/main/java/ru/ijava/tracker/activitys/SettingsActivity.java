package ru.ijava.tracker.activitys;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import ru.ijava.tracker.R;
/**
 * Created by levchenko on 02.08.2017.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
