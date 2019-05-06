package com.wolasoft.maplenou.views.city;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wolasoft.maplenou.data.repositories.CategoryRepository;
import com.wolasoft.maplenou.data.repositories.CityRepository;
import com.wolasoft.maplenou.views.category.CategoryViewModel;

import javax.inject.Inject;

public class CityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private CityRepository repository;

    @Inject
    public CityViewModelFactory(CityRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CityViewModel(this.repository);
    }
}
