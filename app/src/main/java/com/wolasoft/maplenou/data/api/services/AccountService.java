package com.wolasoft.maplenou.data.api.services;

import com.google.gson.JsonObject;
import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountService {
    @POST("accounts")
    Call<Token> create(@Body JsonObject object);
    @GET("accounts")
    Call<Account> getOne();
    @GET("accounts/{uuid}/announcements")
    Call<ApiResponse<Announcement>> getAnnouncements(
            @Path ("uuid") String uuid, @Query("page") int page);
}
