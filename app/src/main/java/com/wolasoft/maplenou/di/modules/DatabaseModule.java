package com.wolasoft.maplenou.di.modules;

import android.content.Context;

import com.wolasoft.maplenou.data.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private AppDatabase appDatabase;

    public DatabaseModule(Context context) {
        // TODO uncomment
        // appDatabase = Room.databaseBuilder(
           //     context, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase() {
        // TODO uncomment return this.appDatabase;
        return null;
    }
}
