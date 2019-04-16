package com.wolasoft.maplenou.ui.announcement;

import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementDataSource extends PageKeyedDataSource<Integer, Announcement> {

    static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 1;
    private AnnouncementRepository repository;

    @Inject
    public AnnouncementDataSource(AnnouncementRepository repository) {
        this.repository = repository;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Announcement> callback) {
        this.repository.getAll(FIRST_PAGE)
                .enqueue(new Callback<ApiResponse<Announcement>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Announcement>> call, Response<ApiResponse<Announcement>> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body().data, null, FIRST_PAGE + 1);
                        } else {
                            // TODO propagate the error
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Announcement>> call, Throwable t) {
                        // TODO propagate the error
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
        this.repository.getAll(FIRST_PAGE)
                .enqueue(new Callback<ApiResponse<Announcement>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Announcement>> call, Response<ApiResponse<Announcement>> response) {
                        if (response.isSuccessful()) {
                            Integer nextKey = response.body().hasMore ? params.key + 1 : null;
                            callback.onResult(response.body().data, nextKey);
                        } else {
                            // TODO propagate the error
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Announcement>> call, Throwable t) {
                        // TODO propagate the error
                    }
                });
    }
}
