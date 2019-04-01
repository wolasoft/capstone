package com.wolasoft.maplenou.data.entities;

import java.util.Date;
import java.util.List;

public class Announcement {
    private int id;
    private String uuid;
    private String title;
    private String description;
    private int price;
    private String localization;
    private String status;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;
    private List<Photo> photos;
    private City city;

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getLocalization() {
        return localization;
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

    public List<Photo> getPhotos() {
        return photos;
    }

    public City getCity() {
        return city;
    }
}
