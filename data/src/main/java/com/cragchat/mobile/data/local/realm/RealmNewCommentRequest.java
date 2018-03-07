package com.cragchat.mobile.data.local.realm;

import com.cragchat.mobile.data.local.model.NewCommentRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/27/2017.
 */

public class RealmNewCommentRequest extends RealmObject implements NewCommentRequest {

    private String comment;
    private String entityKey;
    private String table;
    private String userToken;

    public RealmNewCommentRequest() {
    }

    public RealmNewCommentRequest(String comment, String entityKey, String table, String userToken) {

        this.comment = comment;
        this.entityKey = entityKey;
        this.table = table;
        this.userToken = userToken;
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

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
