package com.wolasoft.maplenou.views.favorite.details;

import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

class FavoriteDetailsViewModel extends ViewModel {
    private LiveData<Announcement> announcementLiveData;
    private AnnouncementRepository repository;

    FavoriteDetailsViewModel(AnnouncementRepository repository) {
        this.repository = repository;
    }

    void init(String uuid) {
        if (this.announcementLiveData != null) {
            return;
        }

        announcementLiveData = this.repository.fetchOneFromDb(uuid);
    }

    LiveData<Announcement> getAnnouncementLiveData() {
        return announcementLiveData;
    }
}
