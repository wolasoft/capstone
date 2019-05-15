package com.wolasoft.maplenou.data.repositories;

import android.util.Log;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.api.LoadingState;
import com.wolasoft.maplenou.data.api.errors.ErrorUtils;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.database.dao.AnnouncementDao;
import com.wolasoft.maplenou.data.dto.Search;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Photo;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public Call<ApiResponse<Announcement>> fetchAllFromApi(final int firstPage, Search search) {
        String title = null;
        String categoryUuid = null;
        String cityUuid = null;

        if (search != null) {
            title = search.getTitle();
            categoryUuid = search.getCategory() != null ? search.getCategory().getUuid() : null;
            cityUuid = search.getCity() != null ? search.getCity().getUuid() : null;
        }

        return this.announcementService.getAll(
                firstPage,
                title,
                categoryUuid,
                cityUuid);
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

    @Override
    public void create(
            String title, String price, String description, String localization, String cityUuid,
            String categoryUuid, List<File> photos, CallBack<Announcement> callBack) {
        final String TEXT_PLAIN_MEDIA_TYPE = "text/plain";
        final String IMAGE_MEDIA_TYPE = "image/*";
        RequestBody titleBody = RequestBody.create(MediaType.parse(TEXT_PLAIN_MEDIA_TYPE), title);
        RequestBody priceBody = RequestBody.create(MediaType.parse(TEXT_PLAIN_MEDIA_TYPE), price);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse(TEXT_PLAIN_MEDIA_TYPE),
                description);
        RequestBody localizationBody = RequestBody.create(MediaType.parse(TEXT_PLAIN_MEDIA_TYPE),
                localization);
        RequestBody cityUuidBody = RequestBody.create(MediaType.parse(TEXT_PLAIN_MEDIA_TYPE),
                cityUuid);
        RequestBody categoryUuidBody = RequestBody.create(MediaType.parse(TEXT_PLAIN_MEDIA_TYPE),
                categoryUuid);

        Map<String, RequestBody> files = new HashMap<>();

        if (photos != null && photos.size() > 0) {
            for (int i=0; i<photos.size(); i++) {
                RequestBody requestBody = RequestBody.create(MediaType.parse(IMAGE_MEDIA_TYPE),
                        photos.get(i));
                files.put(photos.get(i).getName(), requestBody);
            }
        }

        announcementService.create(
                titleBody,
                priceBody,
                descriptionBody,
                localizationBody,
                cityUuidBody,
                categoryUuidBody,
                files
        ).enqueue(new Callback<Announcement>() {
            @Override
            public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        callBack.onSuccess(response.body());
                    } else {
                        callBack.onError(ErrorUtils.parse(response));
                    }
                }
            }

            @Override
            public void onFailure(Call<Announcement> call, Throwable t) {
                if (callBack != null) {
                    callBack.onError(null);
                }
            }
        });
    }
}
