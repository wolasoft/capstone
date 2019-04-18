package com.wolasoft.maplenou.ui.announcement;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.wolasoft.maplenou.data.entities.Announcement;

import javax.inject.Inject;

public class AnnouncementDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<AnnouncementDataSource> announcementLiveDataSource;
    private AnnouncementDataSource announcementDataSource;

    @Inject
    public AnnouncementDataSourceFactory(AnnouncementDataSource announcementDataSource) {
        this.announcementDataSource = announcementDataSource;
        this.announcementLiveDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Announcement> create() {
        announcementLiveDataSource.postValue(announcementDataSource);
        return announcementDataSource;
    }

    public MutableLiveData<AnnouncementDataSource> getAnnouncementLiveDataSource() {
        return announcementLiveDataSource;
    }
}
