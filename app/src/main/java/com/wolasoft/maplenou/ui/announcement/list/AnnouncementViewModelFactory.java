package com.wolasoft.maplenou.ui.announcement.list;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AnnouncementViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private AnnouncementDataSourceFactory factory;

    @Inject
    public AnnouncementViewModelFactory(AnnouncementDataSourceFactory factory) {
        this.factory = factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AnnouncementViewModel(this.factory);
    }
}
