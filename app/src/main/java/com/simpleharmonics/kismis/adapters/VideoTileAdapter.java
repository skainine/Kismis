package com.simpleharmonics.kismis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.entities.CustomVideo;
import com.simpleharmonics.kismis.interfaces.VideoTileInterface;

import java.util.List;

public class VideoTileAdapter extends RecyclerView.Adapter<VideoTileAdapter.CustomViewHolder> {

    private final Context context;
    private final VideoTileInterface listener;
    private final List<CustomVideo> customVideoList;

    public VideoTileAdapter(Context context, VideoTileInterface listener, List<CustomVideo> customVideoList) {
        this.context = context;
        this.listener = listener;
        this.customVideoList = customVideoList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_video_tile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int index) {
        CustomVideo customVideo = customVideoList.get(index);
        ImageView ivTile = customViewHolder.itemView.findViewById(R.id.jzn);
        Glide.with(context).load(customVideo.getVideoThumbnailLink()).thumbnail(1f).into(ivTile);

        ivTile.setOnClickListener(v -> listener.onVideoTileClicked(customVideoList, index));
    }

    @Override
    public int getItemCount() {
        return customVideoList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
