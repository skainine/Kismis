package com.simpleharmonics.kismis.databases.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.simpleharmonics.kismis.databases.entities.CommentReactionEntity;

import java.util.List;

@Dao
public interface CommentReactionDao {

    @Query("SELECT * FROM comment_reactions")
    List<CommentReactionEntity> getAll();

    @Query("SELECT * FROM comment_reactions WHERE comment_id = :commentID")
    List<CommentReactionEntity> getOne(String commentID);

    @Insert
    void insert(CommentReactionEntity commentReactionEntity);

    @Query("DELETE FROM comment_reactions")
    void deleteAll();

    @Delete
    void delete(CommentReactionEntity commentReactionEntity);
}
