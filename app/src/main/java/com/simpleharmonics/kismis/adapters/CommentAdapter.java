package com.simpleharmonics.kismis.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.classes.CustomTools;
import com.simpleharmonics.kismis.entities.CustomComment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomViewHolder> {

    private final Context context;
    private final List<CustomComment> customCommentList;

    public CommentAdapter(Context context, List<CustomComment> customCommentList) {
        this.context = context;
        this.customCommentList = customCommentList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {
        final CustomComment customComment = customCommentList.get(position);

        ShapeableImageView cvOwnerDP = customViewHolder.itemView.findViewById(R.id.rxh);
        String commentOwnerDPLink = customComment.getCommentOwnerDPLink();
        if (commentOwnerDPLink == null || commentOwnerDPLink.isEmpty()) {
            Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.avatar)).into(cvOwnerDP);
        } else {
            Glide.with(context).load(commentOwnerDPLink).into(cvOwnerDP);
        }

        TextView tvCommentOwnerTAG = customViewHolder.itemView.findViewById(R.id.dwh);
        tvCommentOwnerTAG.setText(customComment.getCommentOwnerTAG().concat(" • ").concat(customComment.getCommentTime()));

        TextView tvCommentText = customViewHolder.itemView.findViewById(R.id.hvt);
        tvCommentText.setText(customComment.getCommentText());

        ImageButton ibLike = customViewHolder.itemView.findViewById(R.id.mob);
        TextView tvCommentLikes = customViewHolder.itemView.findViewById(R.id.mdj);
        tvCommentLikes.setText(CustomTools.getReducedNumber(customComment.getCommentLikes()));

        ImageButton ibDislike = customViewHolder.itemView.findViewById(R.id.zyi);
        TextView tvCommentDislikes = customViewHolder.itemView.findViewById(R.id.num);
        tvCommentDislikes.setText(CustomTools.getReducedNumber(customComment.getCommentDislikes()));

        ibLike.setOnClickListener(v -> {
            if (customComment.getUserReaction() == 1) {
                customComment.setUserReaction(0);
            } else {
                customComment.setUserReaction(1);
            }
            toggleUserLikeStatus(customComment, ibLike, ibDislike);
        });

        ibDislike.setOnClickListener(v -> {
            if (customComment.getUserReaction() == -1) {
                customComment.setUserReaction(0);
            } else {
                customComment.setUserReaction(-1);
            }
            toggleUserLikeStatus(customComment, ibLike, ibDislike);
        });

        toggleUserLikeStatus(customComment, ibLike, ibDislike);
    }

    private void toggleUserLikeStatus(CustomComment customComment, ImageButton ibLike, ImageButton ibDislike) {
        if (customComment.getUserReaction() == -1) {
            ibLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_gray_24));
            ibDislike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dislike_primary_24));
        } else if (customComment.getUserReaction() == 1) {
            ibLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_primary_24));
            ibDislike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dislike_gray_24));
        } else {
            ibLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like_gray_24));
            ibDislike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dislike_gray_24));
        }
    }

    @Override
    public int getItemCount() {
        return customCommentList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
