package com.wolasoft.maplenou.data.entities;

import java.util.Date;

public class Person {
    private String uuid;
    private String lastName;
    private String firstName;
    private String gender;
    private String birthdayDate;
    private String placeOfBirth;
    private String status;
    private Date creationDate;
    private Date modificationDate;
    private Date deletionDate;
    private Card card;

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBirthdayDate() {
        return birthdayDate;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String fullName() {
        return this.firstName + " " + this.lastName;
    }

    public Card getCard() {
        return card;
    }
}
