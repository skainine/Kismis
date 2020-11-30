package com.simpleharmonics.kismis.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.entities.CustomVideo;

import java.util.List;

public class MainPlayerAdapter extends RecyclerView.Adapter<MainPlayerAdapter.CustomViewHolder> {

    private final List<CustomVideo> customVideoList;

    public MainPlayerAdapter(List<CustomVideo> customVideoList) {
        this.customVideoList = customVideoList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_main_player, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {
        customViewHolder.itemView.setTag(customViewHolder);
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
