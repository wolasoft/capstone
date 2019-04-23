package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.api.LoadingState;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.entities.Announcement;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementRepository implements IAnnouncementRepository{

    private AnnouncementService announcementService;
    private MutableLiveData<LoadingState> fetchOneLiveStatus;

    @Inject
    public AnnouncementRepository(AnnouncementService announcementService) {
        this.announcementService = announcementService;
        this.fetchOneLiveStatus = new MutableLiveData<>();
    }

    public Call<ApiResponse<Announcement>> getAll(final int firstPage) {
        return this.announcementService.getAll(firstPage);
    }

    @Override
    public LiveData<Announcement> getByUuid(String uuid) {
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
}
