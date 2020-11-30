package com.simpleharmonics.kismis.classes;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.entities.CustomComment;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.CommentGetInterface;
import com.simpleharmonics.kismis.interfaces.CommentOperationInterface;
import com.simpleharmonics.kismis.interfaces.LoginInterface;
import com.simpleharmonics.kismis.interfaces.RefreshRecommendationsInterface;
import com.simpleharmonics.kismis.interfaces.UploadVideoInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CustomFirebase {

    private static final String TAG = "CustomFirebaseTAG";

    public static void checkUserAccount(@NonNull Activity activity, @NonNull LoginInterface listener, @Nullable String uniqueID) {
        listener.onLoginStart();
        DocumentReference userDetailsReference = CustomGlobal.getUserDetailsReference(activity);
        if (userDetailsReference == null) {
            Log.e(TAG, "Kismis: checkUserAccount: userDetailsReference is null");
            listener.onLoginError();
        } else {
            userDetailsReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot == null || !documentSnapshot.exists()) {
                    HashMap<String, Object> hashMapDetails = new HashMap<>();
                    hashMapDetails.put(activity.getString(R.string.firestore_field_authentication_provider), activity.getString(R.string.firestore_field_login_type_google));
                    hashMapDetails.put(activity.getString(R.string.firestore_field_time_stamp), FieldValue.serverTimestamp());
                    CustomGlobal.getUniqueUserReference(activity, uniqueID).set(hashMapDetails).addOnSuccessListener(aVoid1 -> {
                        String awsUsername = CustomGlobal.getUniqueID(activity);
                        if (awsUsername == null || awsUsername.isEmpty()) {
                            Log.e(TAG, "Kismis: checkUserAccount: awsUsername is null or empty");
                            listener.onLoginError();
                        } else {
                            String awsPassword = CustomTools.getRandomStringAlphaNumeric(32);
                            String userTAG = CustomTools.getRandomStringAlphaNumeric(5) + CustomTools.getRandomStringNumeric(5);
                            String userName = CustomTools.getRandomStringAlphaNumeric(5);

                            HashMap<String, Object> hashMapCredentials = new HashMap<>();
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_aws_username), awsUsername);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_aws_password), awsPassword);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_tag), userTAG);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_name), userName);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_total_likes), 0);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_followers), 0);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_dp_link), "");
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_reload_comments), false);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_reload_comment_reactions), false);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_reload_videos), false);
                            hashMapCredentials.put(activity.getString(R.string.firestore_field_reload_video_reactions), false);

                            CustomGlobal.getUserDetailsReference(activity, uniqueID).set(hashMapCredentials).addOnSuccessListener(aVoid2 -> {
                                Log.i(TAG, "Kismis: checkUserAccount: getUserDetailsReference: Progress");
                                CustomAWS.awsCreateProfile(activity, listener, awsUsername, awsPassword);
                            }).addOnFailureListener(e -> {
                                Log.e(TAG, "Kismis: checkUserAccount: addOnFailureListener: ", e);
                                listener.onLoginError();
                            });
                        }
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Kismis: checkUserAccount: addOnFailureListener: ", e);
                        listener.onLoginError();
                    });
                } else {
                    Object reloadCommentReactionsObject = documentSnapshot.get(activity.getString(R.string.firestore_field_reload_comment_reactions));
                    if ((boolean) reloadCommentReactionsObject) {
                        CustomCache.cacheUserCommentReactions(activity);
                    }
                    Object reloadCommentsObject = documentSnapshot.get(activity.getString(R.string.firestore_field_reload_comments));
                    if ((boolean) reloadCommentsObject) {
                        CustomCache.cacheUserComments(activity);
                    }
                    Object reloadVideoReactionsObject = documentSnapshot.get(activity.getString(R.string.firestore_field_reload_video_reactions));
                    if ((boolean) reloadVideoReactionsObject) {
                        CustomCache.cacheUserVideoReactions(activity);
                    }
                    Object reloadVideosObject = documentSnapshot.get(activity.getString(R.string.firestore_field_reload_videos));
                    if ((boolean) reloadVideosObject) {
                        CustomCache.cacheUserVideos(activity);
                    }
                    if (CustomAWS.userNotLoggedIn()) {
                        Object awsUsernameObject = documentSnapshot.get(activity.getString(R.string.firestore_field_aws_username));
                        String awsUsername = String.valueOf(awsUsernameObject);
                        Object awsPasswordObject = documentSnapshot.get(activity.getString(R.string.firestore_field_aws_password));
                        String awsPassword = String.valueOf(awsPasswordObject);
                        CustomAWS.awsLoginProfile(activity, listener, awsUsername, awsPassword);
                    } else {
                        listener.onLoginComplete();
                    }
                }
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Kismis: checkUserAccount: addOnFailureListener: " + e);
                listener.onLoginError();
            });
        }
    }

    public static void anonymousLogin(Activity activity, LoginInterface listener) {
        listener.onLoginStart();
        FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener(authResult -> {
            FirebaseUser firebaseUser = authResult.getUser();
            if (firebaseUser == null) {
                Log.e(TAG, "Kismis: anonymousLogin: addOnFailureListener: firebaseUser is null");
                CustomPreference.setUserSkippedLogin(activity, true);
                listener.onLoginError();
            } else {
                listener.onLoginComplete();
                Log.i(TAG, "Kismis: anonymousLogin: addOnCompleteListener: Login Progress");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Kismis: anonymousLogin: addOnFailureListener: Login failed: ", e);
            listener.onLoginError();
        });
    }

    public static void elevateLoginToPermanent(Activity activity, @NonNull AuthCredential authCredential) {
        FirebaseUser currentUser = CustomTools.getFirebaseUser();
        if (currentUser == null) {
            Log.e(TAG, "Kismis: elevateLogin: currentUser is null");
        } else {
            currentUser.linkWithCredential(authCredential).addOnCompleteListener(task -> {
                Log.i(TAG, "Kismis: anonymousLogin: addOnCompleteListener: Login successful");
//                checkUserAccount();
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Kismis: anonymousLogin: addOnFailureListener: Login failed: ", e);
            });
        }
    }

    public static void getCommentList(Activity activity, CommentGetInterface listener, DocumentSnapshot dsLastComment, String videoID) {
        new Thread(() -> {
            listener.onCommentGetStart();
            if (dsLastComment == null) {
                CustomGlobal.getCommentListReference(activity, videoID).limit(20).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    continueCommentList(activity, listener, queryDocumentSnapshots);
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Kismis: getCommentList: addOnFailureListener: ", e);
                    listener.onCommentGetError();
                });
            } else {
                CustomGlobal.getCommentListReference(activity, videoID).startAfter(dsLastComment).limit(20).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    continueCommentList(activity, listener, queryDocumentSnapshots);
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Kismis: getCommentList: addOnFailureListener: ", e);
                    listener.onCommentGetError();
                });
            }
        });
    }

    private static void continueCommentList(Activity activity, CommentGetInterface listener, QuerySnapshot queryDocumentSnapshots) {
        if (queryDocumentSnapshots == null) {
            Log.e(TAG, "Kismis: getCommentList: queryDocumentSnapshots is null");
            listener.onCommentGetError();
        } else {
            List<CustomComment> customCommentList = new ArrayList<>();
            DocumentSnapshot dsNewLastComment = null;
            for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);

                String commentID = documentSnapshot.getId();
                String commentOwnerID = String.valueOf(documentSnapshot.get(activity.getString(R.string.firestore_field_owner_id)));
                String commentOwnerTAG = String.valueOf(documentSnapshot.get(activity.getString(R.string.firestore_field_tag)));
                String commentOwnerDPLink = String.valueOf(documentSnapshot.get(activity.getString(R.string.firestore_field_dp_link)));
                String commentTime = String.valueOf(documentSnapshot.get(activity.getString(R.string.firestore_field_time_stamp)));
                String commentText = String.valueOf(documentSnapshot.get(activity.getString(R.string.firestore_field_comment_text)));
                long commentLikes = Long.parseLong(String.valueOf(documentSnapshot.get(activity.getString(R.string.firestore_field_comment_likes))));
                long commentDislikes = Long.parseLong(String.valueOf(documentSnapshot.get(activity.getString(R.string.firestore_field_comment_dislikes))));
                int userReactionStatus = CustomCache.getUserCommentReaction(activity, commentID);

                CustomComment customComment = new CustomComment(commentID, commentOwnerID, commentOwnerTAG, commentOwnerDPLink, commentTime, commentText, commentLikes, commentDislikes, userReactionStatus);
                customCommentList.add(customComment);
                if (i == queryDocumentSnapshots.size() - 1) {
                    dsNewLastComment = documentSnapshot;
                }
            }
            listener.onCommentGetComplete(customCommentList, dsNewLastComment);
        }
    }

    public static void addComment(Activity activity, String videoID, CustomComment customComment, CommentOperationInterface listener) {
        new Thread(() -> {
            listener.onCommentOperationStart();
            CustomGlobal.getCommentListReference(activity, videoID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        });
    }

    public static void editComment(Activity activity, CommentOperationInterface listener) {
        new Thread(() -> {
            listener.onCommentOperationStart();
        });
    }

    public static void deleteComment(Activity activity, CommentOperationInterface listener) {
        new Thread(() -> {
            listener.onCommentOperationStart();
        });
    }

    public static void uploadVideo(Activity activity, UploadVideoInterface listener, File videoFile, String caption) {
        new Thread(() -> {
            listener.onUploadVideoStart();
            String uniqueID = CustomGlobal.getUniqueID(activity);
            if (uniqueID == null || uniqueID.isEmpty()) {
                Log.e(TAG, "Kismis: uploadVideo: uniqueID is null or empty");
                listener.onUploadVideoError();
            } else {
                CustomVideo customVideo = new CustomVideo("", "");//TODO fill
                HashMap<String, Object> hashMapVideoData = new HashMap<>();
                hashMapVideoData.put(activity.getString(R.string.firestore_field_video_link), customVideo.getVideoLink());
                hashMapVideoData.put(activity.getString(R.string.firestore_field_thumbnail_link), customVideo.getVideoThumbnailLink());
                hashMapVideoData.put(activity.getString(R.string.firestore_field_owner_id), uniqueID);
                hashMapVideoData.put(activity.getString(R.string.firestore_field_time_stamp), FieldValue.serverTimestamp());

                CustomGlobal.getVideoListReference(activity).add(hashMapVideoData).addOnSuccessListener(documentReference -> {
                    customVideo.setUploadTimeStamp(new Date().toString());//TODO check
                    customVideo.setVideoID(documentReference.getId());
                    CustomCache.addToUserVideos(activity, customVideo);
                    listener.onUploadVideoComplete();
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Kismis: uploadVideo: onFailure: upload failed: ", e);
                    listener.onUploadVideoError();
                });
            }
        });
    }

    //TODO change dummy data
    public static void refreshRecommendations(Activity activity, RefreshRecommendationsInterface listener) {
        new Thread(() -> {
            listener.onRefreshRecommendationsStart();

            List<CustomVideo> customVideoList = new ArrayList<>();

            final String videoID = "";
            final String videoThumbnailLink = "";
            final String videoLink = "";
            final long videoLikes = 28374;
            final long videoDislikes = 83979;
            final long videoComments = 113979;
            final String videoCaption = "#cringe #boring";
            final String videoMusic = "song name here";
            final String ownerID = "";
            final String ownerTAG = "@wall_e";
            final String ownerDPLink = "";
            final int userLikeStatus = 1;

            CustomVideo customVideo1 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
            customVideoList.add(customVideo1);

            CustomVideo customVideo2 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
            customVideoList.add(customVideo2);

            CustomVideo customVideo3 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
            customVideoList.add(customVideo3);

            CustomVideo customVideo4 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
            customVideoList.add(customVideo4);

            CustomVideo customVideo5 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
            customVideoList.add(customVideo5);

            CustomVideo customVideo6 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
            customVideoList.add(customVideo6);

            CustomVideo customVideo7 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
            customVideoList.add(customVideo7);

            CustomVideo customVideo8 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
            customVideoList.add(customVideo8);

            listener.onRefreshRecommendationsComplete(customVideoList);
        }).start();
    }
}
