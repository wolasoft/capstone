package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.entities.Announcement;

import androidx.lifecycle.LiveData;
import retrofit2.Call;

public interface IAnnouncementRepository {
    Call<ApiResponse<Announcement>> getAll(final int firstPage);
    LiveData<Announcement> getByUuid(final String uuid);
}
