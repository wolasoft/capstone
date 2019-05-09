package com.wolasoft.maplenou.data.repositories;

import androidx.lifecycle.LiveData;

import com.wolasoft.maplenou.data.entities.City;

import java.util.List;

public interface ICityRepository {
    LiveData<List<City>> getAll();
}
