package com.wolasoft.maplenou.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wolasoft.maplenou.data.api.services.CategoryService;
import com.wolasoft.maplenou.data.entities.Category;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository implements ICategoryRepository{

    private CategoryService service;

    @Inject
    public CategoryRepository(CategoryService service) {
        this.service = service;
    }

    @Override
    public LiveData<List<Category>> getAll() {
        final MutableLiveData<List<Category>> data = new MutableLiveData<>();
        this.service.getAll().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
