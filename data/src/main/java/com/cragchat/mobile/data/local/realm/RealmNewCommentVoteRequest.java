package com.cragchat.mobile.data.local.realm;

import com.cragchat.mobile.data.local.model.NewCommentVoteRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewCommentVoteRequest extends RealmObject implements NewCommentVoteRequest {
    private String vote;
    private String commentKey;
    private String userToken;

    public RealmNewCommentVoteRequest() {
    }

    public RealmNewCommentVoteRequest(String vote, String commentKey, String userToken) {
        this.vote = vote;
        this.commentKey = commentKey;
        this.userToken = userToken;
    }

    public String getUserToken() {

        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }
}
