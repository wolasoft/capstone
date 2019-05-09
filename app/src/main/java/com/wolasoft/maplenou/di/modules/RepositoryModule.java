package com.wolasoft.maplenou.di.modules;

import com.wolasoft.maplenou.data.api.services.AccountService;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.api.services.AuthService;
import com.wolasoft.maplenou.data.api.services.CategoryService;
import com.wolasoft.maplenou.data.api.services.CityService;
import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;
import com.wolasoft.maplenou.data.preferences.AppPreferences;
import com.wolasoft.maplenou.data.repositories.AccountRepository;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.data.repositories.AuthRepository;
import com.wolasoft.maplenou.data.repositories.CategoryRepository;
import com.wolasoft.maplenou.data.repositories.CityRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
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

    @Provides
    @Singleton
    CategoryRepository provideCategoryRepository(CategoryService service) {
        return new CategoryRepository(service);
    }

    @Provides
    @Singleton
    CityRepository provideCityRepository(CityService service) {
        return new CityRepository(service);
    }
}
