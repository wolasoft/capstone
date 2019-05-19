package com.wolasoft.maplenou.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.wolasoft.maplenou.data.preferences.AppPreferences;
import com.wolasoft.maplenou.data.repositories.AccountRepository;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.data.repositories.CategoryRepository;
import com.wolasoft.maplenou.data.repositories.CityRepository;
import com.wolasoft.maplenou.views.account.main.AccountAnnouncementDataSource;
import com.wolasoft.maplenou.views.account.main.AccountAnnouncementDataSourceFactory;
import com.wolasoft.maplenou.views.account.main.AccountAnnouncementViewModelFactory;
import com.wolasoft.maplenou.views.announcement.details.AnnouncementDetailsViewModelFactory;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementDataSource;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementDataSourceFactory;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementViewModelFactory;
import com.wolasoft.maplenou.views.category.CategoryViewModelFactory;
import com.wolasoft.maplenou.views.city.CityViewModelFactory;
import com.wolasoft.maplenou.views.favorite.details.FavoriteDetailsViewModelFactory;
import com.wolasoft.maplenou.views.favorite.list.FavoriteViewModelFactory;

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

    // data sources
    @Provides
    @Singleton
    AnnouncementDataSource provideAnnouncementDataSource(AnnouncementRepository repository) {
        return new AnnouncementDataSource(repository);
    }

    @Provides
    @Singleton
    AccountAnnouncementDataSource provideAccountAnnouncementDataSource(AccountRepository repository) {
        return new AccountAnnouncementDataSource(repository);
    }

    // data source factories
    @Provides
    @Singleton
    AnnouncementDataSourceFactory provideAnnouncementDataSourceFactory(
            AnnouncementDataSource dataSource) {
        return new AnnouncementDataSourceFactory(dataSource);
    }

    @Provides
    @Singleton
    AccountAnnouncementDataSourceFactory provideAccountAnnouncementDataSourceFactory(
            AccountAnnouncementDataSource dataSource) {
        return new AccountAnnouncementDataSourceFactory(dataSource);
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
    AccountAnnouncementViewModelFactory provideAccountAnnouncementViewModelFactory(
            AccountAnnouncementDataSourceFactory factory) {
        return new AccountAnnouncementViewModelFactory(factory);
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

    @Provides
    @Singleton
    CategoryViewModelFactory provideCategoryViewModelFactory(CategoryRepository repository) {
        return new CategoryViewModelFactory(repository);
    }

    @Provides
    @Singleton
    CityViewModelFactory provideCityViewModelFactory(CityRepository repository) {
        return new CityViewModelFactory(repository);
    }
}
