package com.wolasoft.maplenou.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Photo implements Parcelable {
    private int id;
    private String uuid;
    private String mimetype;
    private String file;
    private String status;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;

    protected Photo(Parcel in) {
        id = in.readInt();
        uuid = in.readString();
        mimetype = in.readString();
        file = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uuid);
        dest.writeString(mimetype);
        dest.writeString(file);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMimetype() {
        return mimetype;
    }

    public String getFile() {
        return file;
    }

    public String getStatus() {
        return status;
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
