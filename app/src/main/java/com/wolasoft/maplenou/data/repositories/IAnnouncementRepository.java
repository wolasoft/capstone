package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.entities.Announcement;

import retrofit2.Call;

public interface IAnnouncementRepository {
    Call<ApiResponse<Announcement>> getAll(final int firstPage);
}
