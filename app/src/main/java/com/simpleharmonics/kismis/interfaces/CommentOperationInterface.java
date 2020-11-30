package com.simpleharmonics.kismis.interfaces;

public interface CommentOperationInterface {

    void onCommentOperationStart();

    void onAddCommentComplete();

    void onEditCommentComplete();

    void onDeleteCommentComplete();

    void onCommentOperationError();
}
