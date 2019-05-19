package com.wolasoft.maplenou.data.api.services;

import com.google.gson.JsonObject;
import com.wolasoft.maplenou.data.entities.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth")
    Call<Token> login(@Body JsonObject object);
}
