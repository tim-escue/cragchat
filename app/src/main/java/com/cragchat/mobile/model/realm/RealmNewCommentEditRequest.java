package com.cragchat.mobile.model.realm;

import com.cragchat.mobile.model.NewCommentEditRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewCommentEditRequest extends RealmObject implements NewCommentEditRequest {

    private String userToken;
    private String comment;
    private String commentKey;

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