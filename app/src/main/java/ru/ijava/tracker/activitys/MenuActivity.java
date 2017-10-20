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
import ru.ijava.tracker.model.LogSystem;
import ru.ijava.tracker.model.Preferences;
import ru.ijava.tracker.model.PrimaryDevice;

public class MenuActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int POSITIONS_LAST = 0;
    public static final int POSITIONS_ALL = 1;

    /**
     *
     *  TODO 2. Затащить проверку разрешения на определение позиции в PrimaryDevice
     *   TODO надо запустить проверку когда пользователь смотрит на активити,
     *   TODO    так как в тракер сервисе мы не увидим запрос
     *
     *  TODO ********************
     *
     *
     * */

    //private TextView mTextMessage;
    private Device device;
    //PositionSystem positionSystem;

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
                case R.id.navigation_map:
                    startMapActivity();
                    return true;
                case R.id.navigation_tools:
                    startToolsActivity();
                    return true;
                case R.id.navigation_settings:
                    startSettingsActivity();
                    return true;
            }
            return false;
        }

    };

    private void dashboard() {
        //mTextMessage.setText(R.string.title_dashboard);

    }

    private void home() {
        //mTextMessage.setText(R.string.title_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        PrimaryDevice primaryDevice;
        try {
            primaryDevice = PrimaryDevice.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            primaryDevice = PrimaryDevice.getInstance(this);
        }

        Preferences preferences = Preferences.getInstance(this);
        preferences.addChangePreferenceListener(primaryDevice);

        device = primaryDevice;


        LogSystem logSystem = LogSystem.getInstance(getApplicationContext());
        logSystem.save("Test log system", LogSystem.DebugLevel.DEBUG, LogSystem.OutputDirection.All);
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
            case R.id.navigation_map:
                startMapActivity();
                return true;
            case R.id.navigation_tools:
                startToolsActivity();
                return true;
            case R.id.navigation_settings:
                startSettingsActivity();
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

    private void startToolsActivity() {
        Intent intent = new Intent(this, ToolsActivity.class);
        startActivity(intent);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

    public void startMapActivity() {
        if(device == null || device.getLocationsHistory() == null)
        {
            loadRequestedPositions(POSITIONS_LAST);
        }

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