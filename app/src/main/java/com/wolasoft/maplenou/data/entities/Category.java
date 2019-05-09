package com.wolasoft.maplenou.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Category implements Parcelable {
    private int id;
    private String uuid;
    private String name;
    private String description;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;

    protected Category(Parcel in) {
        id = in.readInt();
        uuid = in.readString();
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uuid);
        dest.writeString(name);
        dest.writeString(description);
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public Date getDeletionDate() {
        return deletionDate;
    }
}
