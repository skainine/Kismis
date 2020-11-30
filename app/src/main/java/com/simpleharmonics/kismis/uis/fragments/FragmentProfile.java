package com.simpleharmonics.kismis.uis.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.VideoTileAdapter;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.VideoTileInterface;
import com.simpleharmonics.kismis.uis.activities.SettingsActivity;
import com.simpleharmonics.kismis.uis.activities.VideoGeneralActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfile extends Fragment implements VideoTileInterface {

    private static final String TAG = "FragmentHomeTAG";

    private RecyclerView rvVideoTiles;
    private VideoTileAdapter videoTileAdapter;
    private ContentLoadingProgressBar pbProgress;
    private final List<CustomVideo> customVideoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_profile, container, false);

        FragmentActivity fragmentActivity = FragmentProfile.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onCreateView: fragmentActivity is null");
        } else {
            final Toolbar toolbar = fragment.findViewById(R.id.fej);
            ((AppCompatActivity) fragmentActivity).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) fragmentActivity).getSupportActionBar();
            if (actionBar == null) {
                Log.e(TAG, "Kismis: onCreate: actionBar is null");
            } else {
                actionBar.setTitle("My Profile");
            }

            setHasOptionsMenu(true);

            NestedScrollView nestedScrollView = fragment.findViewById(R.id.pzi);
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY > 0 && scrollY < 700) {
                    float alpha = (scrollY / 700f);
                    int resultColor = ColorUtils.blendARGB(getResources().getColor(android.R.color.transparent, null), getResources().getColor(android.R.color.black, null), alpha);
                    toolbar.setBackgroundColor(resultColor);
                } else if (scrollY < 50 && oldScrollY > scrollY) {
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent, null));
                }
            });

            rvVideoTiles = fragment.findViewById(R.id.cvh);
            rvVideoTiles.setLayoutManager(new GridLayoutManager(fragmentActivity, 3));
            rvVideoTiles.setHasFixedSize(true);
            pbProgress = fragment.findViewById(R.id.dne);

            videoTileAdapter = new VideoTileAdapter(fragmentActivity, FragmentProfile.this, customVideoList);
            rvVideoTiles.setAdapter(videoTileAdapter);

            new Handler().postDelayed(this::loadVideos, 200);
        }
        return fragment;
    }

    //TODO change dummy data
    private void loadVideos() {
        final FragmentActivity fragmentActivity = FragmentProfile.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: loadVideos: fragmentActivity is null");
        } else {
            pbProgress.hide();

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

            for (int i = 1; i < 3; i++) {
                CustomVideo customVideo1 = new CustomVideo(videoID, "https://source.unsplash.com/random/144x256", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
                customVideoList.add(customVideo1);

                CustomVideo customVideo2 = new CustomVideo(videoID, "https://source.unsplash.com/random/144x256", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
                customVideoList.add(customVideo2);

                CustomVideo customVideo3 = new CustomVideo(videoID, "https://source.unsplash.com/random/144x256", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
                customVideoList.add(customVideo3);

                CustomVideo customVideo4 = new CustomVideo(videoID, "https://source.unsplash.com/random/144x256", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
                customVideoList.add(customVideo4);

                CustomVideo customVideo5 = new CustomVideo(videoID, "https://source.unsplash.com/random/144x256", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
                customVideoList.add(customVideo5);

                CustomVideo customVideo6 = new CustomVideo(videoID, "https://source.unsplash.com/random/144x256", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
                customVideoList.add(customVideo6);

                CustomVideo customVideo7 = new CustomVideo(videoID, "https://source.unsplash.com/random/144x256", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
                customVideoList.add(customVideo7);

                CustomVideo customVideo8 = new CustomVideo(videoID, "https://source.unsplash.com/random/144x256", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4", videoLikes, videoDislikes, videoComments, videoCaption, videoMusic, ownerID, ownerTAG, ownerDPLink, userLikeStatus);
                customVideoList.add(customVideo8);
            }

            videoTileAdapter = new VideoTileAdapter(fragmentActivity, FragmentProfile.this, customVideoList);
            rvVideoTiles.setAdapter(videoTileAdapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        FragmentActivity fragmentActivity = FragmentProfile.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onCreateOptionsMenu: fragmentActivity is null");
        } else {
            fragmentActivity.getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentActivity fragmentActivity = FragmentProfile.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onOptionsItemSelected: fragmentActivity is null");
        } else {
            if (item.getItemId() == R.id.mzh) {
                fragmentActivity.startActivity(new Intent(fragmentActivity, SettingsActivity.class));
            } else {
                Log.e(TAG, "Kismis: onOptionsItemSelected: Unknown item id: " + item.getItemId());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVideoTileClicked(List<CustomVideo> customVideoList, int index) {
        FragmentActivity fragmentActivity = FragmentProfile.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onVideoTileClicked: fragmentActivity is null");
        } else {
            Intent intent = new Intent(fragmentActivity, VideoGeneralActivity.class);
            startActivity(intent);
        }
    }
}
