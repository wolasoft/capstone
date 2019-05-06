package com.wolasoft.maplenou.data.api.services;

import com.wolasoft.maplenou.data.entities.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("categories")
    Call<List<Category>> getAll();
}
