package ru.ijava.tracker.model;

import android.content.Context;
import android.location.Location;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by levchenko on 20.07.2017.
 */

public class YandexMap {

    private static final String ADDITIONAL_CODE_PATTERN = "$ADDITIONAL_CODE$";
    private static final String BALLOON_CONTENT_PATTERN = "$BALLOON_CONTENT$";
    private static final String ICON_COLOR_PATTERN = "$ICON_COLOR$";
    private static final String BALLOON_INDEX_PATTERN = "$BALLOON_INDEX$";
    private static final String BALLOON_LATITUDE_PATTERN = "$BALLOON_LATITUDE$";
    private static final String BALLOON_LONGITUDE_PATTERN = "$BALLOON_LONGITUDE$";
    private static final String POLYLINE_COORDINATES_PATTERN = "$POLYLINE_COORDINATES$";
    private static final String BOUNDS_COORDINATES_PATTERN = "$BOUNDS_COORDINATES$";

    private static final String LAST_BALLOON_COLOR = "Blue";
    private static final String COMMON_BALLOON_COLOR = "Grey";

    private static final double DEFAULT_CENTER_LATITUDE = 44.758002;
    private static final double DEFAULT_CENTER_LONGITUDE = 37.379895;

    private static final double GEO_OBJECT_PADDING_GROUP = 0.05d;
    private static final double GEO_OBJECT_PADDING_SINGLE = 0.005d;

    private static final int ONLY_ONE_LOCATION = 1;
    private static final int NO_ONE_LOCATION = 0;
    private static final int FIRST_ELEMENT_INDEX = 0;

    private Context context;

    public YandexMap(Context context) {
        this.context = context;
    }

    public String getHtml(Device device) {
        String indexHtml = readAsset("index.html");
        String balloonJS = readAsset("balloon.js");
        String polylineJS = readAsset("polyline.js");
        String setBoundsJS = readAsset("set_bounds.js");

        ArrayList<Location> locationsHistory = device.getLocationsHistory();

        StringBuilder additionalCode = new StringBuilder();

        String balloonsCode = generateBalloonsCode(device, locationsHistory, balloonJS);
        additionalCode.append(balloonsCode);

        String polylineCode = generatePolylineCode(locationsHistory, polylineJS);
        additionalCode.append(polylineCode);

        String setBoundsCode = generateSetBoundsCode(locationsHistory, setBoundsJS);
        additionalCode.append(setBoundsCode);

        indexHtml = addAdditionalCode(additionalCode.toString(), indexHtml);

        return indexHtml;
    }

    private String addAdditionalCode(String additionalCode, String sourceCode) {
        sourceCode = sourceCode.replace(ADDITIONAL_CODE_PATTERN, additionalCode);
        return sourceCode;
    }

    private String readAsset(String name) {
        try {
            InputStream is = context.getAssets().open(name);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String generateBalloonsCode(Device device, ArrayList<Location> locationsHistory, String sourceCode) {
        StringBuilder balloonsCode = new StringBuilder();

        if(device != null && locationsHistory != null) {
            int lastIndex = locationsHistory.size() - 1;
            String balloonColor = COMMON_BALLOON_COLOR;
            for (int i = 0; i < locationsHistory.size(); i++) {
                Location location = locationsHistory.get(i);
                if (i == lastIndex) {
                    balloonColor = LAST_BALLOON_COLOR;
                }
                String balloonCode = generateBalloonCode(device, location, balloonColor, sourceCode);
                balloonsCode.append(balloonCode);
            }
        }

        return balloonsCode.toString();
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

    private String generateSetBoundsCode(ArrayList<Location> locationsHistory, String setBoundsSourceCode) {
        double minLatitude;
        double maxLatitude;
        double minLongitude;
        double maxLongitude;

        if (locationsHistory != null && locationsHistory.size() != NO_ONE_LOCATION)
        {
            if(locationsHistory.size() == ONLY_ONE_LOCATION) {
                minLatitude = locationsHistory.get(FIRST_ELEMENT_INDEX).getLatitude() - GEO_OBJECT_PADDING_SINGLE;
                maxLatitude = locationsHistory.get(FIRST_ELEMENT_INDEX).getLatitude() + GEO_OBJECT_PADDING_SINGLE;
                minLongitude = locationsHistory.get(FIRST_ELEMENT_INDEX).getLongitude() - GEO_OBJECT_PADDING_SINGLE;
                maxLongitude = locationsHistory.get(FIRST_ELEMENT_INDEX).getLongitude() + GEO_OBJECT_PADDING_SINGLE;

            }
            else {
                minLatitude = getMinLatitudeLocation(locationsHistory).getLatitude() - GEO_OBJECT_PADDING_GROUP;
                maxLatitude = getMaxLatitudeLocation(locationsHistory).getLatitude() + GEO_OBJECT_PADDING_GROUP;
                minLongitude = getMinLongitudeLocation(locationsHistory).getLongitude() - GEO_OBJECT_PADDING_GROUP;
                maxLongitude = getMaxLongitudeLocation(locationsHistory).getLongitude() + GEO_OBJECT_PADDING_GROUP;
            }
        }
        else {
            minLatitude = DEFAULT_CENTER_LATITUDE - GEO_OBJECT_PADDING_SINGLE;;
            maxLatitude = DEFAULT_CENTER_LATITUDE + GEO_OBJECT_PADDING_SINGLE;
            minLongitude = DEFAULT_CENTER_LONGITUDE - GEO_OBJECT_PADDING_SINGLE;;
            maxLongitude = DEFAULT_CENTER_LONGITUDE + GEO_OBJECT_PADDING_SINGLE;
        }

        StringBuilder coordinates = new StringBuilder();
        coordinates.append("[[");
        coordinates.append(minLatitude);
        coordinates.append(",");
        coordinates.append(minLongitude);
        coordinates.append("],[");
        coordinates.append(maxLatitude);
        coordinates.append(",");
        coordinates.append(maxLongitude);
        coordinates.append("]]");
        return setBoundsSourceCode.replace(BOUNDS_COORDINATES_PATTERN, coordinates.toString());
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
