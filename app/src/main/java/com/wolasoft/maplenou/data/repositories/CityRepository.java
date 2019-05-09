package com.wolasoft.maplenou.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wolasoft.maplenou.data.api.services.CityService;
import com.wolasoft.maplenou.data.entities.City;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityRepository implements ICityRepository{

    private CityService service;

    @Inject
    public CityRepository(CityService service) {
        this.service = service;
    }

    @Override
    public LiveData<List<City>> getAll() {
        final MutableLiveData<List<City>> data = new MutableLiveData<>();
        this.service.getAll().enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
