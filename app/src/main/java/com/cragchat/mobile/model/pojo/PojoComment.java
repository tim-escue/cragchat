package com.cragchat.mobile.model.pojo;

import java.util.List;

import io.realm.RealmList;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 11/10/2017.
 */

public class PojoComment {

    private String comment;
    private int score;
    private String date;
    private String key;
    private String entityId;
    private String parentId;
    private int depth;
    private String authorName;
    private List<String> childrenIds;
    private String table;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<String> getChildrenIds() {
        return childrenIds;
    }

    public void setChildrenIds(List<String> childrenIds) {
        this.childrenIds = childrenIds;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
