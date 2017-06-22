package ru.ijava.tracker.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import ru.ijava.tracker.R;
import ru.ijava.tracker.model.Device;

public class MenuActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Device mDevice;

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
                    return true;
                case R.id.navigation_notifications:
                    Notifications();
                    return true;
            }
            return false;
        }

    };

    private void Notifications() {
        mTextMessage.setText(R.string.title_notifications);
    }

    private void dashboard() {
        mTextMessage.setText(R.string.title_dashboard);

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.DEVICE_KEY, mDevice);
        startActivity(intent);
    }

    private void home() {
        mTextMessage.setText(R.string.title_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mDevice = new Device();
        mDevice.setNickName("ReLe");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling   Необходимо запросить разрешение
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else {
            final Context mContext = this;
            FusedLocationProviderClient fusedLocationClient;
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        ru.ijava.tracker.model.Location deviceLocation = new ru.ijava.tracker.model.Location();
                        deviceLocation.setLatitude(location.getLatitude());
                        deviceLocation.setLongitude(location.getLongitude());
                        mDevice.setLocation(deviceLocation);

                        Toast.makeText(mContext, "Location is determined successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


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
                return true;
            case R.id.navigation_notifications:
                Notifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
