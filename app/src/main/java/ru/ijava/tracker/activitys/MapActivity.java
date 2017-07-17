package ru.ijava.tracker.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import ru.ijava.tracker.R;
import ru.ijava.tracker.model.Device;
import ru.ijava.tracker.model.PositionSystem;
import ru.ijava.tracker.model.Tools;

import android.location.Location;

/**
 * Created by levchenko on 21.06.2017.
 */
public class MapActivity extends AppCompatActivity {
    public static final String DEVICE_KEY = "device_key";

    public static final String MAP_CENTER_LATITUDE_PATTERN = "$LATITUDE$";
    public static final String MAP_CENTER_LONGITUDE_PATTERN = "$LONGITUDE$";
    public static final String BALLOON_CONTENT_PATTERN = "$BALLOON_CONTENT$";
    public static final String ICON_COLOR_PATTERN = "$ICON_COLOR$";
    public static final String GEO_OBJECTS_PATTERN = "$GEO_OBJECTS$";
    public static final String BALLOON_INDEX_PATTERN = "$BALLOON_INDEX$";
    public static final String BALLOON_LATITUDE_PATTERN = "$BALLOON_LATITUDE$";
    public static final String BALLOON_LONGITUDE_PATTERN = "$BALLOON_LONGITUDE$";
    public static final String MAP_ZOOM_PATTERN = "$ZOOM$";
    public static final String POLYLINE_COORDINATES_PATTERN = "$POLYLINE_COORDINATES$";

    public static final String LAST_BALLOON_COLOR = "Blue";
    public static final String COMMON_BALLOON_COLOR = "Grey";

    public static final double DEFAULT_CENTER_LATITUDE = 55.641468;
    public static final double DEFAULT_CENTER_LONGITUDE = 37.442406;

    public static final int DEFAULT_MAP_ZOOM = 10;
    public static final int ONLY_ONE_LOCATION = 1;
    public static final int ZOOM_ONE_LOCATION = 16;
    public static final int FIRST_ELEMENT_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        Device device = null;
        if (bundle != null) {
            device = (Device) bundle.getParcelable(DEVICE_KEY);//bundle.getSerializable(DEVICE_KEY);
        }

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String indexHtml = readAsset("index.html");
        String balloonJS = readAsset("balloon.js");
        String polylineJS = readAsset("polyline.js");

        ArrayList<Location> locationsHistory = device.getLocationsHistory();

        if(device != null && locationsHistory != null) {
            StringBuilder geoObjects = new StringBuilder();

            int lastIndex = locationsHistory.size() - 1;
            String balloonColor = COMMON_BALLOON_COLOR;
            for (int i = 0; i < locationsHistory.size(); i++) {
                Location location = locationsHistory.get(i);
                if(i == lastIndex) {
                    balloonColor = LAST_BALLOON_COLOR;
                }
                String balloonCode = generateBalloonCode(device, location, balloonColor, balloonJS);
                geoObjects.append(balloonCode);
            }

            String polylineCode = generatePolylineCode(locationsHistory, polylineJS);
            geoObjects.append(polylineCode);

            indexHtml = indexHtml.replace(GEO_OBJECTS_PATTERN, geoObjects.toString());

            Location centerLocation = determineCenterLocation(locationsHistory);
            indexHtml = indexHtml.replace(MAP_CENTER_LATITUDE_PATTERN, Double.toString(centerLocation.getLatitude()));
            indexHtml = indexHtml.replace(MAP_CENTER_LONGITUDE_PATTERN, Double.toString(centerLocation.getLongitude()));
        }
        else {
            indexHtml = indexHtml.replace(MAP_CENTER_LATITUDE_PATTERN, Double.toString(DEFAULT_CENTER_LATITUDE));
            indexHtml = indexHtml.replace(MAP_CENTER_LONGITUDE_PATTERN, Double.toString(DEFAULT_CENTER_LONGITUDE));
            indexHtml = indexHtml.replace(GEO_OBJECTS_PATTERN, "");
        }

        //set map zoom
        int zoomMap = determineScale(locationsHistory);
        indexHtml = indexHtml.replace(MAP_ZOOM_PATTERN, Integer.toString(zoomMap));

