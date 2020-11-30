package com.simpleharmonics.kismis.databases.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.simpleharmonics.kismis.databases.entities.VideoReactionEntity;

import java.util.List;

@Dao
public interface VideoReactionDao {

    @Query("SELECT * FROM video_reactions")
    List<VideoReactionEntity> getAll();

    @Insert
    void insert(VideoReactionEntity videoReactionEntity);

    @Query("DELETE FROM video_reactions")
    void deleteAll();

    @Delete
    void delete(VideoReactionEntity videoReactionEntity);
}
