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
    private Category category;

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

    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Announcement)) return false;

        Announcement that = (Announcement) o;

        if (id != that.id) return false;
        if (price != that.price) return false;
        if (!uuid.equals(that.uuid)) return false;
        if (!title.equals(that.title)) return false;
        if (!description.equals(that.description)) return false;
        if (!localization.equals(that.localization)) return false;
        if (!status.equals(that.status)) return false;
        if (!creationDate.equals(that.creationDate)) return false;
        if (!modificationDate.equals(that.modificationDate)) return false;
        if (!deletionDate.equals(that.deletionDate)) return false;
        if (!photos.equals(that.photos)) return false;
        if (!category.equals(that.category)) return false;
        return city.equals(that.city);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + uuid.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + price;
        result = 31 * result + localization.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + modificationDate.hashCode();
        result = 31 * result + deletionDate.hashCode();
        result = 31 * result + photos.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }
}
