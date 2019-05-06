package com.wolasoft.maplenou.views.city;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wolasoft.maplenou.data.entities.City;
import com.wolasoft.maplenou.data.repositories.CityRepository;

import java.util.List;

public class CityViewModel extends ViewModel {

    private CityRepository repository;

    public CityViewModel(CityRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<City>> getCities() {
        return this.repository.getAll();
    }
}
