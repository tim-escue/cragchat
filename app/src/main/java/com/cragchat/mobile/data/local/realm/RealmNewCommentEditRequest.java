package com.cragchat.mobile.data.local.realm;

import com.cragchat.mobile.data.local.model.NewCommentEditRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewCommentEditRequest extends RealmObject implements NewCommentEditRequest {

    private String userToken;
    private String comment;

    public RealmNewCommentEditRequest() {
    }

    private String commentKey;

    public RealmNewCommentEditRequest(String userToken, String comment, String commentKey) {
        this.userToken = userToken;
        this.comment = comment;
        this.commentKey = commentKey;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }
}
