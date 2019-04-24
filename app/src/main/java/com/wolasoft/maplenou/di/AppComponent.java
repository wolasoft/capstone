package com.wolasoft.maplenou.di;

import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.di.modules.ApiModule;
import com.wolasoft.maplenou.di.modules.ContextModule;
import com.wolasoft.maplenou.di.modules.DataModule;
import com.wolasoft.maplenou.di.modules.DatabaseModule;
import com.wolasoft.maplenou.ui.announcement.list.AnnouncementDataSource;
import com.wolasoft.maplenou.ui.announcement.list.AnnouncementDataSourceFactory;
import com.wolasoft.maplenou.ui.announcement.list.AnnouncementListFragment;
import com.wolasoft.maplenou.ui.announcement.list.AnnouncementViewModelFactory;
import com.wolasoft.maplenou.ui.announcement.details.AnnouncementDetailsFragment;
import com.wolasoft.maplenou.ui.announcement.details.AnnouncementDetailsViewModelFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApiModule.class, ContextModule.class, DataModule.class, DatabaseModule.class})
public interface AppComponent {
    // api service
    AnnouncementService announcementService();
    // dao
    AnnouncementDao announcementDao();
    // data repositories
    AnnouncementRepository announcementRepository();
    //data sources
    AnnouncementDataSource announcementDataSource();
    // data source factory
    AnnouncementDataSourceFactory announcementDataSourceFactory();
    // view model factories
    AnnouncementViewModelFactory announcementViewModelFactory();
    AnnouncementDetailsViewModelFactory announcementDetailsViewModelFactory();



    // injection methods
    void inject(AnnouncementListFragment fragment);
    void inject(AnnouncementDetailsFragment fragment);
}
