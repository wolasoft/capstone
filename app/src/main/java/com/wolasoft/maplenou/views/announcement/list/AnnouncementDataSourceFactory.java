package com.wolasoft.maplenou.views.announcement.list;

import com.wolasoft.maplenou.data.dto.Search;
import com.wolasoft.maplenou.data.entities.Announcement;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class AnnouncementDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<AnnouncementDataSource> announcementLiveDataSource;
    private AnnouncementDataSource announcementDataSource;
    private Search search;

    @Inject
    public AnnouncementDataSourceFactory(AnnouncementDataSource announcementDataSource) {
        this.announcementDataSource = announcementDataSource;
        this.announcementLiveDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Announcement> create() {
        announcementDataSource.setSearchParams(search);
        announcementLiveDataSource.postValue(announcementDataSource);
        return announcementDataSource;
    }

    public MutableLiveData<AnnouncementDataSource> getAnnouncementLiveDataSource() {
        return announcementLiveDataSource;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
