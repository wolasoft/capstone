package com.wolasoft.maplenou.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.preferences.AppPreferences;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.ui.announcement.AnnouncementDataSource;
import com.wolasoft.maplenou.ui.announcement.AnnouncementDataSourceFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    AppPreferences provideAppPreferences(Context context, Gson gson) {
        return new AppPreferences(context, gson);
    }

    // repositories
    @Provides
    @Singleton
    AnnouncementRepository provideAnnouncementRepository(AnnouncementService service) {
        return new AnnouncementRepository(service);
    }

    // data sources
    @Provides
    @Singleton
    AnnouncementDataSource provideAnnouncementDataSource(AnnouncementRepository repository) {
        return new AnnouncementDataSource(repository);
    }

    // data source factories
    @Provides
    @Singleton
    AnnouncementDataSourceFactory provideAnnouncementDataSourceFactory(
            AnnouncementDataSource dataSource) {
        return new AnnouncementDataSourceFactory(dataSource);
    }

}
