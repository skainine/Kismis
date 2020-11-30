package com.simpleharmonics.kismis.databases.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "comments", primaryKeys = {"comment_id", "video_id"})
public class CommentEntity {

    @NonNull
    @ColumnInfo(name = "comment_id")
    public String commentID;

    @NonNull
    @ColumnInfo(name = "video_id")
    public String videoID;

    @ColumnInfo(name = "comment_text")
    public String comment_text;
}
