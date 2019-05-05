package com.wolasoft.maplenou.data.database;

import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;
import com.wolasoft.maplenou.data.entities.Announcement;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        version = AppDatabase.DATABASE_VERSION,
        entities = {Announcement.class},
        exportSchema = false)
@TypeConverters(DataTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "maplenou";
    static final int DATABASE_VERSION = 1;

    public abstract AnnouncementDao announcementDao();
}
