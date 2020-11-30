package com.simpleharmonics.kismis.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;
import com.simpleharmonics.kismis.entities.CustomComment;

import java.util.List;

public interface CommentGetInterface {

    void onCommentGetStart();

    void onCommentGetComplete(List<CustomComment> customCommentList, DocumentSnapshot dsLastComment);

    void onCommentGetError();
}
