package com.simpleharmonics.kismis.uis.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.CommentAdapter;
import com.simpleharmonics.kismis.classes.CustomFirebase;
import com.simpleharmonics.kismis.entities.CustomComment;
import com.simpleharmonics.kismis.interfaces.CommentGetInterface;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetComments extends BottomSheetDialogFragment implements CommentGetInterface {

    private static final String TAG = "BottomSheetComments";

    private static String videoID;
    private DocumentSnapshot dsLastComment;

    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private List<CustomComment> customCommentList = new ArrayList<>();

    private Handler handler = new Handler();

    public static BottomSheetComments newInstance(String videoID) {
        BottomSheetComments.videoID = videoID;
        return new BottomSheetComments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.layout_bottom_sheet_comments, container, false);

        rvComments = fragment.findViewById(R.id.cdi);
        rvComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvComments.setHasFixedSize(true);
        commentAdapter = new CommentAdapter(getActivity(), customCommentList);
        rvComments.setAdapter(commentAdapter);

        new Handler().postDelayed(this::getComments, 300);

        return fragment;
    }

    private void getComments() {
        FragmentActivity fragmentActivity = BottomSheetComments.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: showComments: fragmentActivity is null");
        } else {
            CustomFirebase.getCommentList(fragmentActivity, BottomSheetComments.this, dsLastComment, videoID);
        }

        String commentID = "dsakfnsjkl";
        String commentOwnerID = "";
        String commentOwnerTAG = "lorem_ipsum";
        String commentOwnerDPLink = "";
        String commentTime = "5h";
        String commentText;
        long commentLikes = 23526;
        long commentDislikes = 43634;
        int userReaction;

        for (int i = 0; i < 30; i++) {
            userReaction = i % 3;
            if (userReaction == 2) {
                userReaction = -1;
            }
            if (i % 3 == 0) {
                commentText = "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.";
            } else if (i % 3 == 1) {
                commentText = "Lorem Ipsum is simply dummy text of the printing.";
            } else {
                commentText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
            }
            CustomComment customComment = new CustomComment(commentID, commentOwnerID, commentOwnerTAG, commentOwnerDPLink, commentTime, commentText, commentLikes, commentDislikes, userReaction);
            customCommentList.add(customComment);
        }

        commentAdapter = new CommentAdapter(getActivity(), customCommentList);
        rvComments.setAdapter(commentAdapter);
    }

    @Override
    public void onCommentGetStart() {
        handler.post(() -> {

        });
    }

    @Override
    public void onCommentGetComplete(List<CustomComment> customCommentList, DocumentSnapshot dsLastComment) {
        handler.post(() -> {
            this.dsLastComment = dsLastComment;
            this.customCommentList.addAll(customCommentList);
            commentAdapter = new CommentAdapter(getActivity(), customCommentList);
            rvComments.setAdapter(commentAdapter);
        });
    }

    @Override
    public void onCommentGetError() {
        handler.post(() -> {

        });
    }
}
