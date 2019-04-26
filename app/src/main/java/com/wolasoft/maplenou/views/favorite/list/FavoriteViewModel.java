package com.wolasoft.maplenou.views.favorite.list;

import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.utils.Constants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class FavoriteViewModel extends ViewModel {

    private final LiveData<PagedList<Announcement>> favoriteAnnouncements;
    private AnnouncementRepository repository;

    public FavoriteViewModel(AnnouncementRepository repository) {
        this.repository = repository;
        favoriteAnnouncements = new LivePagedListBuilder<>(
                this.repository.fetchAllFromDb(), Constants.PAGE_SIZE).build();
    }

    public LiveData<PagedList<Announcement>> getFavoriteAnnouncements() {
        return favoriteAnnouncements;
    }
}
