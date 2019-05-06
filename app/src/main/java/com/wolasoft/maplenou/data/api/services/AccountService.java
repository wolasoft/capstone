package com.wolasoft.maplenou.data.api.services;

import com.google.gson.JsonObject;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccountService {
    @POST("accounts")
    Call<Token> create(@Body JsonObject object);
    @GET("accounts")
    Call<Account> getOne();
}
