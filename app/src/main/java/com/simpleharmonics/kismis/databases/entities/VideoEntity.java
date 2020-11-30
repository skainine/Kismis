package com.simpleharmonics.kismis.databases.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "videos", primaryKeys = {"video_id"})
public class VideoEntity {

    @NonNull
    @ColumnInfo(name = "video_id")
    public String videoID;

    @ColumnInfo(name = "video_thumbnail_link")
    public String videoThumbnailLink;

    @ColumnInfo(name = "video_link")
    public String videoLink;

    @ColumnInfo(name = "upload_time_stamp")
    public String uploadTimeStamp;
}
