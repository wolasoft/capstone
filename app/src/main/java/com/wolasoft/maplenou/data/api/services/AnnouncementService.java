package com.wolasoft.maplenou.data.api.services;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.entities.Announcement;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AnnouncementService {
    @GET("announcements")
    Call<ApiResponse<Announcement>> getAll(@Query("page") int page,
                                           @Query("title") String title,
                                           @Query("categoryUuid") String categoryUuid,
                                           @Query("cityUuid") String cityUuid);
    @GET("announcements/{uuid}")
    Call<Announcement> getOne(@Path("uuid") String uuid);
    @Multipart
    @POST("announcements")
    Call<Announcement> create(
            @Part("title") RequestBody title,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part("localization") RequestBody localization,
            @Part("cityUuid") RequestBody cityUuid,
            @Part("categoryUuid") RequestBody categoryUuid,
            @PartMap Map<String, RequestBody> photos
    );
}
