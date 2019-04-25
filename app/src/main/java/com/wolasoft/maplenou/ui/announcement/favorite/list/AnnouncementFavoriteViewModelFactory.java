package com.wolasoft.maplenou.ui.announcement.favorite.list;

import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AnnouncementFavoriteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AnnouncementRepository repository;
    @Inject
    public AnnouncementFavoriteViewModelFactory(AnnouncementRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AnnouncementFavoriteViewModel(this.repository);
    }
}
