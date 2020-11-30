package com.simpleharmonics.kismis.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.simpleharmonics.kismis.databases.daos.CommentDao;
import com.simpleharmonics.kismis.databases.daos.CommentReactionDao;
import com.simpleharmonics.kismis.databases.daos.VideoDao;
import com.simpleharmonics.kismis.databases.daos.VideoReactionDao;
import com.simpleharmonics.kismis.databases.entities.CommentEntity;
import com.simpleharmonics.kismis.databases.entities.CommentReactionEntity;
import com.simpleharmonics.kismis.databases.entities.VideoEntity;
import com.simpleharmonics.kismis.databases.entities.VideoReactionEntity;

@Database(entities = {VideoReactionEntity.class, CommentReactionEntity.class, CommentEntity.class, VideoEntity.class}, version = 1)
public abstract class CustomDatabase extends RoomDatabase {

    public abstract VideoReactionDao videoReactionDao();

    public abstract CommentReactionDao commentReactionDao();

    public abstract CommentDao commentDao();

    public abstract VideoDao videoDao();
}
