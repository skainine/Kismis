package com.simpleharmonics.kismis.uis.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.VideoTileAdapter;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.VideoTileInterface;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity implements VideoTileInterface {

    private static final String TAG = "BookmarkActivityTAG";
    private final List<CustomVideo> customVideoList = new ArrayList<>();
    private RecyclerView rvVideoTiles;
    private VideoTileAdapter videoTileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Toolbar toolbar = findViewById(R.id.ywn);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "Kismis: onCreate: actionBar is null");
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        rvVideoTiles = findViewById(R.id.dvi);
        rvVideoTiles.setLayoutManager(new GridLayoutManager(BookmarkActivity.this, 3));
        rvVideoTiles.setHasFixedSize(true);

        videoTileAdapter = new VideoTileAdapter(BookmarkActivity.this, BookmarkActivity.this, customVideoList);
        rvVideoTiles.setAdapter(videoTileAdapter);

        loadVideos();
    }

    //TODO change dummy data
    private void loadVideos() {
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

        videoTileAdapter = new VideoTileAdapter(BookmarkActivity.this, BookmarkActivity.this, customVideoList);
        rvVideoTiles.setAdapter(videoTileAdapter);
    }

    @Override
    public void onVideoTileClicked(List<CustomVideo> customVideoList, int index) {

    }
}