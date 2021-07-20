package com.simpleharmonics.kismis.views;

import android.content.Context;
import android.graphics.Outline;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
//import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.util.Util;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.MainPlayerAdapter;
import com.simpleharmonics.kismis.classes.CustomGlobal;
import com.simpleharmonics.kismis.classes.CustomVideoCache;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.VideoInteractionInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SnappingExoRecycler extends RecyclerView {

    private static final String TAG = "ExoPlayerRecyclerView";

    private List<CustomVideo> customVideoList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private ImageView ivThumbnail;
    private FrameLayout flPlayerViewContainer;
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;

    private Context contextHolder;
    private int currentIndex = -1;
    private boolean isPlayerViewAdded;

    private VideoInteractionInterface listener;
    private PlayState playState;

    private final OnClickListener videoViewClickListener = view -> togglePlay();

    public SnappingExoRecycler(@NonNull Context context) {
        super(context);
        initializeRecyclerView(context);
    }

    public SnappingExoRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeRecyclerView(context);
    }

    private void initializeRecyclerView(Context context) {
        contextHolder = context.getApplicationContext();

        linearLayoutManager = (LinearLayoutManager) getLayoutManager();

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(SnappingExoRecycler.this);

        initializePlayer();

        SnappingExoRecycler.this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    playVideo();
                }
            }
        });

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING: {
                        Log.i(TAG, "Kismis: initializeRecyclerView: onPlayerStateChanged: Buffering");
                        break;
                    }
                    case Player.STATE_ENDED: {
                        simpleExoPlayer.seekTo(0);
                        break;
                    }
                    case Player.STATE_IDLE: {
                        Log.i(TAG, "Kismis: initializeRecyclerView: onPlayerStateChanged: Player Idle");
                        break;
                    }
                    case Player.STATE_READY: {
                        Log.i(TAG, "Kismis: initializeRecyclerView: onPlayerStateChanged: Ready to Play");
                        if (isPlayerViewAdded) {
                            Log.i(TAG, "Kismis: initializeRecyclerView: onPlayerStateChanged: playerView is already added");
                        } else {
                            addPlayerView();
                        }
                        break;
                    }
                    default: {
                        Log.e(TAG, "Kismis: initializeRecyclerView: onPlayerStateChanged: State Unknown: " + playbackState);
                        break;
                    }
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e(TAG, "Kismis: initializeRecyclerView: onPlayerError: " + error);
            }
        });
    }

    public void playVideo() {
        int nextIndex = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        if (nextIndex < 0 || nextIndex >= customVideoList.size()) {
            Log.e(TAG, "Kismis: playVideo: nextIndex out of bounds");
        } else {
            if (currentIndex == nextIndex) {
                Log.i(TAG, "Kismis: playVideo: Already Playing at: " + nextIndex);
            } else {
                currentIndex = nextIndex;
                listener.onVideoChanged(currentIndex);

                if (isPlayerViewAdded) {
                    if (playerView == null) {
                        Log.e(TAG, "Kismis: playVideo: playerView is null");
                    } else {
                        removePlayerView(playerView);
                    }
                }

                View targetChild = SnappingExoRecycler.this.getChildAt(0);
                if (targetChild == null) {
                    Log.e(TAG, "Kismis: playVideo: targetChild is null");
                } else {
                    MainPlayerAdapter.CustomViewHolder customViewHolder = (MainPlayerAdapter.CustomViewHolder) targetChild.getTag();
                    if (customViewHolder == null) {
                        currentIndex = -1;
                        Log.e(TAG, "Kismis: playVideo: customVideoPlayerHolder is null");
                    } else {
                        ivThumbnail = customViewHolder.itemView.findViewById(R.id.ksh);
                        Glide.with(contextHolder).load(customVideoList.get(currentIndex).getVideoThumbnailLink()).thumbnail(1f).into(ivThumbnail);

                        flPlayerViewContainer = customViewHolder.itemView.findViewById(R.id.bog);
                        flPlayerViewContainer.setOnClickListener(videoViewClickListener);
                        playerView.setPlayer(simpleExoPlayer);

                        String videoLink = customVideoList.get(currentIndex).getVideoLink();
                        if (videoLink == null) {
                            Log.e(TAG, "Kismis: playVideo: videoLink is null");
                        } else {
                            simpleExoPlayer.prepare(buildMediaSource(videoLink));
                            setPlayState(PlayState.PLAYING);
                        }
                    }
                }
            }
        }
    }

    private MediaSource buildMediaSource(String link) {
        CustomVideoCache customVideoCache = new CustomVideoCache(contextHolder);
        return new ProgressiveMediaSource.Factory(customVideoCache).createMediaSource(Uri.parse(link));
    }

    private void initializePlayer() {
        playerView = new PlayerView(contextHolder);
        playerView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), contextHolder.getResources().getDimension(R.dimen.main_player_corner));
            }
        });

        playerView.setClipToOutline(true);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);//TODO change as desired

        LoadControl loadControl = new DefaultLoadControl.Builder()
                .setAllocator(new DefaultAllocator(true, 16))
                .setBufferDurationsMs(2000, 5000, 1500, 2000)
                .setTargetBufferBytes(-1)
                .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl();

        DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(contextHolder);
        DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector();
        defaultTrackSelector.setParameters(defaultTrackSelector.buildUponParameters().setMaxVideoSizeSd());
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(contextHolder, defaultRenderersFactory, defaultTrackSelector, loadControl);

        playerView.setUseController(false);
        playerView.setPlayer(simpleExoPlayer);
        setPlayState(PlayState.PLAYING);
    }

