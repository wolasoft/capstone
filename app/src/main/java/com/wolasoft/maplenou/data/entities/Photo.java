package com.wolasoft.maplenou.data.entities;

import java.util.Date;

public class Photo {
    private int id;
    private String uuid;
    private String mimetype;
    private String file;
    private String status;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;

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
