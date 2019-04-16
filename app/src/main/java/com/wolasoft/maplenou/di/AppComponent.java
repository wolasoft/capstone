package com.wolasoft.maplenou.di;

import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.di.modules.ApiModule;
import com.wolasoft.maplenou.di.modules.ContextModule;
import com.wolasoft.maplenou.di.modules.DataModule;
import com.wolasoft.maplenou.di.modules.DatabaseModule;
import com.wolasoft.maplenou.ui.announcement.AnnouncementDataSource;
import com.wolasoft.maplenou.ui.announcement.AnnouncementDataSourceFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApiModule.class, ContextModule.class, DataModule.class, DatabaseModule.class})
public interface AppComponent {
    // api service
    AnnouncementService announcementService();
    // data repositories
    AnnouncementRepository announcementRepository();
    //data sources
    AnnouncementDataSource announcementDataSource();
    // data source factory
    AnnouncementDataSourceFactory announcementDataSourceFactory();

}
