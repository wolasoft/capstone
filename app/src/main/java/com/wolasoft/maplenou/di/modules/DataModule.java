package com.wolasoft.maplenou.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.wolasoft.maplenou.data.api.services.AccountService;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.api.services.AuthService;
import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;
import com.wolasoft.maplenou.data.preferences.AppPreferences;
import com.wolasoft.maplenou.data.repositories.AccountRepository;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.data.repositories.AuthRepository;
import com.wolasoft.maplenou.views.announcement.details.AnnouncementDetailsViewModelFactory;
import com.wolasoft.maplenou.views.favorite.details.FavoriteDetailsViewModelFactory;
import com.wolasoft.maplenou.views.favorite.list.FavoriteViewModelFactory;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementDataSource;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementDataSourceFactory;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementViewModelFactory;

import javax.inject.Named;
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
    AppPreferences provideAppPreferences(Context context) {
        return new AppPreferences(context);
    }

    // repositories
    @Provides
    @Singleton
    AnnouncementRepository provideAnnouncementRepository(
            AnnouncementService service, AnnouncementDao announcementDao) {
        return new AnnouncementRepository(service, announcementDao);
    }

    @Provides
    @Singleton
    AccountRepository provideAccountRepository(AccountService service, AppPreferences preferences) {
        return new AccountRepository(service, preferences);
    }

    @Provides
    @Singleton
    AuthRepository provideAuthRepository(AuthService service) {
        return new AuthRepository(service);
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
    FavoriteViewModelFactory provideAnnouncementFavoriteViewModelFactory(
            AnnouncementRepository repository) {
        return new FavoriteViewModelFactory(repository);
    }

    @Provides
    @Singleton
    FavoriteDetailsViewModelFactory provideFavoriteDetailsViewModelFactory(
            AnnouncementRepository repository) {
        return new FavoriteDetailsViewModelFactory(repository);
    }
}
