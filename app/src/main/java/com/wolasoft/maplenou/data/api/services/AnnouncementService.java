package com.wolasoft.maplenou.data.api.services;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.entities.Announcement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AnnouncementService {
    @GET("announcements")
    Call<ApiResponse<Announcement>> getAll(@Query("page") int page);
}
