package com.wolasoft.maplenou.views.announcement.list;

import com.wolasoft.maplenou.data.api.LoadingState;
import com.wolasoft.maplenou.data.dto.Search;
import com.wolasoft.maplenou.data.entities.Announcement;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class AnnouncementViewModel extends ViewModel {

    private AnnouncementDataSourceFactory dataSourceFactory;
    private LiveData<PagedList<Announcement>> itemPagedList;
    private LiveData<LoadingState> progressLiveStatus;
    private Search search;

    public AnnouncementViewModel(AnnouncementDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.search = null;
        this.itemPagedList = config(null);
    }

    private LiveData<PagedList<Announcement>> config(Search search) {
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(AnnouncementDataSource.PAGE_SIZE)
                        .setPageSize(AnnouncementDataSource.PAGE_SIZE).build();

        dataSourceFactory.setSearch(search);
        this.itemPagedList = (new LivePagedListBuilder(dataSourceFactory, pagedListConfig))
                .build();

        progressLiveStatus = Transformations.switchMap(
                dataSourceFactory.getAnnouncementLiveDataSource(),
                AnnouncementDataSource::getProgressLiveStatus);

        return this.itemPagedList;
    }

    public void search(LifecycleOwner owner, Search search) {
        this.search = search;
        this.itemPagedList.removeObservers(owner);
        this.itemPagedList = config(search);
    }

    public LiveData<LoadingState> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    public LiveData<PagedList<Announcement>> getItemPagedList() {
        return this.itemPagedList;
    }
}
