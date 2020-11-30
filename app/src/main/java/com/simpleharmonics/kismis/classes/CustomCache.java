package com.simpleharmonics.kismis.classes;

import android.app.Activity;

import com.simpleharmonics.kismis.databases.CustomDatabase;
import com.simpleharmonics.kismis.databases.daos.CommentReactionDao;
import com.simpleharmonics.kismis.databases.daos.VideoDao;
import com.simpleharmonics.kismis.databases.entities.CommentReactionEntity;
import com.simpleharmonics.kismis.databases.entities.VideoEntity;
import com.simpleharmonics.kismis.entities.CustomVideo;

import java.util.List;

public class CustomCache {

    /**
     * Adds a new video to the cache of uploaded videos.
     * Runs on an independent Thread.
     **/
    public static void addToUserVideos(Activity activity, CustomVideo customVideo) {
        new Thread(() -> {
            final CustomDatabase databaseAccess = CustomGlobal.getDatabaseAccess(activity.getApplicationContext());
            VideoDao videoDao = databaseAccess.videoDao();
            VideoEntity videoEntity = new VideoEntity();
            videoEntity.videoThumbnailLink = customVideo.getVideoThumbnailLink();
            videoEntity.videoID = customVideo.getVideoID();
            videoEntity.videoLink = customVideo.getVideoLink();
            videoEntity.uploadTimeStamp = customVideo.getUploadTimeStamp();
            videoDao.insert(videoEntity);
        }).start();
    }

    /**
     * Caches all the comment reactions made by the user on any comment.
     * Runs on an independent Thread.
     **/
    public static void cacheUserCommentReactions(Activity activity) {
        new Thread(() -> {

        }).start();
    }

    /**
     * Caches all the comments made by the user on any video.
     * Runs on an independent Thread.
     **/
    public static void cacheUserComments(Activity activity) {
        new Thread(() -> {

        }).start();
    }

    /**
     * Caches all the reactions made by the user in any video.
     * Runs on an independent Thread.
     **/
    public static void cacheUserVideoReactions(Activity activity) {
        new Thread(() -> {

        }).start();
    }

    /**
     * Caches the videos uploaded by the user.
     * Runs on an independent Thread.
     **/
    public static void cacheUserVideos(Activity activity) {
        new Thread(() -> {

        }).start();
    }

    /**
     * Get user's reaction on a public comment given the comment id.
     * Runs on parent thread because parent thread is already an independent thread and result is required to proceed.
     **/
    public static int getUserCommentReaction(Activity activity, String commentID) {
        final CustomDatabase databaseAccess = CustomGlobal.getDatabaseAccess(activity.getApplicationContext());
        CommentReactionDao commentReactionDao = databaseAccess.commentReactionDao();
        List<CommentReactionEntity> commentReactionEntityList = commentReactionDao.getOne(commentID);
        if (commentReactionEntityList == null) {
            return 0;
        } else {
            if (commentReactionEntityList.size() < 1) {
                return 0;
            } else {
                return commentReactionEntityList.get(0).reaction;
            }
        }
    }
}
