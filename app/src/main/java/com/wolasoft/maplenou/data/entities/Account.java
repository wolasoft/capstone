package com.wolasoft.maplenou.data.entities;

import java.util.Date;

public class Account {
    private String uuid;
    private String email;
    private String phoneNumber;
    private String password;
    private String imageUrl;
    private Date lastLogin;
    private boolean certified;
    private String status;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;
    private Person person;

    public String getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public boolean isCertified() {
        return certified;
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

    public Person getPerson() {
        return person;
    }
}
