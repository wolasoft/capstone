package com.wolasoft.maplenou.data.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DataTypeConverter {
    @TypeConverter
    public Date fromTimestamp(Long date) {
        if (date != null) {
            return new Date(date);
        } else {
            return null;
        }
    }

    public Long toTimestamp(Date date) {
        return date.getTime();
    }

    public String byteToString(byte[] stream) {
        return stream.toString();
    }

    public byte[] stringToByte(String stream) {
        return stream.getBytes();
    }
}
