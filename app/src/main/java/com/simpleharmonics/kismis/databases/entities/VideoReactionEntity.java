package com.simpleharmonics.kismis.databases.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "video_reactions", primaryKeys = {"video_id"})
public class VideoReactionEntity {

    @NonNull
    @ColumnInfo(name = "video_id")
    public String videoID;

    @ColumnInfo(name = "reaction")
    public String reaction;
}
