package com.wolasoft.maplenou.ui.announcement.details;

import com.wolasoft.maplenou.data.api.LoadingState;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AnnouncementDetailsViewModel extends ViewModel {
    private LiveData<Announcement> announcementLiveData;
    private LiveData<Announcement> dbAnnouncementLiveData;
    private LiveData<LoadingState> progressLiveStatus;
    private AnnouncementRepository repository;

    public AnnouncementDetailsViewModel(AnnouncementRepository repository) {
        this.repository = repository;
    }

    public void init(String uuid) {
        if (this.announcementLiveData != null) {
            return;
        }

        announcementLiveData = this.repository.fetchOneFromApi(uuid);
        progressLiveStatus = this.repository.getFetchOneLiveStatus();
        dbAnnouncementLiveData = this.repository.fetchOneFromDb(uuid);
    }

    public LiveData<Announcement> getAnnouncementLiveData() {
        return announcementLiveData;
    }

    public LiveData<LoadingState> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    public LiveData<Announcement> getDbAnnouncementLiveData() {
        return dbAnnouncementLiveData;
    }
}
