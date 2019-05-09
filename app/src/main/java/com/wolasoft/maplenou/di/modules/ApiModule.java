package com.wolasoft.maplenou.di.modules;

import com.wolasoft.maplenou.data.api.ApiConnector;
import com.wolasoft.maplenou.data.api.interceptors.TokenInterceptor;
import com.wolasoft.maplenou.data.api.services.AccountService;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.api.services.AuthService;
import com.wolasoft.maplenou.data.api.services.CategoryService;
import com.wolasoft.maplenou.data.api.services.CityService;
import com.wolasoft.maplenou.data.preferences.AppPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    // interceptors
    @Provides
    @Singleton
    TokenInterceptor provideInterceptor(AppPreferences preferences) {
        return new TokenInterceptor(preferences);
    }

    @Provides
    @Singleton
    AnnouncementService provideAnnouncementService(TokenInterceptor tokenInterceptor) {
        return ApiConnector.createAuthenticatedRetrofitService(
                AnnouncementService.class, tokenInterceptor);
    }

    @Provides
    @Singleton
    AccountService provideAccountService(TokenInterceptor tokenInterceptor) {
        return ApiConnector.createAuthenticatedRetrofitService(AccountService.class, tokenInterceptor);
    }

    @Provides
    @Singleton
    AuthService provideAuthService() {
        return ApiConnector.createRetrofitService(AuthService.class);
    }

    @Provides
    @Singleton
    CategoryService provideCategoryService() {
        return ApiConnector.createRetrofitService(CategoryService.class);
    }

    @Provides
    @Singleton
    CityService provideCityService() {
        return ApiConnector.createRetrofitService(CityService.class);
    }
}
