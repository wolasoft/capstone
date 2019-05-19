package com.wolasoft.maplenou.views.account.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

public class AccountAnnouncementViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private AccountAnnouncementDataSourceFactory factory;

    @Inject
    public AccountAnnouncementViewModelFactory(AccountAnnouncementDataSourceFactory factory) {
        this.factory = factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AccountAnnouncementViewModel(this.factory);
    }
}
