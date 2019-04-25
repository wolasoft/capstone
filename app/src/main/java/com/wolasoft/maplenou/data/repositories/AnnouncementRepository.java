package com.wolasoft.maplenou.data.repositories;

import android.util.Log;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.api.LoadingState;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;
import com.wolasoft.maplenou.data.entities.Announcement;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementRepository implements IAnnouncementRepository{

    public final String TAG = "AnnouncementRepository";
    private AnnouncementService announcementService;
    private AnnouncementDao announcementDao;
    private MutableLiveData<LoadingState> fetchOneLiveStatus;

    @Inject
    public AnnouncementRepository(
            AnnouncementService announcementService,AnnouncementDao announcementDao) {
        this.announcementService = announcementService;
        this.announcementDao = announcementDao;
        this.fetchOneLiveStatus = new MutableLiveData<>();
    }

    public Call<ApiResponse<Announcement>> fetchAllFromApi(final int firstPage) {
        return this.announcementService.getAll(firstPage);
    }

    @Override
    public LiveData<Announcement> fetchOneFromApi(String uuid) {
        final MutableLiveData<Announcement> data = new MutableLiveData<>();
        this.announcementService.getOne(uuid).enqueue(new Callback<Announcement>() {
            @Override
            public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body());
                    fetchOneLiveStatus.postValue(LoadingState.LOADED);
                } else {
                    fetchOneLiveStatus.postValue(LoadingState.ERROR);
                }
            }

            @Override
            public void onFailure(Call<Announcement> call, Throwable t) {
                fetchOneLiveStatus.postValue(LoadingState.ERROR);
            }
        });

        return data;
    }

    public MutableLiveData<LoadingState> getFetchOneLiveStatus() {
        return fetchOneLiveStatus;
    }

    @Override
    public void saveToDb(final Announcement announcement) {
        Log.d(TAG, "Saving announcement");
        this.announcementDao.insert(announcement);
    }

    @Override
    public LiveData<Announcement> fetchOneFromDb(String uuid) {
        Log.d(TAG, "Fetching announcement by uuid");
        return this.announcementDao.getByUuid(uuid);
    }

    @Override
    public void deleteFromDb(final Announcement announcement) {
        Log.d(TAG, "Deleting announcement");
        this.announcementDao.delete(announcement);
    }

    @Override
    public DataSource.Factory<Integer, Announcement> fetchAllFromDb() {
        Log.d(TAG, "fetching paginated announcements from db");
        return this.announcementDao.getAllPaged();
    }
}
