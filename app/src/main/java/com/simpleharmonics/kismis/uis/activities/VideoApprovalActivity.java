package com.simpleharmonics.kismis.uis.activities;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.slider.RangeSlider;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.source.UriDataSource;
import com.otaliastudios.transcoder.strategy.DefaultAudioStrategy;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AspectRatioResizer;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.classes.CustomFirebase;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.interfaces.UploadVideoInterface;

import java.io.File;

public class VideoApprovalActivity extends AppCompatActivity implements UploadVideoInterface {

    private static final String TAG = "VideoApprovalActivity";

    //Video Specs
    private static final int VIDEO_BIT_RATE = 12000000;
    private static final int AUDIO_BIT_RATE = 12000000;
    private static final int FRAME_RATE = 60;
    private static final long VIDEO_MAX_DURATION = 15 * 1000;
    private SimpleExoPlayer simpleExoPlayer;
    private ImageView ivPlay;
    private CircularProgressIndicator piProgress;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_video_approval);

        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, "Kismis: onCreate: intent is null");
            CustomTools.showToast(VideoApprovalActivity.this, "Something went wrong");
        } else {
            String videoUriString = intent.getStringExtra("VideoUriString");
            if (videoUriString == null || videoUriString.isEmpty()) {
                Log.e(TAG, "Kismis: onCreate: videoUriString is null or empty");
                CustomTools.showToast(VideoApprovalActivity.this, "Something went wrong");
            } else {
                piProgress = findViewById(R.id.ufs);
                transcode(videoUriString);

                ImageButton btnBack = findViewById(R.id.oqe);
                btnBack.setOnClickListener(view -> VideoApprovalActivity.this.finish());
            }
        }
    }

    private void transcode(String videoUriString) {
        piProgress.show();
        File fleOutput = CustomTools.getVideoFileTranscoded(VideoApprovalActivity.this, videoUriString);
        if (fleOutput == null) {
            transcodeFailure();
        } else {
            DefaultAudioStrategy audioStrategy = DefaultAudioStrategy.builder()
                    .bitRate(AUDIO_BIT_RATE)
                    .build();

            DefaultVideoStrategy videoStrategy = new DefaultVideoStrategy.Builder()
                    .addResizer(new AspectRatioResizer(16F / 9F))
//                .addResizer(new FractionResizer(0.4F))
                    .addResizer(new AtMostResizer(1000))
                    .frameRate(FRAME_RATE)
                    .bitRate(VIDEO_BIT_RATE)
                    .build();

            com.otaliastudios.transcoder.source.DataSource dataSource = new UriDataSource(VideoApprovalActivity.this, Uri.parse(videoUriString));
//            com.otaliastudios.transcoder.source.DataSource clippedSource = new ClipDataSource(dataSource, 0, 15 * 1000 * 1000);
            Transcoder.into(fleOutput.getAbsolutePath())
                    .setAudioTrackStrategy(audioStrategy)
                    .setVideoTrackStrategy(videoStrategy)
                    .addDataSource(dataSource)
                    .setListener(new TranscoderListener() {
                        public void onTranscodeProgress(double progress) {
                            piProgress.setProgress((int) (progress * 10));
                        }

                        public void onTranscodeCompleted(int successCode) {
                            piProgress.hide();
                            transcodeSuccess(fleOutput);
                        }

                        public void onTranscodeCanceled() {
                            Log.e(TAG, "Kismis: transcode: onTranscodeCanceled: Cancelled");
                            transcodeFailure();
                        }

                        public void onTranscodeFailed(@NonNull Throwable exception) {
                            Log.e(TAG, "Kismis: transcode: onTranscodeFailed: " + exception);
                            transcodeFailure();
                        }
                    }).transcode();
        }
    }

    private void transcodeSuccess(File videoFile) {
        RangeSlider rsSlider = findViewById(R.id.bhq);
        rsSlider.setStepSize(0);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(VideoApprovalActivity.this, Uri.fromFile(videoFile));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long videoDurationMS = Long.parseLong(time);

        rsSlider.setValueFrom(0f);
        rsSlider.setValueTo((float) videoDurationMS);
        if (videoDurationMS > VIDEO_MAX_DURATION) {
            rsSlider.setValues(0f, (float) VIDEO_MAX_DURATION);
        } else {
            rsSlider.setValues(0f, (float) videoDurationMS);
        }

        retriever.release();

        PlayerView playerView = findViewById(R.id.ves);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(VideoApprovalActivity.this, new DefaultRenderersFactory(VideoApprovalActivity.this), new DefaultTrackSelector());
        playerView.setPlayer(simpleExoPlayer);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(VideoApprovalActivity.this, Util.getUserAgent(VideoApprovalActivity.this, "Kismis"));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(videoFile));
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ONE);
        simpleExoPlayer.setPlayWhenReady(true);

        RelativeLayout rlParent = findViewById(R.id.fpu);
        ivPlay = findViewById(R.id.ubz);
        rlParent.setOnClickListener(v -> {
            togglePlay(!simpleExoPlayer.getPlayWhenReady());
        });

        AppCompatEditText adtCaption = findViewById(R.id.pyk);
        Editable editable = adtCaption.getText();
        ImageButton btnNext = findViewById(R.id.xhj);
        btnNext.setOnClickListener(view -> {
            if (editable == null) {
                CustomFirebase.uploadVideo(VideoApprovalActivity.this, VideoApprovalActivity.this, videoFile, null);
            } else {
                CustomFirebase.uploadVideo(VideoApprovalActivity.this, VideoApprovalActivity.this, videoFile, String.valueOf(editable));
            }
        });
    }

    private void transcodeFailure() {
        VideoApprovalActivity.this.finish();
    }

    private void togglePlay(boolean play) {
        if (simpleExoPlayer == null || ivPlay == null) {
            Log.i(TAG, "Kismis: togglePlay: simpleExoPlayer or ivPlay is null");
        } else {
            simpleExoPlayer.setPlayWhenReady(play);
            if (play) {
                ivPlay.setVisibility(View.INVISIBLE);
            } else {
                ivPlay.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        togglePlay(false);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (simpleExoPlayer == null) {
            Log.i(TAG, "Kismis: onDestroy: simpleExoPlayer is null");
        } else {
            simpleExoPlayer.release();
        }
    }

    @Override
    public void onUploadVideoStart() {
        handler.post(() -> {

        });
    }

    @Override
    public void onUploadVideoComplete() {
        handler.post(() -> {

        });
    }

    @Override
    public void onUploadVideoError() {
        handler.post(() -> {

        });
    }
}