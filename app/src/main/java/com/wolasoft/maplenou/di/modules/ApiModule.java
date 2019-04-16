package com.wolasoft.maplenou.di.modules;

import com.wolasoft.maplenou.data.api.ApiConnector;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Provides
    @Singleton
    AnnouncementService provideAnnouncementService() {
        return ApiConnector.createRetrofitService(AnnouncementService.class);
    }
}
