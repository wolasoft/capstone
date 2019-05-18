package com.wolasoft.maplenou.data.database.dao;

import com.wolasoft.maplenou.data.entities.Announcement;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AnnouncementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Announcement announcement);
    @Query("SELECT * FROM announcements WHERE uuid = :uuid")
    LiveData<Announcement> getByUuid(String uuid);
    @Delete()
    void delete(Announcement announcement);
    @Query("DELETE FROM announcements WHERE uuid = :uuid")
    void delete(String uuid);
    @Query("SELECT * FROM announcements ORDER BY id DESC")
    DataSource.Factory<Integer, Announcement> getAllPaged();
}
