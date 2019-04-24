package com.wolasoft.maplenou.data.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.data.entities.City;
import com.wolasoft.maplenou.data.entities.Photo;

import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class DataTypeConverter {
    private Gson gson = new Gson();

    @TypeConverter
    public Date fromTimestamp(Long date) {
        if (date != null) {
            return new Date(date);
        } else {
            return null;
        }
    }

    @TypeConverter
    public Long toTimestamp(Date date) {
        if (date != null) {
            return date.getTime();
        }

        return 0L;
    }

    @TypeConverter
    public City stringToCity(String data) {
        if (data != null) {
            Type type = new TypeToken<City>(){}.getType();
            return this.gson.fromJson(data, type);
        }

        return null;
    }

    @TypeConverter
    public String cityToString(City city) {
        if (city != null) {
            return this.gson.toJson(city);
        }

        return "";
    }

    @TypeConverter
    public Category stringToCategory(String data) {
        if (data != null) {
            Type type = new TypeToken<Category>(){}.getType();
            return this.gson.fromJson(data, type);
        }

        return null;
    }

    @TypeConverter
    public String categoryToString(Category category) {
        if (category != null) {
            return this.gson.toJson(category);
        }

        return "";
    }

    @TypeConverter
    public List<Photo> stringToPhotos(String data) {
        if (data != null) {
            Type type = new TypeToken<List<Photo>>(){}.getType();
            return this.gson.fromJson(data, type);
        }

        return null;
    }

    @TypeConverter
    public String potosToString(List<Photo> photos) {
        if (photos != null) {
            Type type = new TypeToken<List<Photo>>(){}.getType();
            return this.gson.toJson(photos, type);
        }

        return "";
    }
}
