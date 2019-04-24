package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.entities.Announcement;

import androidx.lifecycle.LiveData;
import retrofit2.Call;

public interface IAnnouncementRepository {
    Call<ApiResponse<Announcement>> fetchAllFromApi(final int firstPage);
    LiveData<Announcement> fetchOneFromApi(final String uuid);
    void saveToDb(final Announcement announcement);
    LiveData<Announcement> fetchOneFromDb(String uuid);
    void deleteFromDb(final Announcement announcement);
}
