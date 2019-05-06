package com.wolasoft.maplenou.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class City implements Parcelable {
    private int id;
    private String uuid;
    private String name;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;

    protected City(Parcel in) {
        id = in.readInt();
        uuid = in.readString();
        name = in.readString();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uuid);
        dest.writeString(name);
    }
}
