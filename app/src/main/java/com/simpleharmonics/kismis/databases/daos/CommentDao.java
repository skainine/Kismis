package com.simpleharmonics.kismis.databases.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.simpleharmonics.kismis.databases.entities.CommentEntity;
import com.simpleharmonics.kismis.databases.entities.CommentReactionEntity;

import java.util.List;

@Dao
public interface CommentDao {

    @Query("SELECT * FROM comments")
    List<CommentEntity> getAll();

    @Insert
    void insert(CommentReactionEntity commentReactionEntity);

    @Query("DELETE FROM comments")
    void deleteAll();

    @Delete
    void delete(CommentEntity commentEntity);
}
