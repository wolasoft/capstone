package com.wolasoft.maplenou.di;

import com.wolasoft.maplenou.MainActivity;
import com.wolasoft.maplenou.data.api.services.AccountService;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.api.services.AuthService;
import com.wolasoft.maplenou.data.api.services.CategoryService;
import com.wolasoft.maplenou.data.api.services.CityService;
import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.di.modules.ApiModule;
import com.wolasoft.maplenou.di.modules.ContextModule;
import com.wolasoft.maplenou.di.modules.DataModule;
import com.wolasoft.maplenou.di.modules.DatabaseModule;
import com.wolasoft.maplenou.di.modules.RepositoryModule;
import com.wolasoft.maplenou.di.modules.UtilsModule;
import com.wolasoft.maplenou.views.account.AccountFragment;
import com.wolasoft.maplenou.views.account.subscribe.SubscribeFragment;
import com.wolasoft.maplenou.views.account.subscribe.SubscribeSuccessFragment;
import com.wolasoft.maplenou.views.announcement.create.CreateAnnouncementFragment;
import com.wolasoft.maplenou.views.announcement.details.AnnouncementDetailsFragment;
import com.wolasoft.maplenou.views.announcement.details.AnnouncementDetailsViewModelFactory;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementDataSource;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementDataSourceFactory;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementListFragment;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementViewModelFactory;
import com.wolasoft.maplenou.views.category.CategoryListFragment;
import com.wolasoft.maplenou.views.city.CityListFragment;
import com.wolasoft.maplenou.views.favorite.details.FavoriteDetailsFragment;
import com.wolasoft.maplenou.views.favorite.list.FavoriteListFragment;
import com.wolasoft.maplenou.views.login.LoginFragment;
import com.wolasoft.maplenou.views.search.SearchFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApiModule.class, ContextModule.class, DataModule.class, DatabaseModule.class,
        RepositoryModule.class, UtilsModule.class})
public interface AppComponent {
    // api service
    AnnouncementService announcementService();
    AccountService accountService();
    AuthService authService();
    CategoryService categoryService();
    CityService cityService();
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
    void inject(FavoriteListFragment fragment);
    void inject(FavoriteDetailsFragment fragment);
    void inject(SubscribeFragment fragment);
    void inject(LoginFragment fragment);
    void inject(AccountFragment fragment);
    void inject(CategoryListFragment fragment);
    void inject(CityListFragment fragment);
    void inject(CreateAnnouncementFragment fragment);
    void inject(SubscribeSuccessFragment fragment);
    void inject(SearchFragment fragment);
    void inject(MainActivity activity);
}
