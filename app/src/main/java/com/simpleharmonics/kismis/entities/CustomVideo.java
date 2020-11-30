package com.simpleharmonics.kismis.entities;

public class CustomVideo {

    private final String videoThumbnailLink;
    private final String videoLink;
    private String videoID;
    private long videoLikes;
    private long videoDislikes;
    private long videoComments;
    private String videoCaption;
    private String videoMusic;
    private String ownerID;
    private String ownerTAG;
    private String ownerDPLink;
    private int userLikeStatus;
    private String uploadTimeStamp;

    public CustomVideo(String videoThumbnailLink, String videoLink) {
        this.videoThumbnailLink = videoThumbnailLink;
        this.videoLink = videoLink;
    }

    public CustomVideo(String videoID, String videoThumbnailLink, String videoLink, long videoLikes, long videoDislikes, long videoComments, String videoCaption, String videoMusic, String ownerID, String ownerTAG, String ownerDPLink, int userLikeStatus) {
        this.videoID = videoID;
        this.videoThumbnailLink = videoThumbnailLink;
        this.videoLink = videoLink;
        this.videoLikes = videoLikes;
        this.videoDislikes = videoDislikes;
        this.videoComments = videoComments;
        this.videoCaption = videoCaption;
        this.videoMusic = videoMusic;
        this.ownerID = ownerID;
        this.ownerTAG = ownerTAG;
        this.ownerDPLink = ownerDPLink;
        this.userLikeStatus = userLikeStatus;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getVideoThumbnailLink() {
        return videoThumbnailLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public long getVideoLikes() {
        return videoLikes;
    }

    public long getVideoDislikes() {
        return videoDislikes;
    }

    public long getVideoComments() {
        return videoComments;
    }

    public String getVideoCaption() {
        return videoCaption;
    }

    public String getVideoMusic() {
        return videoMusic;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getOwnerTAG() {
        return ownerTAG;
    }

    public String getOwnerDPLink() {
        return ownerDPLink;
    }

    public int getUserLikeStatus() {
        return userLikeStatus;
    }

    public void setUserLikeStatus(int userLikeStatus) {
        this.userLikeStatus = userLikeStatus;
    }

    public String getUploadTimeStamp() {
        return uploadTimeStamp;
    }

    public void setUploadTimeStamp(String uploadTimeStamp) {
        this.uploadTimeStamp = uploadTimeStamp;
    }
}
