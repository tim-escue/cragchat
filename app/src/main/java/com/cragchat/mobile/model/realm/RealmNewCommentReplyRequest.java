package com.cragchat.mobile.model.realm;

import com.cragchat.mobile.model.NewCommentReplyRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewCommentReplyRequest extends RealmObject implements NewCommentReplyRequest {

    private String comment;
    private String entityKey;
    private String table;
    private String parentId;
    private int depth;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}