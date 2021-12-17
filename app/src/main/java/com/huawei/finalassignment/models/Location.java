package com.huawei.finalassignment.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    Double Latitude;
    Double Longitude;

    public Location(Double latitude, Double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    protected Location(Parcel in) {
        if (in.readByte() == 0) {
            Latitude = null;
        } else {
            Latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            Longitude = null;
        } else {
            Longitude = in.readDouble();
        }
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(Latitude);
        parcel.writeDouble(Longitude);
    }

    @Override
    public String toString() {
        return Latitude + "&" + Longitude;
    }
}