        webView.loadDataWithBaseURL(
                "http://ru.yandex.api.yandexmapswebviewexample.ymapapp",
                indexHtml,
                "text/html",
                "UTF-8",
                null
        );
    }

    private String readAsset(String name) {
        try {
            InputStream is = getAssets().open(name);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String generateBalloonCode(Device device, Location balloonLocation, String balloonColor, String sourceCode) {
        if(device != null && balloonLocation != null) {
            double latitude = balloonLocation.getLatitude();
            double longitude = balloonLocation.getLongitude();
            long timestamp = balloonLocation.getTime();
            String balloonContent = device.getNickName() + "<BR>" + new Date(timestamp) +
                    "<BR>Provider: " + balloonLocation.getProvider();

            sourceCode = sourceCode.replace(BALLOON_INDEX_PATTERN, Long.toString(timestamp));
            sourceCode = sourceCode.replace(BALLOON_LATITUDE_PATTERN, Double.toString(latitude));
            sourceCode = sourceCode.replace(BALLOON_LONGITUDE_PATTERN, Double.toString(longitude));
            sourceCode = sourceCode.replace(BALLOON_CONTENT_PATTERN, balloonContent);
            sourceCode = sourceCode.replace(ICON_COLOR_PATTERN, balloonColor);
        }
        else
        {
            return "";
        }

        return sourceCode;
    }

    private String generatePolylineCode(ArrayList<Location> locationsHistory, String sourceCode) {
        if(locationsHistory != null && locationsHistory.size() > 1)
        {
            Collections.sort(locationsHistory, Tools.comparator);

            StringBuilder polylineCoordinates = new StringBuilder();
            polylineCoordinates.append("[");
            for (Location location : locationsHistory) {
                //generate polyline coords [[55.80, 37.50], [55.80, 37.40], [55.70, 37.50], [55.70, 37.40]]
                polylineCoordinates.append("[" + location.getLatitude() + "," + location.getLongitude() + "],");
            }
            polylineCoordinates.append("]");
            sourceCode = sourceCode.replace(POLYLINE_COORDINATES_PATTERN, polylineCoordinates.toString());
        }
        else
        {
            return "";
        }

        return sourceCode;
    }

    private int determineScale(ArrayList<Location> locationHistory) {
        int zoom = DEFAULT_MAP_ZOOM;
        if (locationHistory != null)
        {
            if(locationHistory.size() == ONLY_ONE_LOCATION) {
                zoom = ZOOM_ONE_LOCATION;
            }
            else {
                //TODO Вычисляем маштаб с учетом рассояния между точками, чтобы все вместились и не слишком мелко
                float[] results = new float[3];
                double minLatitude = getMinLatitudeLocation(locationHistory).getLatitude();
                double maxLatitude = getMaxLatitudeLocation(locationHistory).getLatitude();
                double minLongitude = getMinLongitudeLocation(locationHistory).getLongitude();
                double maxLongitude = getMaxLongitudeLocation(locationHistory).getLongitude();
                Location.distanceBetween(minLatitude, minLongitude, maxLatitude, maxLongitude, results);
                float distance = results[0];

                //TODO map.setBounds([[60,-40], [20,60]])  и можно применить костыль в виде отступа, что к одиночной позиции, что к групповой. Так что похоже setZoom для нас декпрекатед, будем отображать требуемый кусок через установку границ
                //TODO незабудь убрать костыль из assets/index.html костыль выглядит как статический setBounds, его необходимо автоматизировать
//                Log.i("RELE", "minLatitude = " + minLatitude);
//                Log.i("RELE", "maxLatitude = " + maxLatitude);
//                Log.i("RELE", "minLongitude = " + minLongitude);
//                Log.i("RELE", "maxLongitude = " + maxLongitude);
            }
        }

        return zoom;
    }

    //TODO В перспективе необходимо центрировать карту относительно выводимых точек и выставлять маштаб карты такой чтобы все точки вписались в экран с небольши отступом по краям
    private Location determineCenterLocation(ArrayList<Location> locationHistory) {
        // Набросок метода определения центра и маштаба по выводимым точкам
        double centerLatitude;
        double centerLongitude;
        if(locationHistory.size() == ONLY_ONE_LOCATION) {
            centerLatitude = locationHistory.get(FIRST_ELEMENT_INDEX).getLatitude();
            centerLongitude = locationHistory.get(FIRST_ELEMENT_INDEX).getLongitude();
        }
        else {
            double minLatitude = getMinLatitudeLocation(locationHistory).getLatitude();
            double maxLatitude = getMaxLatitudeLocation(locationHistory).getLatitude();
            double minLongitude = getMinLongitudeLocation(locationHistory).getLongitude();
            double maxLongitude = getMaxLongitudeLocation(locationHistory).getLongitude();

            centerLatitude = (maxLatitude - minLatitude)/2 + minLatitude;
            centerLongitude = (maxLongitude - minLongitude)/2 + minLongitude;
        }

        Location location = new Location(PositionSystem.TRACKER_PROVIDER);
        location.setLatitude(centerLatitude);
        location.setLongitude(centerLongitude);
        location.setTime(0);

        return location;
    }

    private Location getMinLatitudeLocation(ArrayList<Location> locationsList) {
        Location minLatitudeLocation = null;
        for (Location location : locationsList) {
            if(minLatitudeLocation == null) {
                minLatitudeLocation = location;
            }
            if(location.getLatitude() < minLatitudeLocation.getLatitude()) {
                minLatitudeLocation = location;
            }
        }
        return minLatitudeLocation;
    }

    private Location getMaxLatitudeLocation(ArrayList<Location> locationsList) {
        Location maxLatitudeLocation = null;
        for (Location location : locationsList) {
            if(maxLatitudeLocation == null) {
                maxLatitudeLocation = location;
            }
            if(location.getLatitude() > maxLatitudeLocation.getLatitude()) {
                maxLatitudeLocation = location;
            }
        }
        return maxLatitudeLocation;
    }

    private Location getMinLongitudeLocation(ArrayList<Location> locationsList) {
        Location minLongitudeLocation = null;
        for (Location location : locationsList) {
            if(minLongitudeLocation == null) {
                minLongitudeLocation = location;
            }
            if(location.getLatitude() < minLongitudeLocation.getLatitude()) {
                minLongitudeLocation = location;
            }
        }
        return minLongitudeLocation;
    }

    private Location getMaxLongitudeLocation(ArrayList<Location> locationsList) {
        Location maxLongitudeLocation = null;
        for (Location location : locationsList) {
            if(maxLongitudeLocation == null) {
                maxLongitudeLocation = location;
            }
            if(location.getLatitude() > maxLongitudeLocation.getLatitude()) {
                maxLongitudeLocation = location;
            }
        }
        return maxLongitudeLocation;
    }

}
