package com.wolasoft.maplenou.data.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.data.entities.City;

public class Search implements Parcelable {
    private Category category;
    private City city;
    private String title;

    public Search() { }

    public Search(Category category, City city, String title) {
        this.category = category;
        this.city = city;
        this.title = title;
    }

    protected Search(Parcel in) {
        category = in.readParcelable(Category.class.getClassLoader());
        city = in.readParcelable(City.class.getClassLoader());
        title = in.readString();
    }

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };

    public Category getCategory() {
        return category;
    }

    public City getCity() {
        return city;
    }

    public String getTitle() {
        return title;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(category, flags);
        dest.writeParcelable(city, flags);
        dest.writeString(title);
    }
}
