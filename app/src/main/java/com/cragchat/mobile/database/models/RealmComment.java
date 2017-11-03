package com.cragchat.mobile.database.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 10/20/2017.
 */

public class RealmComment extends RealmObject {

    public static final String FIELD_COMMENT = "comment";
    public static final String FIELD_SCORE = "score";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_ID = "key";
    public static final String FIELD_ENTITY_ID = "entityId";
    public static final String FIELD_PARENT_ID = "parentId";
    public static final String FIELD_DEPTH = "depth";
    public static final String FIELD_AUTHOR = "authorName";
    public static final String FIELD_CHILDREN_IDS = "childrenIds";
    public static final String FIELD_TABLE = "table";

    private String comment;
    private int score;
    private String date;
    @PrimaryKey
    @Index
    private String key;
    private String entityId;
    private String parentId;
    private int depth;
    private String authorName;
    private RealmList<Tag> childrenIds;
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

    public RealmList<Tag> getChildrenIds() {
        return childrenIds;
    }

    public void setChildrenIds(RealmList<Tag> childrenIds) {
        this.childrenIds = childrenIds;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
