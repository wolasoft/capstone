package com.wolasoft.maplenou.data.api.services;

import com.wolasoft.maplenou.data.entities.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CityService {
    @GET("cities")
    Call<List<City>> getAll();
}
