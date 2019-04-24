package com.wolasoft.maplenou.di.modules;

import android.content.Context;

import com.wolasoft.maplenou.data.database.AppDatabase;
import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private AppDatabase appDatabase;

    public DatabaseModule(Context context) {
        appDatabase = Room.databaseBuilder(
                context, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase() {
        return this.appDatabase;
    }

    @Singleton
    @Provides
    AnnouncementDao provideAnnouncementDao() {
        return appDatabase.announcementDao();
    }
}
