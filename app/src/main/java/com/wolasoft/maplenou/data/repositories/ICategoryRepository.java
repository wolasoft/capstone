package com.wolasoft.maplenou.data.repositories;

import androidx.lifecycle.LiveData;

import com.wolasoft.maplenou.data.entities.Category;

import java.util.List;

public interface ICategoryRepository {
    LiveData<List<Category>> getAll();
}
