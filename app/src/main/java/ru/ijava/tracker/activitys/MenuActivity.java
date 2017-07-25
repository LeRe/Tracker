package ru.ijava.tracker.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ru.ijava.tracker.R;
import ru.ijava.tracker.db.DBHelper;
import ru.ijava.tracker.model.Device;
import ru.ijava.tracker.model.PositionSystem;
import ru.ijava.tracker.network.Client;
import ru.ijava.tracker.services.ServerService;
import ru.ijava.tracker.services.TrackerService;

public class MenuActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    public static final int POSITIONS_LAST = 0;
    public static final int POSITIONS_ALL = 1;

    //private TextView mTextMessage;
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
                    //startMapActivity();
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
        //mTextMessage.setText(R.string.title_notifications);
    }

    private void dashboard() {
        //mTextMessage.setText(R.string.title_dashboard);
    }

    private void home() {
        //mTextMessage.setText(R.string.title_home);
        new Thread(new Client()).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        device = new Device(this);
        positionSystem = new PositionSystem(this, device);


        //start service
        Intent intentTracker = new Intent(this, TrackerService.class);
        startService(intentTracker);

        //TODO need deBug
        Intent intentServer = new Intent(this, ServerService.class);
        startService(intentServer);
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
                //startMapActivity();
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
                    //Permitions granted!!!
                }
                break;
        }
    }

    private void startDBStatisticsActivity() {
        Intent intent = new Intent(this, DBActivity.class);
        intent.putExtra(MapActivity.DEVICE_KEY, device);
        startActivity(intent);
    }

    public void startMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.DEVICE_KEY, device);
        startActivity(intent);
    }

    public void loadRequestedPositions(int positionsId) {
        device.initializeLocationsHistory();
        DBHelper sqliteDB = new DBHelper(this);

        if (positionsId == POSITIONS_LAST)
        {
            // извлечем из базы и положим в объект  девайс последнюю позицию
            // класть будем в список вывода
            // при отображении на карте потом будем определять
            //если позиция в списке одна отображаем ее как единственную (последнюю)
            //если не одна - строим маршрут из позиций
            sqliteDB.getDeviceLastLocation(device);
        }
        else if (positionsId == POSITIONS_ALL)
        {
            // извлекаем из базы энное количество позиций удовлетворяющих условию
            // кладем их также в список вывода принадлежащий объекту Девисе,
            // на их основе будем строить маршрут
            sqliteDB.getDeviceLocationsHistory(device);

        }
    }
}