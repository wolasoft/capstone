package com.wolasoft.maplenou.data.api.services;

import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccountService {
    @FormUrlEncoded
    @POST("accounts")
    Call<Token> create(
            @Field("email") String email,
            @Field("phoneNumber") String phoneNumber,
            @Field("password") String password,
            @Field("lastName") String lastName,
            @Field("firstName") String firstName);
    @GET("accounts")
    Call<Account> getOne();
}
