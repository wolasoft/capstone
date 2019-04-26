package com.wolasoft.maplenou.views.favorite.list;

import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FavoriteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AnnouncementRepository repository;
    @Inject
    public FavoriteViewModelFactory(AnnouncementRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoriteViewModel(this.repository);
    }
}
