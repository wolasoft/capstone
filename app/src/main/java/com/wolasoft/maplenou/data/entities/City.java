package com.wolasoft.maplenou.data.entities;

import java.util.Date;

public class City {
    private int id;
    private String uuid;
    private String name;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;

    public String getName() {
        return name;
    }
}
