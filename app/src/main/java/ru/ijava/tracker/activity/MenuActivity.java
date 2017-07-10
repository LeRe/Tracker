package ru.ijava.tracker.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import ru.ijava.tracker.R;
import ru.ijava.tracker.model.Device;
import ru.ijava.tracker.model.PositionSystem;
import ru.ijava.tracker.model.TrackerService;

public class MenuActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private TextView mTextMessage;
    private Device device;
    PositionSystem positionSystem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    home();
                    return true;
                case R.id.navigation_dashboard:
                    dashboard();
                    startMapActivity();
                    return true;
                case R.id.navigation_notifications:
                    notifications();
                    return true;
                case R.id.navigation_db_statistics:
                    startDBStatisticsActivity();
                    return true;
            }
            return false;
        }

    };

    private void notifications() {
        mTextMessage.setText(R.string.title_notifications);
    }

    private void dashboard() {
        mTextMessage.setText(R.string.title_dashboard);
    }

    private void home() {
        mTextMessage.setText(R.string.title_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        device = new Device(this);
        positionSystem = new PositionSystem(this, device);


        //start service
        Intent intent = new Intent(this, TrackerService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                home();
                return true;
            case R.id.navigation_dashboard:
                dashboard();
                startMapActivity();
                return true;
            case R.id.navigation_notifications:
                notifications();
                return true;
            case R.id.navigation_db_statistics:
                startDBStatisticsActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    positionSystem.determineCurrentLocation();
                }
                break;
        }
    }

    private void startDBStatisticsActivity() {
        Intent intent = new Intent(this, DBActivity.class);
        startActivity(intent);
    }

    private void startMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.DEVICE_KEY, device);
        startActivity(intent);
    }
}