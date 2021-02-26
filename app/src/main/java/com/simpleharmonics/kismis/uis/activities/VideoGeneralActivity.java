package com.simpleharmonics.kismis.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.MainPlayerAdapter;
import com.simpleharmonics.kismis.classes.BounceAnimationInterpolator;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.VideoInteractionInterface;
import com.simpleharmonics.kismis.uis.fragments.BottomSheetComments;
import com.simpleharmonics.kismis.views.SnappingExoRecycler;

import java.util.ArrayList;
import java.util.List;

public class VideoGeneralActivity extends AppCompatActivity implements VideoInteractionInterface {

    private static final String TAG = "VideoGeneralActivity";
    //Controls
    public ImageView ivPlay;
    public List<CustomVideo> globalCustomVideoList = new ArrayList<>();
    private ImageButton ibLike, ibDislike, ibComments, ibShare;
    private TextView tvLikes, tvDislikes, tvComments, tvOwnerTAG, tvCaption, tvMusic;
    private ImageView ivOwnerProfile;
    private SnappingExoRecycler snappingExoRecycler;
    private MainPlayerAdapter mainPlayerAdapter;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_player);

        snappingExoRecycler = findViewById(R.id.thf);
        snappingExoRecycler.setHasFixedSize(true);

        snappingExoRecycler.initializePlayList(VideoGeneralActivity.this, globalCustomVideoList);
        mainPlayerAdapter = new MainPlayerAdapter(globalCustomVideoList);
        snappingExoRecycler.setAdapter(mainPlayerAdapter);

        ImageButton ibBAck = findViewById(R.id.ntb);
        ibBAck.setOnClickListener(v -> VideoGeneralActivity.this.finish());

        ivPlay = findViewById(R.id.apd);

        LinearLayout llLikes = findViewById(R.id.kuq);
        ibLike = findViewById(R.id.sgu);
        tvLikes = findViewById(R.id.pnv);
        llLikes.setOnClickListener(view -> {
            CustomVideo customVideo = globalCustomVideoList.get(currentIndex);
            Animation bounceAnimation = AnimationUtils.loadAnimation(VideoGeneralActivity.this, R.anim.bounce);
            bounceAnimation.setInterpolator(new BounceAnimationInterpolator(0.2, 20));
            ibLike.startAnimation(bounceAnimation);
            if (customVideo.getUserLikeStatus() == 1) {
                customVideo.setUserLikeStatus(0);
            } else {
                customVideo.setUserLikeStatus(1);
            }
            updateLikeDislikeStatus(customVideo);
        });

        LinearLayout llDislikes = findViewById(R.id.fmd);
        ibDislike = findViewById(R.id.gjc);
        tvDislikes = findViewById(R.id.rir);
        llDislikes.setOnClickListener(view -> {
            CustomVideo customVideo = globalCustomVideoList.get(currentIndex);
            Animation bounceAnimation = AnimationUtils.loadAnimation(VideoGeneralActivity.this, R.anim.bounce);
            bounceAnimation.setInterpolator(new BounceAnimationInterpolator(0.2, 20));
            ibDislike.startAnimation(bounceAnimation);
            if (customVideo.getUserLikeStatus() == -1) {
                customVideo.setUserLikeStatus(0);
            } else {
                customVideo.setUserLikeStatus(-1);
            }
            updateLikeDislikeStatus(customVideo);
        });

        LinearLayout llComments = findViewById(R.id.sfd);
        ibComments = findViewById(R.id.wsg);
        tvComments = findViewById(R.id.rmn);
        llComments.setOnClickListener(v -> {
            Animation bounceAnimation = AnimationUtils.loadAnimation(VideoGeneralActivity.this, R.anim.bounce);
            bounceAnimation.setInterpolator(new BounceAnimationInterpolator(0.2, 20));
            ibComments.startAnimation(bounceAnimation);
            if (currentIndex == -1 || globalCustomVideoList.size() < 1 || currentIndex >= globalCustomVideoList.size()) {
                Log.e(TAG, "Kismis: onCreate: index out of bounds");
                CustomTools.showToast(VideoGeneralActivity.this, "Something does not seem right");
            } else {
                BottomSheetComments bottomSheetComments = BottomSheetComments.newInstance(globalCustomVideoList.get(currentIndex).getVideoID());
                bottomSheetComments.show(VideoGeneralActivity.this.getSupportFragmentManager(), "Comment Fragment");
            }
        });

        ibShare = findViewById(R.id.org);
        ibShare.setOnClickListener(v -> {
            Animation bounceAnimation = AnimationUtils.loadAnimation(VideoGeneralActivity.this, R.anim.bounce);
            bounceAnimation.setInterpolator(new BounceAnimationInterpolator(0.2, 20));
            ibShare.startAnimation(bounceAnimation);
        });

        tvOwnerTAG = findViewById(R.id.kaw);
        tvCaption = findViewById(R.id.kov);
        tvMusic = findViewById(R.id.ajy);

        ivOwnerProfile = findViewById(R.id.iez);
        ivOwnerProfile.setOnClickListener(v -> startActivity(new Intent(VideoGeneralActivity.this, ForeignProfileActivity.class)));

        globalCustomVideoList = new ArrayList<>();
        snappingExoRecycler.initializePlayList(VideoGeneralActivity.this, globalCustomVideoList);
        mainPlayerAdapter = new MainPlayerAdapter(globalCustomVideoList);
        snappingExoRecycler.setAdapter(mainPlayerAdapter);

        new Handler().postDelayed(this::getVideoList, 200);
    }

    //TODO change dummy data
    private void getVideoList() {
        final String videoID = "";
        final String videoThumbnailLink = "";
        final String videoLink = "";
        final long videoLikes = 28374;
        final long videoDislikes = 83979;
        final long videoComments = 113979;
        final String videoCaption = "#lorem #ipsum";
        final String videoMusic = "song name here";
        final String ownerID = "";
        final String ownerTAG = "@lorem_ipsum";
        final String ownerDPLink = "";
        final int userLikeStatus = 1;

        CustomVideo customVideo1 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
        globalCustomVideoList.add(customVideo1);

        CustomVideo customVideo2 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
        globalCustomVideoList.add(customVideo2);

        CustomVideo customVideo3 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
        globalCustomVideoList.add(customVideo3);

        CustomVideo customVideo4 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
        globalCustomVideoList.add(customVideo4);

        CustomVideo customVideo5 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
        globalCustomVideoList.add(customVideo5);

        CustomVideo customVideo6 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
        globalCustomVideoList.add(customVideo6);

        CustomVideo customVideo7 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
        globalCustomVideoList.add(customVideo7);

        CustomVideo customVideo8 = new CustomVideo(videoID, "https://source.unsplash.com/random/320x480", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
        globalCustomVideoList.add(customVideo8);

        snappingExoRecycler.initializePlayList(VideoGeneralActivity.this, globalCustomVideoList);
        mainPlayerAdapter = new MainPlayerAdapter(globalCustomVideoList);
        snappingExoRecycler.setAdapter(mainPlayerAdapter);

        new Handler(Looper.getMainLooper()).post(() -> snappingExoRecycler.playVideo());
    }

    private void updateLikeDislikeStatus(CustomVideo customVideo) {
        if (customVideo.getUserLikeStatus() == -1) {
            ibLike.setImageDrawable(ContextCompat.getDrawable(VideoGeneralActivity.this, R.drawable.ic_like_white_24));
            ibDislike.setImageDrawable(ContextCompat.getDrawable(VideoGeneralActivity.this, R.drawable.ic_dislike_primary_24));
        } else if (customVideo.getUserLikeStatus() == 1) {
            ibLike.setImageDrawable(ContextCompat.getDrawable(VideoGeneralActivity.this, R.drawable.ic_like_primary_24));
            ibDislike.setImageDrawable(ContextCompat.getDrawable(VideoGeneralActivity.this, R.drawable.ic_dislike_white_24));
        } else {
            ibLike.setImageDrawable(ContextCompat.getDrawable(VideoGeneralActivity.this, R.drawable.ic_like_white_24));
            ibDislike.setImageDrawable(ContextCompat.getDrawable(VideoGeneralActivity.this, R.drawable.ic_dislike_white_24));
        }
    }

    private void updateUi(CustomVideo customVideo) {
        updateLikeDislikeStatus(customVideo);
        tvLikes.setText(CustomTools.getReducedNumber(customVideo.getVideoLikes()));
        tvDislikes.setText(CustomTools.getReducedNumber(customVideo.getVideoDislikes()));
        tvComments.setText(CustomTools.getReducedNumber(customVideo.getVideoComments()));
        tvOwnerTAG.setText(customVideo.getOwnerTAG());
        tvCaption.setText(customVideo.getVideoCaption());
        tvMusic.setText(customVideo.getVideoMusic());

        if (customVideo.getOwnerDPLink() == null || customVideo.getOwnerDPLink().isEmpty()) {
            Glide.with(VideoGeneralActivity.this).load(ContextCompat.getDrawable(VideoGeneralActivity.this, R.drawable.avatar)).into(ivOwnerProfile);
        } else {
            Glide.with(VideoGeneralActivity.this).load(customVideo.getOwnerDPLink()).into(ivOwnerProfile);
        }
    }

    @Override
    public void onPause() {
        snappingExoRecycler.pausePlayer();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (snappingExoRecycler == null) {
            Log.e(TAG, "Kismis: onDestroy: exoPlayerRecyclerView is null");
        } else {
            snappingExoRecycler.releasePlayer();
        }
        super.onDestroy();
    }

    @Override
    public void onVideoChanged(int currentIndex) {
        this.currentIndex = currentIndex;
        updateUi(globalCustomVideoList.get(currentIndex));
    }

    @Override
    public void onTogglePlayTapped(boolean isPaused) {
        if (isPaused) {
            ivPlay.setVisibility(View.VISIBLE);
        } else {
            ivPlay.setVisibility(View.GONE);
        }
    }
}