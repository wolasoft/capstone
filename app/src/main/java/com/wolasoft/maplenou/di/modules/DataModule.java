package com.wolasoft.maplenou.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;
import com.wolasoft.maplenou.data.preferences.AppPreferences;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.ui.announcement.details.AnnouncementDetailsViewModelFactory;
import com.wolasoft.maplenou.ui.announcement.favorite.details.FavoriteDetailsViewModelFactory;
import com.wolasoft.maplenou.ui.announcement.favorite.list.AnnouncementFavoriteViewModelFactory;
import com.wolasoft.maplenou.ui.announcement.list.AnnouncementDataSource;
import com.wolasoft.maplenou.ui.announcement.list.AnnouncementDataSourceFactory;
import com.wolasoft.maplenou.ui.announcement.list.AnnouncementViewModelFactory;

import javax.inject.Singleton;

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
    AnnouncementRepository provideAnnouncementRepository(
            AnnouncementService service, AnnouncementDao announcementDao) {
        return new AnnouncementRepository(service, announcementDao);
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

    // view model factories
    @Provides
    @Singleton
    AnnouncementViewModelFactory provideAnnouncementViewModelFactory(
            AnnouncementDataSourceFactory factory) {
        return new AnnouncementViewModelFactory(factory);
    }

    @Provides
    @Singleton
    AnnouncementDetailsViewModelFactory provideAnnouncementDetailsViewModelFactory(
            AnnouncementRepository repository) {
        return new AnnouncementDetailsViewModelFactory(repository);
    }

    @Provides
    @Singleton
    AnnouncementFavoriteViewModelFactory provideAnnouncementFavoriteViewModelFactory(
            AnnouncementRepository repository) {
        return new AnnouncementFavoriteViewModelFactory(repository);
    }

    @Provides
    @Singleton
    FavoriteDetailsViewModelFactory provideFavoriteDetailsViewModelFactory(
            AnnouncementRepository repository) {
        return new FavoriteDetailsViewModelFactory(repository);
    }
}
