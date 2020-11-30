package com.simpleharmonics.kismis.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.uis.activities.MessageActivity;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder> {

    private final Context context;
    private final List<CustomChat> customChatList;

    public ChatAdapter(Context context, List<CustomChat> customChatList) {
        this.context = context;
        this.customChatList = customChatList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {
        CustomChat customChat = customChatList.get(position);

        ShapeableImageView sivProfilePicture = customViewHolder.itemView.findViewById(R.id.lqh);
        String foreignUserProfilePictureLink = customChat.getForeignUserProfilePictureLink();
        if (foreignUserProfilePictureLink == null || foreignUserProfilePictureLink.isEmpty()) {
            Glide.with(context).load(context.getDrawable(R.drawable.avatar)).into(sivProfilePicture);
        } else {
            Glide.with(context).load(foreignUserProfilePictureLink).into(sivProfilePicture);
        }

        TextView tvName = customViewHolder.itemView.findViewById(R.id.ngy);
        tvName.setText(customChat.getForeignUserName());

        TextView tvLastMessageTime = customViewHolder.itemView.findViewById(R.id.fpo);
        tvLastMessageTime.setText(customChat.getLastMessageTime());

        TextView tvLastMessage = customViewHolder.itemView.findViewById(R.id.nor);
        tvLastMessage.setText(customChat.getLastMessage());

        customViewHolder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, MessageActivity.class)));
    }

    @Override
    public int getItemCount() {
        return customChatList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class CustomChat {

        private final String foreignUserID;
        private final String foreignUserProfilePictureLink;
        private final String foreignUserName;
        private final String lastMessageTime;
        private final String lastMessage;

        public CustomChat(String foreignUserID, String foreignUserProfilePictureLink, String foreignUserName, String lastMessageTime, String lastMessage) {
            this.foreignUserID = foreignUserID;
            this.foreignUserProfilePictureLink = foreignUserProfilePictureLink;
            this.foreignUserName = foreignUserName;
            this.lastMessageTime = lastMessageTime;
            this.lastMessage = lastMessage;
        }

        public String getForeignUserID() {
            return foreignUserID;
        }

        public String getForeignUserProfilePictureLink() {
            return foreignUserProfilePictureLink;
        }

        public String getForeignUserName() {
            return foreignUserName;
        }

        public String getLastMessageTime() {
            return lastMessageTime;
        }

        public String getLastMessage() {
            return lastMessage;
        }
    }
}
