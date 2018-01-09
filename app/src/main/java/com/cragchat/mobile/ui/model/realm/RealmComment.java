package com.cragchat.mobile.ui.model.realm;

import com.cragchat.mobile.ui.model.Comment;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.model.pojo.PojoComment;
import com.cragchat.mobile.util.RealmUtil;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 10/20/2017.
 */

public class RealmComment extends RealmObject implements Comment, Datable {

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
    public static final String FIELD_ENTITY_NAME = "entityName";

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
    private RealmList<String> childrenIds;
    private String table;
    private String entityName;

    public static RealmComment from(PojoComment comment) {
        RealmComment realmComment = new RealmComment();
        realmComment.setKey(comment.getKey());
        realmComment.setEntityId(comment.getEntityId());
        realmComment.setComment(comment.getComment());
        realmComment.setScore(comment.getScore());
        realmComment.setDate(comment.getDate());
        realmComment.setParentId(comment.getParentId());
        realmComment.setDepth(comment.getDepth());
        realmComment.setAuthorName(comment.getAuthorName());
        realmComment.setTable(comment.getTable());
        realmComment.setChildrenIds(RealmUtil.convertListToRealmList(comment.getChildrenIds()));
        realmComment.setEntityName(comment.getEntityName());
        return realmComment;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

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

    public RealmList<String> getChildrenIds() {
        return childrenIds;
    }

    public void setChildrenIds(RealmList<String> childrenIds) {
        this.childrenIds = childrenIds;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
