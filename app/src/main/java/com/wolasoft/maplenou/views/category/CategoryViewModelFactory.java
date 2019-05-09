package com.wolasoft.maplenou.views.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wolasoft.maplenou.data.repositories.CategoryRepository;

import javax.inject.Inject;

public class CategoryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private CategoryRepository repository;

    @Inject
    public CategoryViewModelFactory(CategoryRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CategoryViewModel(this.repository);
    }
}
