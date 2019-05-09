package com.wolasoft.maplenou.views.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.data.repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private CategoryRepository repository;

    public CategoryViewModel(CategoryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Category>> getCategories() {
        return this.repository.getAll();
    }
}
