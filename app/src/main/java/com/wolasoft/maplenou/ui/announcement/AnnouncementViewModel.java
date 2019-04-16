package com.wolasoft.maplenou.ui.announcement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.wolasoft.maplenou.data.entities.Announcement;

public class AnnouncementViewModel extends ViewModel {

    private LiveData<PagedList<Announcement>> itemPagedList;

    public AnnouncementViewModel(AnnouncementDataSourceFactory dataSourceFactory) {
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(AnnouncementDataSource.PAGE_SIZE).build();

        this.itemPagedList = (new LivePagedListBuilder(dataSourceFactory, pagedListConfig))
                .build();
    }

    public LiveData<PagedList<Announcement>> getItemPagedList() {
        return this.itemPagedList;
    }
}
