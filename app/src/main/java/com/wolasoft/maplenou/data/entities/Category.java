package com.wolasoft.maplenou.data.entities;

import java.util.Date;

public class Category {
    private int id;
    private String uuid;
    private String name;
    private String description;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;

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
