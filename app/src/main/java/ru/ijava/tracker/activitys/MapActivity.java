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
    public static final String LATITUDE_PATTERN = "$LATITUDE$";
    public static final String LONGITUDE_PATTERN = "$LONGITUDE$";
    public static final String BALLOON_CONTENT_PATTERN = "$BALLOON_CONTENT$";
    public static final String ICON_COLOR_PATTERN = "$ICON_COLOR$";
    public static final String BALLOONS_PATTERN = "$BALLOONS$";
    public static final String BALLOON_INDEX_PATTERN = "$BALLOON_INDEX$";
    public static final String BALLOON_LATITUDE_PATTERN = "$BALLOON_LATITUDE$";
    public static final String BALLOON_LONGITUDE_PATTERN = "$BALLOON_LONGITUDE$";

    public static final double DOMODEDOVO_LONGITUDE = 37.747817;
    public static final double DOMODEDOVO_LATITUDE = 55.4430234;

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

        String htmlText = readAsset("index.html");
        String balloonJS = readAsset("balloon.js");

        ArrayList<Location> locationsHistory = device.getLocationsHistory();
        if(device != null && locationsHistory != null) {
            for (Location location : locationsHistory) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                long timestamp = location.getTime();


                //TODO !!!!!!! вот тут переделываем вывод одиночного баллууна на множественный
                htmlText = htmlText.replace(BALLOONS_PATTERN, balloonJS);
                htmlText = htmlText.replace(BALLOON_CONTENT_PATTERN,
                        device.getNickName() + "<BR>" + new Date(timestamp));
                htmlText = htmlText.replace(ICON_COLOR_PATTERN, "Blue");
            }


            //set map center
            //TODO В перспективе необходимо центрировать карту относительно выводимых точек и выставлять маштаб карты такой чтобы все точки вписались в экран с небольши отступом по краям
            htmlText = htmlText.replace(LATITUDE_PATTERN, Double.toString(DOMODEDOVO_LATITUDE));
            htmlText = htmlText.replace(LONGITUDE_PATTERN, Double.toString(DOMODEDOVO_LONGITUDE));
        }
        else {
            htmlText = htmlText.replace(LATITUDE_PATTERN, Double.toString(DOMODEDOVO_LATITUDE));
            htmlText = htmlText.replace(LONGITUDE_PATTERN, Double.toString(DOMODEDOVO_LONGITUDE));
            htmlText = htmlText.replace(BALLOONS_PATTERN, "");
        }

//        if(device != null && device.getCurrentLocation() != null) {
//            htmlText = htmlText.replace(LATITUDE_PATTERN, Double.toString(device.getCurrentLocation().getLatitude()));
//            htmlText = htmlText.replace(LONGITUDE_PATTERN, Double.toString(device.getCurrentLocation().getLongitude()));
//
//            htmlText = htmlText.replace(BALLOONS_PATTERN, balloonJS);
//            htmlText = htmlText.replace(BALLOON_CONTENT_PATTERN,
//                    device.getNickName() + "<BR>" + new Date(device.getCurrentLocation().getTime()));
//            htmlText = htmlText.replace(ICON_COLOR_PATTERN, "Blue");
//        }
//        else {
//            htmlText = htmlText.replace(LATITUDE_PATTERN, Double.toString(DOMODEDOVO_LATITUDE));
//            htmlText = htmlText.replace(LONGITUDE_PATTERN, Double.toString(DOMODEDOVO_LONGITUDE));
//            htmlText = htmlText.replace(BALLOONS_PATTERN, "");
//        }

        myWebView.loadDataWithBaseURL(
                "http://ru.yandex.api.yandexmapswebviewexample.ymapapp",
                htmlText,
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
}
