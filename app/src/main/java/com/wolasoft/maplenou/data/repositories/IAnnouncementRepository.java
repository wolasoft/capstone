package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.dto.Search;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Photo;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import java.io.File;
import java.util.List;

import retrofit2.Call;

public interface IAnnouncementRepository {
    Call<ApiResponse<Announcement>> fetchAllFromApi(final int firstPage, Search search);
    LiveData<Announcement> fetchOneFromApi(final String uuid);
    void saveToDb(final Announcement announcement);
    LiveData<Announcement> fetchOneFromDb(String uuid);
    void deleteFromDb(final Announcement announcement);
    DataSource.Factory<Integer, Announcement> fetchAllFromDb();
    void create(String title, String price, String description, String localization, String cityUuid,
                String categoryUuid, List<File> photos, CallBack<Announcement> callBack);
}
