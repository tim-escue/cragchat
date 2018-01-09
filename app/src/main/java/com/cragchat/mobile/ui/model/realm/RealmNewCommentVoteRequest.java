package com.cragchat.mobile.ui.model.realm;

import com.cragchat.mobile.ui.model.NewCommentVoteRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewCommentVoteRequest extends RealmObject implements NewCommentVoteRequest {
    private String vote;
    private String commentKey;

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
