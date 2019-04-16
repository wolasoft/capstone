package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.api.services.AnnouncementService;
import com.wolasoft.maplenou.data.entities.Announcement;

import javax.inject.Inject;

import retrofit2.Call;

public class AnnouncementRepository implements IAnnouncementRepository{

    private AnnouncementService announcementService;

    @Inject
    public AnnouncementRepository(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    public Call<ApiResponse<Announcement>> getAll(final int firstPage) {
        return this.announcementService.getAll(firstPage);
    }
}
