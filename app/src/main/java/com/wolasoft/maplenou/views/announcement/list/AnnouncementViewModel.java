package com.wolasoft.maplenou.views.announcement.list;

import com.wolasoft.maplenou.data.api.LoadingState;
import com.wolasoft.maplenou.data.entities.Announcement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class AnnouncementViewModel extends ViewModel {

    private AnnouncementDataSourceFactory dataSourceFactory;
    private LiveData<PagedList<Announcement>> itemPagedList;
    private LiveData<LoadingState> progressLiveStatus;

    public AnnouncementViewModel(AnnouncementDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        config();
    }

    private void config() {
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(AnnouncementDataSource.PAGE_SIZE)
                        .setPageSize(AnnouncementDataSource.PAGE_SIZE).build();

        this.itemPagedList = (new LivePagedListBuilder(dataSourceFactory, pagedListConfig))
                .build();

        progressLiveStatus = Transformations.switchMap(
                dataSourceFactory.getAnnouncementLiveDataSource(),
                AnnouncementDataSource::getProgressLiveStatus);
    }

    public LiveData<LoadingState> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    public LiveData<PagedList<Announcement>> getItemPagedList() {
        return this.itemPagedList;
    }
}
