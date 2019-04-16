package com.wolasoft.maplenou.ui.announcement;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.wolasoft.maplenou.data.entities.Announcement;

import javax.inject.Inject;

public class AnnouncementDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Announcement>> itemLiveDataSource ;
    private AnnouncementDataSource announcementDataSource;

    @Inject
    public AnnouncementDataSourceFactory(AnnouncementDataSource announcementDataSource) {
        this.announcementDataSource = announcementDataSource;
        this.itemLiveDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Announcement> create() {
        itemLiveDataSource.postValue(announcementDataSource);
        return announcementDataSource;
    }
}
