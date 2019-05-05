package com.wolasoft.maplenou.data.entities;

import java.util.Date;

public class Card {
    private String uuid;
    private String type;
    private String number;
    private Date issueDate;
    private Date expirationDate;
    private String issuingOffice;
    private String nationality;
    private String status;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;

    public String getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getIssuingOffice() {
        return issuingOffice;
    }

    public String getNationality() {
        return nationality;
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
