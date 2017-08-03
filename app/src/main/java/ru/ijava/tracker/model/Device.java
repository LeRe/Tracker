package ru.ijava.tracker.model;

import android.content.Context;
import com.google.android.gms.iid.InstanceID;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

/**
 * Created by levchenko on 22.06.2017.
 */

public class Device implements Parcelable {
    private String id;
    private String nickName;
    private Location currentLocation;
    private ArrayList<Location> locationsHistory;

    public void putLocationToHistory(Location location) {
        if(this.locationsHistory == null) {
            initializeLocationsHistory();
        }
        this.locationsHistory.add(location);
    }

    public ArrayList<Location> getLocationsHistory() {
        //TODO Сделать сортировку по времени
        return locationsHistory;
    }

    public void initializeLocationsHistory() {
        this.locationsHistory = new ArrayList<Location>();
    }

    public Device(Context context) {
        String iid = InstanceID.getInstance(context).getId();
        setId(iid);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String deviceNickName = sharedPreferences.getString("edit_text_preference_nickname", "NO_PREFERENCE");
        setNickName(deviceNickName);
    }

    public Device(String id, String nickName) {
        setId(id);
        setNickName(nickName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nickName);
        dest.writeParcelable(currentLocation, 0);
        dest.writeTypedList(locationsHistory);
    }

    public Device(Parcel in) {
        this.id = in.readString();
        this.nickName = in.readString();
        this.currentLocation = in.readParcelable(Location.class.getClassLoader());
        this.initializeLocationsHistory();
        in.readTypedList(this.locationsHistory, Location.CREATOR);
    }

    public static final Parcelable.Creator<Device> CREATOR
            = new Parcelable.ClassLoaderCreator<Device>() {

        @Override
        public Device createFromParcel(Parcel source) {
            return new Device(source);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }

        @Override
        public Device createFromParcel(Parcel source, ClassLoader loader) {
            return new Device(source);
        }
    };
}
