package com.cragchat.mobile.model;

/**
 * Created by timde on 11/28/2017.
 */

public interface NewCommentReplyRequest {
    String getComment();

    String getEntityKey();

    String getTable();

    String getParentId();

    int getDepth();
}