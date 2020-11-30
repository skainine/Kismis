package com.simpleharmonics.kismis.uis.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.MainPlayerAdapter;
import com.simpleharmonics.kismis.classes.BounceAnimationInterpolator;
import com.simpleharmonics.kismis.classes.CustomFirebase;
import com.simpleharmonics.kismis.classes.CustomPreference;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.RefreshRecommendationsInterface;
import com.simpleharmonics.kismis.interfaces.VideoInteractionInterface;
import com.simpleharmonics.kismis.uis.activities.ForeignProfileActivity;
import com.simpleharmonics.kismis.views.SnappingExoRecycler;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment implements VideoInteractionInterface, RefreshRecommendationsInterface {

    private static final String TAG = "FragmentHomeTAG";

    //Controls
    public ImageView ivPlay;
    public List<CustomVideo> globalCustomVideoList = new ArrayList<>();
    private ImageButton ibLike, ibDislike, ibComments, ibShare;
    private TextView tvLikes, tvDislikes, tvComments, tvOwnerTAG, tvCaption, tvMusic;
    private ImageView ivOwnerProfile;
    private SnappingExoRecycler snappingExoRecycler;
    private MainPlayerAdapter mainPlayerAdapter;

    private int currentIndex = -1;

    private final Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_home, container, false);

        FragmentActivity fragmentActivity = FragmentHome.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onCreateView: activity is null");
        } else {
            snappingExoRecycler = fragment.findViewById(R.id.cva);
            snappingExoRecycler.setHasFixedSize(true);

            snappingExoRecycler.initializePlayList(FragmentHome.this, globalCustomVideoList);
            mainPlayerAdapter = new MainPlayerAdapter(globalCustomVideoList);
            snappingExoRecycler.setAdapter(mainPlayerAdapter);

            ivPlay = fragment.findViewById(R.id.ize);

            LinearLayout llLikes = fragment.findViewById(R.id.jwz);
            ibLike = fragment.findViewById(R.id.wyb);
            tvLikes = fragment.findViewById(R.id.dat);
            llLikes.setOnClickListener(view -> {
                CustomVideo customVideo = globalCustomVideoList.get(currentIndex);
                Animation bounceAnimation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.bounce);
                bounceAnimation.setInterpolator(new BounceAnimationInterpolator(0.2, 20));
                ibLike.startAnimation(bounceAnimation);
                if (customVideo.getUserLikeStatus() == 1) {
                    customVideo.setUserLikeStatus(0);
                } else {
                    customVideo.setUserLikeStatus(1);
                }
                updateLikeDislikeStatus(fragmentActivity, customVideo);
            });

            LinearLayout llDislikes = fragment.findViewById(R.id.kfp);
            ibDislike = fragment.findViewById(R.id.ldf);
            tvDislikes = fragment.findViewById(R.id.apx);
            llDislikes.setOnClickListener(view -> {
                CustomVideo customVideo = globalCustomVideoList.get(currentIndex);
                Animation bounceAnimation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.bounce);
                bounceAnimation.setInterpolator(new BounceAnimationInterpolator(0.2, 20));
                ibDislike.startAnimation(bounceAnimation);
                if (customVideo.getUserLikeStatus() == -1) {
                    customVideo.setUserLikeStatus(0);
                } else {
                    customVideo.setUserLikeStatus(-1);
                }
                updateLikeDislikeStatus(fragmentActivity, customVideo);
            });

            LinearLayout llComments = fragment.findViewById(R.id.hss);
            ibComments = fragment.findViewById(R.id.yyj);
            tvComments = fragment.findViewById(R.id.myz);
            llComments.setOnClickListener(v -> {
                Animation bounceAnimation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.bounce);
                bounceAnimation.setInterpolator(new BounceAnimationInterpolator(0.2, 20));
                ibComments.startAnimation(bounceAnimation);
                showComments();
            });

            ibShare = fragment.findViewById(R.id.ika);
            ibShare.setOnClickListener(v -> {
                Animation bounceAnimation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.bounce);
                bounceAnimation.setInterpolator(new BounceAnimationInterpolator(0.2, 20));
                ibShare.startAnimation(bounceAnimation);
            });

            tvOwnerTAG = fragment.findViewById(R.id.gvt);
            tvCaption = fragment.findViewById(R.id.gjy);
            tvMusic = fragment.findViewById(R.id.sjd);

            ivOwnerProfile = fragment.findViewById(R.id.qdr);
            ivOwnerProfile.setOnClickListener(v -> {
                if (globalCustomVideoList.get(currentIndex).getOwnerID().equals(CustomPreference.getUserID(fragmentActivity))) {
                    Log.i(TAG, "Kismis: onCreateView: User's own video");
                    CustomTools.showToast(fragmentActivity, "It's You!");
                } else {
                    Intent intent = new Intent(fragmentActivity, ForeignProfileActivity.class);
                    intent.putExtra("ForeignID", globalCustomVideoList.get(currentIndex).getOwnerID());
                    startActivity(intent);
                }
            });
            new Handler().postDelayed(this::getRecommendations, 200);
        }
        return fragment;
    }

    private void showComments() {
        FragmentActivity fragmentActivity = FragmentHome.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: showComments: fragmentActivity is null");
        } else {
            if (currentIndex == -1 || globalCustomVideoList.size() < 1 || currentIndex >= globalCustomVideoList.size()) {
                Log.e(TAG, "Kismis: showComments: index out of bounds");
                CustomTools.showToast(fragmentActivity, "Something does not seem right");
            } else {
                BottomSheetComments bottomSheetComments = BottomSheetComments.newInstance(globalCustomVideoList.get(currentIndex).getVideoID());
                bottomSheetComments.show(fragmentActivity.getSupportFragmentManager(), "Comment Fragment");
            }
        }
    }

    private void getRecommendations() {
        FragmentActivity fragmentActivity = FragmentHome.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: getRecommendations: activity is null");
        } else {
            CustomFirebase.refreshRecommendations(fragmentActivity, FragmentHome.this);
        }
    }

    private void updateUi(CustomVideo customVideo) {
        FragmentActivity fragmentActivity = FragmentHome.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: updateUi: fragmentActivity is null");
        } else {
            updateLikeDislikeStatus(fragmentActivity, customVideo);
            tvLikes.setText(CustomTools.getReducedNumber(customVideo.getVideoLikes()));
            tvDislikes.setText(CustomTools.getReducedNumber(customVideo.getVideoDislikes()));
            tvComments.setText(CustomTools.getReducedNumber(customVideo.getVideoComments()));
            tvOwnerTAG.setText(customVideo.getOwnerTAG());
            tvCaption.setText(customVideo.getVideoCaption());
            tvMusic.setText(customVideo.getVideoMusic());

            if (customVideo.getOwnerDPLink() == null || customVideo.getOwnerDPLink().isEmpty()) {
                Glide.with(fragmentActivity).load(fragmentActivity.getDrawable(R.drawable.avatar)).into(ivOwnerProfile);
            } else {
                Glide.with(fragmentActivity).load(customVideo.getOwnerDPLink()).into(ivOwnerProfile);
            }
        }
    }


    private void updateLikeDislikeStatus(FragmentActivity fragmentActivity, CustomVideo customVideo) {
        if (customVideo.getUserLikeStatus() == -1) {
            ibLike.setImageDrawable(fragmentActivity.getDrawable(R.drawable.ic_like_white_24));
            ibDislike.setImageDrawable(fragmentActivity.getDrawable(R.drawable.ic_dislike_primary_24));
        } else if (customVideo.getUserLikeStatus() == 1) {
            ibLike.setImageDrawable(fragmentActivity.getDrawable(R.drawable.ic_like_primary_24));
            ibDislike.setImageDrawable(fragmentActivity.getDrawable(R.drawable.ic_dislike_white_24));
        } else {
            ibLike.setImageDrawable(fragmentActivity.getDrawable(R.drawable.ic_like_white_24));
            ibDislike.setImageDrawable(fragmentActivity.getDrawable(R.drawable.ic_dislike_white_24));
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
        if (currentIndex == -1 || globalCustomVideoList.size() < 1) {
            Log.i(TAG, "Kismis: onVideoChanged: Empty List");
        } else {
            updateUi(globalCustomVideoList.get(currentIndex));
        }
    }

    @Override
    public void onTogglePlayTapped(boolean isPaused) {
        if (isPaused) {
            ivPlay.setVisibility(View.VISIBLE);
        } else {
            ivPlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefreshRecommendationsStart() {
        handler.post(() -> {
            Log.i(TAG, "Kismis: onRefreshRecommendationsStart: Refreshing Recommendations");
        });
    }

    @Override
    public void onRefreshRecommendationsComplete(List<CustomVideo> customVideoList) {
        handler.post(() -> {
            globalCustomVideoList = customVideoList;
            snappingExoRecycler.initializePlayList(FragmentHome.this, globalCustomVideoList);
            mainPlayerAdapter = new MainPlayerAdapter(globalCustomVideoList);
            snappingExoRecycler.setAdapter(mainPlayerAdapter);

            new Handler(Looper.getMainLooper()).post(() -> snappingExoRecycler.playVideo());
        });
    }

    @Override
    public void onRefreshRecommendationsAlreadyUpdated() {
        handler.post(() -> {
            FragmentActivity fragmentActivity = FragmentHome.this.getActivity();
            if (fragmentActivity == null) {
                Log.e(TAG, "Kismis: onRefreshRecommendationsAlreadyUpdated: fragmentActivity is null");
            } else {
                CustomTools.showToast(fragmentActivity, "Already Updated!");
            }
        });
    }

    @Override
    public void onRefreshRecommendationsError() {
        handler.post(() -> {
            FragmentActivity fragmentActivity = FragmentHome.this.getActivity();
            if (fragmentActivity == null) {
                Log.e(TAG, "Kismis: onRefreshRecommendationsAlreadyUpdated: fragmentActivity is null");
            } else {
                CustomTools.showToast(fragmentActivity, "Already Updated!");
            }
        });
    }
}
