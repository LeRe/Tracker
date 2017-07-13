package ru.ijava.tracker.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import ru.ijava.tracker.R;
import ru.ijava.tracker.model.Device;
import ru.ijava.tracker.model.Location;

/**
 * Created by levchenko on 21.06.2017.
 */
public class MapActivity extends AppCompatActivity {
    public static final String DEVICE_KEY = "device_key";

    public static final String MAP_CENTER_LATITUDE_PATTERN = "$LATITUDE$";
    public static final String MAP_CENTER_LONGITUDE_PATTERN = "$LONGITUDE$";
    public static final String BALLOON_CONTENT_PATTERN = "$BALLOON_CONTENT$";
    public static final String ICON_COLOR_PATTERN = "$ICON_COLOR$";
    public static final String BALLOONS_PATTERN = "$BALLOONS$";
    public static final String BALLOON_INDEX_PATTERN = "$BALLOON_INDEX$";
    public static final String BALLOON_LATITUDE_PATTERN = "$BALLOON_LATITUDE$";
    public static final String BALLOON_LONGITUDE_PATTERN = "$BALLOON_LONGITUDE$";
    public static final String MAP_ZOOM_PATTERN = "$ZOOM$";
    public static final String POLYLINE_COORDINATES_PATTERN = "$POLYLINE_COORDINATES$";

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
            device = (Device) bundle.getSerializable(DEVICE_KEY);
        }

        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String indexHtml = readAsset("index.html");
        String balloonJS = readAsset("balloon.js");
        String polylineJS = readAsset("polyline.js");

        int mapZoom = DEFAULT_MAP_ZOOM;

        ArrayList<Location> locationsHistory = device.getLocationsHistory();
        if(device != null && locationsHistory != null) {

            StringBuilder balloonsCode = new StringBuilder();
            StringBuilder polylineCoordinates = new StringBuilder();
            polylineCoordinates.append("[");
            for (Location location : locationsHistory) {
                String balloonCode = generateBalloonCode(device, location, balloonJS);
                balloonsCode.append(balloonCode);

                polylineCoordinates.append("[" + location.getLatitude() + "," + location.getLongitude() + "],");
                //generate polyline coords
                /*
                        [[55.80, 37.50],
                        [55.80, 37.40],
                        [55.70, 37.50],
                        [55.70, 37.40]]
                 */
            }
            polylineCoordinates.append("]");
            polylineJS = polylineJS.replace(POLYLINE_COORDINATES_PATTERN, polylineCoordinates.toString());
            //TODO polyline рисует, незабыть отсортировать координаты по таймстампу, баллуун рисуем в конечной точке другим цветом, ну и привести код генерации карты в порядок (слишком много черновых набросков)
            balloonsCode.append(polylineJS);

            indexHtml = indexHtml.replace(BALLOONS_PATTERN, balloonsCode.toString());

            //TODO В перспективе необходимо центрировать карту относительно выводимых точек и выставлять маштаб карты такой чтобы все точки вписались в экран с небольши отступом по краям
            // Набросок метода определения центра и маштаба по выводимым точкам
            double centerLatitude = DEFAULT_CENTER_LATITUDE;
            double centerLongitude = DEFAULT_CENTER_LONGITUDE;
            if(device.getLocationsHistory().size() == ONLY_ONE_LOCATION) {
                mapZoom = ZOOM_ONE_LOCATION;
                centerLatitude = device.getLocationsHistory().get(FIRST_ELEMENT_INDEX).getLatitude();
                centerLongitude = device.getLocationsHistory().get(FIRST_ELEMENT_INDEX).getLongitude();
            }

            indexHtml = indexHtml.replace(MAP_CENTER_LATITUDE_PATTERN, Double.toString(centerLatitude));
            indexHtml = indexHtml.replace(MAP_CENTER_LONGITUDE_PATTERN, Double.toString(centerLongitude));
        }
        else {
            indexHtml = indexHtml.replace(MAP_CENTER_LATITUDE_PATTERN, Double.toString(DEFAULT_CENTER_LATITUDE));
            indexHtml = indexHtml.replace(MAP_CENTER_LONGITUDE_PATTERN, Double.toString(DEFAULT_CENTER_LONGITUDE));
            indexHtml = indexHtml.replace(BALLOONS_PATTERN, "");
        }

        //set map zoom
        indexHtml = indexHtml.replace(MAP_ZOOM_PATTERN, Integer.toString(mapZoom));

        myWebView.loadDataWithBaseURL(
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

    private String generateBalloonCode(Device device, Location balloonLocation, String sourceCode) {
        double latitude = balloonLocation.getLatitude();
        double longitude = balloonLocation.getLongitude();
        long timestamp = balloonLocation.getTime();
        String balloonContent = device.getNickName() + "<BR>" + new Date(timestamp);
        String balloonColor = "Blue";

        sourceCode = sourceCode.replace(BALLOON_INDEX_PATTERN, Long.toString(timestamp));
        sourceCode = sourceCode.replace(BALLOON_LATITUDE_PATTERN, Double.toString(latitude));
        sourceCode = sourceCode.replace(BALLOON_LONGITUDE_PATTERN, Double.toString(longitude));
        sourceCode = sourceCode.replace(BALLOON_CONTENT_PATTERN, balloonContent);
        sourceCode = sourceCode.replace(ICON_COLOR_PATTERN, balloonColor);

        return sourceCode;
    }
}
