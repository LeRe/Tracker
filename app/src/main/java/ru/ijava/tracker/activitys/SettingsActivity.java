package ru.ijava.tracker.activitys;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import ru.ijava.tracker.fragments.SettingsFragment;

/**
 * Created by levchenko on 02.08.2017.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        getFragmentManager().beginTransaction()
                .add(new SettingsFragment(),"Settings")
                .commit();
    }
}