//    private void cacheVideo(Uri videoUri) {
//        DataSpec dataSpec = new DataSpec(videoUri);
////        CacheUtil.ProgressListener progressListener = (requestLength, bytesCached, newBytesCached) -> Log.e(TAG, String.valueOf(bytesCached * 100.0 / requestLength));
//        DataSource dataSource = new DefaultDataSourceFactory(contextHolder, Util.getUserAgent(contextHolder, "Kismis")).createDataSource();
//        new Thread(() -> {
//            try {
//                CacheUtil.cache(dataSpec, (CustomGlobal.getSimpleCache(contextHolder)), CacheUtil.DEFAULT_CACHE_KEY_FACTORY, dataSource, null/*progressListener*/, null);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

    private void removePlayerView(PlayerView playerView) {
        ViewGroup viewGroup = (ViewGroup) playerView.getParent();
        if (viewGroup == null) {
            Log.e(TAG, "Kismis: removePlayerView: viewGroup is null");
        } else {
            pausePlayer();
            ivThumbnail.setVisibility(VISIBLE);
            viewGroup.removeView(playerView);
            flPlayerViewContainer.setOnClickListener(null);
            playerView.setVisibility(INVISIBLE);
            isPlayerViewAdded = false;
        }
    }

    private void addPlayerView() {
        ivThumbnail.setVisibility(INVISIBLE);
        flPlayerViewContainer.addView(playerView);
        playerView.setVisibility(VISIBLE);
        isPlayerViewAdded = true;
    }

    public void pausePlayer() {
        setPlayState(PlayState.PAUSED);
    }

    public void releasePlayer() {
        if (simpleExoPlayer == null) {
            Log.i(TAG, "Kismis: releasePlayer: simpleExoPlayer is already null");
        } else {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
        flPlayerViewContainer = null;
    }

    private void togglePlay() {
        if (simpleExoPlayer == null) {
            Log.i(TAG, "Kismis: togglePlay: simpleExoPlayer is null");
        } else {
            if (playState == PlayState.PLAYING) {
                setPlayState(PlayState.PAUSED);
            } else if (playState == PlayState.PAUSED) {
                setPlayState(PlayState.PLAYING);
            } else {
                Log.e(TAG, "Kismis: togglePlay: PlayState Unknown: " + playState);
            }
        }
    }

    private void setPlayState(PlayState playStateInput) {
        playState = playStateInput;
        if (playState == PlayState.PLAYING) {
            simpleExoPlayer.setPlayWhenReady(true);
        } else if (playState == PlayState.PAUSED) {
            simpleExoPlayer.setPlayWhenReady(false);
        } else {
            Log.e(TAG, "Kismis: togglePlay: PlayState Unknown: " + playStateInput);
        }

        if (listener == null) {
            Log.i(TAG, "Kismis: togglePlay: listener is null");
        } else {
            if (playState == PlayState.PLAYING) {
                listener.onTogglePlayTapped(false);
            } else if (playState == PlayState.PAUSED) {
                listener.onTogglePlayTapped(true);
            } else {
                Log.e(TAG, "Kismis: togglePlay: PlayState Unknown: " + playStateInput);
            }
        }
    }

    public void initializePlayList(VideoInteractionInterface listener, List<CustomVideo> customVideoList) {
        this.listener = listener;
        this.customVideoList = customVideoList;
    }

    private enum PlayState {
        PLAYING, PAUSED
    }
}