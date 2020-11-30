package com.simpleharmonics.kismis.uis.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.VideoTileAdapter;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.VideoTileInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VideoPickerActivity extends AppCompatActivity implements VideoTileInterface {

    private static final String TAG = "VideoPickerActivityTAG";
    private VideoTileAdapter videoTileAdapter;
    private RecyclerView rvVideos;
    private ContentLoadingProgressBar pbProgress;
    private List<CustomVideo> customVideoList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.stay);
        setContentView(R.layout.activity_video_picker);

        final Toolbar toolbar = findViewById(R.id.ats);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "Kismis: onCreate: actionBar is null");
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
            actionBar.setTitle("Choose the Video to upload");
        }

        rvVideos = findViewById(R.id.yrw);
        rvVideos.setLayoutManager(new GridLayoutManager(VideoPickerActivity.this, 3));
        rvVideos.setHasFixedSize(true);
        pbProgress = findViewById(R.id.ghw);

        setEmptyAdapter();
        getAllVideos();
    }

    private void setEmptyAdapter() {
        customVideoList = new ArrayList<>();
        videoTileAdapter = new VideoTileAdapter(VideoPickerActivity.this, VideoPickerActivity.this, customVideoList);
        rvVideos.setAdapter(videoTileAdapter);
    }

    private void getAllVideos() {
        Handler handler = new Handler();
        new Thread(() -> {
            HashSet<String> videoHashSet = new HashSet<>();
            String[] projection = {MediaStore.Video.VideoColumns.DATA};
            String selection = MediaStore.Video.VideoColumns.DATA + " LIKE? ";
            String[] selectionArgs = {"%.mp4"};
            Cursor cursor = VideoPickerActivity.this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
            try {
                if (cursor == null) {
                    Log.e(TAG, "Kismis: getAllVideos: cursor is null");
                } else {
                    cursor.moveToFirst();
                    do {
                        String stringLink = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        videoHashSet.add(stringLink);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            customVideoList = new ArrayList<>();
            List<String> videoPathList = new ArrayList<>(videoHashSet);
            for (String videoPath : videoPathList) {
                CustomVideo customVideo = new CustomVideo(videoPath, videoPath);
                customVideoList.add(customVideo);
            }

            handler.post(() -> {
                pbProgress.hide();
                videoTileAdapter = new VideoTileAdapter(VideoPickerActivity.this, VideoPickerActivity.this, customVideoList);
                rvVideos.setAdapter(videoTileAdapter);
            });
        }).start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public void onVideoTileClicked(List<CustomVideo> customVideoList, int index) {
        CustomVideo customVideo = customVideoList.get(index);
        if (customVideo.getVideoLink() == null) {
            Log.e(TAG, "Kismis: onVideoTileClicked: videoLink is null");
        } else {
            Intent intent = new Intent(VideoPickerActivity.this, VideoApprovalActivity.class);
            intent.putExtra("VideoUriString", customVideo.getVideoLink());
            startActivity(intent);
        }
    }
}