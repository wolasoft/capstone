package com.wolasoft.maplenou.views.account.main;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.wolasoft.maplenou.data.entities.Announcement;

import javax.inject.Inject;

public class AccountAnnouncementDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<AccountAnnouncementDataSource> announcementLiveDataSource;
    private AccountAnnouncementDataSource announcementDataSource;
    private String accountUuid;

    @Inject
    public AccountAnnouncementDataSourceFactory(AccountAnnouncementDataSource announcementDataSource) {
        this.announcementDataSource = announcementDataSource;
        this.announcementLiveDataSource = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Announcement> create() {
        announcementDataSource.setAccountUuid(accountUuid);
        announcementLiveDataSource.postValue(announcementDataSource);
        return announcementDataSource;
    }

    public MutableLiveData<AccountAnnouncementDataSource> getAnnouncementLiveDataSource() {
        return announcementLiveDataSource;
    }

    public void setAccountUuid(String accountUuid) {
        this.accountUuid = accountUuid;
    }
}
