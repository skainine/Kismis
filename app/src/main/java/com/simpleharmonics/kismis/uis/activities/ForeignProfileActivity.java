package com.simpleharmonics.kismis.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.VideoTileAdapter;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.VideoTileInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Expects intent with String extra ForeignID (Id of the user in demand)
 **/
public class ForeignProfileActivity extends AppCompatActivity implements VideoTileInterface {

    private static final String TAG = "ForeignProfileActivity";
    private RecyclerView rvVideoTiles;
    private VideoTileAdapter videoTileAdapter;
    private ContentLoadingProgressBar pbProgress;
    private final List<CustomVideo> customVideoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_profile);

        Toolbar toolbar = findViewById(R.id.qbj);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "Kismis: onCreate: actionBar is null");
        } else {
            actionBar.setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back_white_24));
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
            actionBar.setTitle("Alien Walker's Profile");
        }

        Intent intent = getIntent();
        intent.putExtra("ForeignID", "Simple");//TODO remove this
        if (intent == null) {
            Log.e(TAG, "Kismis: onCreate: intent is null");
            CustomTools.showToast(ForeignProfileActivity.this, "Something is not right");
            ForeignProfileActivity.this.finish();
        } else {
            if (intent.hasExtra("ForeignID")) {
                String foreignID = intent.getStringExtra("ForeignID");
                if (foreignID == null || foreignID.isEmpty()) {
                    Log.e(TAG, "Kismis: onCreate: foreignID is null or empty");
                    CustomTools.showToast(ForeignProfileActivity.this, "Something is not right");
                    ForeignProfileActivity.this.finish();
                } else {
                    MaterialButton mbtMessage = findViewById(R.id.ytp);
                    mbtMessage.setOnClickListener(view -> startActivity(new Intent(ForeignProfileActivity.this, MessageActivity.class)));

                    rvVideoTiles = findViewById(R.id.suv);
                    rvVideoTiles.setLayoutManager(new GridLayoutManager(ForeignProfileActivity.this, 3));
                    rvVideoTiles.setHasFixedSize(true);
                    pbProgress = findViewById(R.id.utx);

                    NestedScrollView nestedScrollView = findViewById(R.id.tdy);
                    nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        if (scrollY > 0 && scrollY < 700) {
                            float alpha = (scrollY / 700f);
                            int resultColorToolbar = ColorUtils.blendARGB(getResources().getColor(android.R.color.transparent, null), getResources().getColor(android.R.color.black, null), alpha);
                            toolbar.setBackgroundColor(resultColorToolbar);
                        } else if (scrollY < 50 && oldScrollY > scrollY) {
                            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent, null));
                        }
                    });

                    videoTileAdapter = new VideoTileAdapter(ForeignProfileActivity.this, ForeignProfileActivity.this, customVideoList);
                    rvVideoTiles.setAdapter(videoTileAdapter);

                    new Handler().postDelayed(() -> loadVideos(foreignID), 200);
                }
            } else {
                Log.e(TAG, "Kismis: onCreate: intent does not contain ForeignID");
                CustomTools.showToast(ForeignProfileActivity.this, "Something is not right");
                ForeignProfileActivity.this.finish();
            }
        }
    }

    //TODO change dummy data
    private void loadVideos(String foreignID) {
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

        videoTileAdapter = new VideoTileAdapter(ForeignProfileActivity.this, ForeignProfileActivity.this, customVideoList);
        rvVideoTiles.setAdapter(videoTileAdapter);
    }


    @Override
    public void onVideoTileClicked(List<CustomVideo> customVideoList, int index) {
        Intent intent = new Intent(ForeignProfileActivity.this, VideoGeneralActivity.class);
        startActivity(intent);
    }
}