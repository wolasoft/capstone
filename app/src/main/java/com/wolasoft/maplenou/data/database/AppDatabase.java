package com.wolasoft.maplenou.data.database;

import android.arch.persistence.room.RoomDatabase;

//TODO uncomment
/*@Database(
        version = AppDatabase.DATABASE_VERSION,
        entities = {}
)
@TypeConverters(DataTypeConverter.class)*/
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "maplenou";
    static final int DATABASE_VERSION = 1;
}
