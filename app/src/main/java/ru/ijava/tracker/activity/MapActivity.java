package ru.ijava.tracker.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

import ru.ijava.tracker.R;
import ru.ijava.tracker.model.Device;

/**
 * Created by levchenko on 21.06.2017.
 */
public class MapActivity extends AppCompatActivity {
    public static final String DEVICE_KEY = "device_key";
    public static final String LATITUDE_PATTERN = "$LATITUDE$";
    public static final String LONGITUDE_PATTERN = "$LONGITUDE$";
    public static final String NICK_NAME_PATTERN = "$NICK_NAME$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        Device mDevice = (Device) bundle.getSerializable(DEVICE_KEY);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        try {
            InputStream is = getAssets().open("index.html");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String htmlText = new String(buffer);

            htmlText = htmlText.replace(LATITUDE_PATTERN, Double.toString(mDevice.getLocation().getLatitude()));
            htmlText = htmlText.replace(LONGITUDE_PATTERN, Double.toString(mDevice.getLocation().getLongitude()));
            htmlText = htmlText.replace(NICK_NAME_PATTERN, mDevice.getNickName());

            myWebView.loadDataWithBaseURL(
                    "http://ru.yandex.api.yandexmapswebviewexample.ymapapp",
                    htmlText,
                    "text/html",
                    "UTF-8",
                    null
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
