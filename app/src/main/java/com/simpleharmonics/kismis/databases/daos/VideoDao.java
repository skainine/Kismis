package com.simpleharmonics.kismis.databases.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.simpleharmonics.kismis.databases.entities.VideoEntity;

import java.util.List;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM videos")
    List<VideoEntity> getAll();

    @Insert
    void insert(VideoEntity videoEntity);

    @Query("DELETE FROM videos")
    void deleteAll();

    @Delete
    void delete(VideoEntity videoEntity);
}
