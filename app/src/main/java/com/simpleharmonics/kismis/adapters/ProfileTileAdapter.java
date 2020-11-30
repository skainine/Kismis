package com.simpleharmonics.kismis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.interfaces.ProfileTileInterface;

import java.util.List;

public class ProfileTileAdapter extends RecyclerView.Adapter<ProfileTileAdapter.CustomViewHolder> {

    private final Context context;
    private final ProfileTileInterface listener;
    private final List<CustomProfileTile> customProfileTileList;

    public ProfileTileAdapter(Context context, ProfileTileInterface listener, List<CustomProfileTile> customProfileTileList) {
        this.context = context;
        this.listener = listener;
        this.customProfileTileList = customProfileTileList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_profile_tile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {
        CustomProfileTile customVideoTile = customProfileTileList.get(position);
        ShapeableImageView ivProfileImage = customViewHolder.itemView.findViewById(R.id.hfa);
        String profileImageLink = customVideoTile.getProfileImageLink();
        if (profileImageLink == null || profileImageLink.isEmpty()) {
            Glide.with(context).load(context.getDrawable(R.drawable.avatar)).into(ivProfileImage);
        } else {
            Glide.with(context).load(profileImageLink).into(ivProfileImage);
        }

        MaterialTextView mtvDisplayName = customViewHolder.itemView.findViewById(R.id.vdi);
        mtvDisplayName.setText(customVideoTile.getDisplayName());

        MaterialTextView mtvDisplayID = customViewHolder.itemView.findViewById(R.id.uxh);
        mtvDisplayID.setText(customVideoTile.getDisplayID());

        MaterialButton mbtFollow = customViewHolder.itemView.findViewById(R.id.axs);
        MaterialTextView mtvFollowing = customViewHolder.itemView.findViewById(R.id.qyx);

        if (customVideoTile.isFollowed()) {
            mbtFollow.setVisibility(View.GONE);
            mtvFollowing.setVisibility(View.VISIBLE);
        } else {
            mtvFollowing.setVisibility(View.GONE);
            mbtFollow.setVisibility(View.VISIBLE);
        }

        MaterialTextView mtvPosts = customViewHolder.itemView.findViewById(R.id.toa);
        mtvPosts.setText(CustomTools.getReducedNumber(customVideoTile.getPosts()));

        MaterialTextView mtvFollowers = customViewHolder.itemView.findViewById(R.id.wrr);
        mtvFollowers.setText(CustomTools.getReducedNumber(customVideoTile.getFollowers()));

        customViewHolder.itemView.setOnClickListener(v -> listener.onProfileTileClicked(customVideoTile));
    }

    @Override
    public int getItemCount() {
        return customProfileTileList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class CustomProfileTile {

        private final String profileID;
        private final String displayName;
        private final String displayID;
        private final String profileImageLink;
        private final boolean isFollowed;
        private final long posts;
        private final long followers;
        private final long following;

        public CustomProfileTile(String profileID, String displayName, String displayID, String profileImageLink, boolean isFollowed, long posts, long followers, long following) {
            this.profileID = profileID;
            this.displayName = displayName;
            this.displayID = displayID;
            this.profileImageLink = profileImageLink;
            this.isFollowed = isFollowed;
            this.posts = posts;
            this.followers = followers;
            this.following = following;
        }

        public String getProfileID() {
            return profileID;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDisplayID() {
            return displayID;
        }

        public String getProfileImageLink() {
            return profileImageLink;
        }

        public boolean isFollowed() {
            return isFollowed;
        }

        public long getPosts() {
            return posts;
        }

        public long getFollowers() {
            return followers;
        }

        public long getFollowing() {
            return following;
        }
    }
}
