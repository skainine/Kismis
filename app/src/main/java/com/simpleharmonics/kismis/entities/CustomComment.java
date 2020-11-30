package com.simpleharmonics.kismis.entities;

public class CustomComment {

    private final String commentID;
    private final String commentOwnerID;
    private final String commentOwnerTAG;
    private final String commentOwnerDPLink;
    private final String commentTime;
    private final String commentText;
    private final long commentLikes;
    private final long commentDislikes;
    private int userReaction;

    public CustomComment(String commentID, String commentOwnerID, String commentOwnerTAG, String commentOwnerDPLink, String commentTime, String commentText, long commentLikes, long commentDislikes, int userReaction) {
        this.commentID = commentID;
        this.commentOwnerID = commentOwnerID;
        this.commentOwnerTAG = commentOwnerTAG;
        this.commentOwnerDPLink = commentOwnerDPLink;
        this.commentTime = commentTime;
        this.commentText = commentText;
        this.commentLikes = commentLikes;
        this.commentDislikes = commentDislikes;
        this.userReaction = userReaction;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getCommentOwnerID() {
        return commentOwnerID;
    }

    public String getCommentOwnerTAG() {
        return commentOwnerTAG;
    }

    public String getCommentOwnerDPLink() {
        return commentOwnerDPLink;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public String getCommentText() {
        return commentText;
    }

    public long getCommentLikes() {
        return commentLikes;
    }

    public long getCommentDislikes() {
        return commentDislikes;
    }

    public int getUserReaction() {
        return userReaction;
    }

    public void setUserReaction(int userReaction) {
        this.userReaction = userReaction;
    }
}
