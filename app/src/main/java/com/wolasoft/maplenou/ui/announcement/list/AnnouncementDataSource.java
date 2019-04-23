package com.wolasoft.maplenou.ui.announcement.list;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.api.LoadingState;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementDataSource extends PageKeyedDataSource<Integer, Announcement> {

    static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 1;
    private AnnouncementRepository repository;
    private MutableLiveData<LoadingState> progressLiveStatus;

    @Inject
    public AnnouncementDataSource(AnnouncementRepository repository) {
        this.repository = repository;
        this.progressLiveStatus = new MutableLiveData<>();
    }

    public MutableLiveData<LoadingState> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Announcement> callback) {
        progressLiveStatus.postValue(LoadingState.LOADING);
        this.repository.getAll(FIRST_PAGE)
                .enqueue(new Callback<ApiResponse<Announcement>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Announcement>> call, Response<ApiResponse<Announcement>> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body().data, null, FIRST_PAGE + 1);
                            progressLiveStatus.postValue(LoadingState.LOADED);
                        } else {
                            progressLiveStatus.postValue(LoadingState.ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Announcement>> call, Throwable t) {
                        progressLiveStatus.postValue(LoadingState.ERROR);
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Announcement> callback) {
        this.repository.getAll(FIRST_PAGE)
                .enqueue(new Callback<ApiResponse<Announcement>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Announcement>> call, Response<ApiResponse<Announcement>> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            callback.onResult(response.body().data, adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Announcement>> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Announcement> callback) {
        progressLiveStatus.postValue(LoadingState.LOADING_MORE);
        this.repository.getAll(FIRST_PAGE)
                .enqueue(new Callback<ApiResponse<Announcement>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Announcement>> call, Response<ApiResponse<Announcement>> response) {
                        if (response.isSuccessful()) {
                            Integer nextKey = response.body().hasMore ? params.key + 1 : null;
                            callback.onResult(response.body().data, nextKey);
                            progressLiveStatus.postValue(LoadingState.LOADED_MORE);
                        } else {
                            progressLiveStatus.postValue(LoadingState.ERROR_LOADING_MORE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Announcement>> call, Throwable t) {
                        progressLiveStatus.postValue(LoadingState.ERROR_LOADING_MORE);
                    }
                });
    }
}
