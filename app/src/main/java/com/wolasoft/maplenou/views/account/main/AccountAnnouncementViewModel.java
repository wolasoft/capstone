package com.wolasoft.maplenou.views.account.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.wolasoft.maplenou.data.api.LoadingState;
import com.wolasoft.maplenou.data.entities.Announcement;

public class AccountAnnouncementViewModel extends ViewModel {

    private AccountAnnouncementDataSourceFactory dataSourceFactory;
    private LiveData<PagedList<Announcement>> itemPagedList;
    private LiveData<LoadingState> progressLiveStatus;

    public AccountAnnouncementViewModel(AccountAnnouncementDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    private LiveData<PagedList<Announcement>> config(String accountUuid) {
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(AccountAnnouncementDataSource.PAGE_SIZE)
                        .setPageSize(AccountAnnouncementDataSource.PAGE_SIZE).build();

        dataSourceFactory.setAccountUuid(accountUuid);
        this.itemPagedList = (new LivePagedListBuilder(dataSourceFactory, pagedListConfig))
                .build();

        progressLiveStatus = Transformations.switchMap(
                dataSourceFactory.getAnnouncementLiveDataSource(),
                AccountAnnouncementDataSource::getProgressLiveStatus);

        return this.itemPagedList;
    }

    public LiveData<LoadingState> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    public LiveData<PagedList<Announcement>> getItemPagedList(String accountUuid) {
        this.itemPagedList = config(accountUuid);
        return this.itemPagedList;
    }
}
