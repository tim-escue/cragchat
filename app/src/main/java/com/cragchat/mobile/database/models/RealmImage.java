package com.cragchat.mobile.database.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 10/23/2017.
 */

public class RealmImage extends RealmObject {

    public static final String FIELD_ENTITY_KEY = "entityKey";
    public static final String FIELD_AUTHOR_NAME = "authorName";
    public static final String FIELD_CAPTION = "caption";
    public static final String FIELD_FILENAME = "filename";
    public static final String FIELD_DATE = "date";

    @Index
    private String entityKey;
    private String caption;
    @Index
    @PrimaryKey
    private String filename;
    private String date;
    @Index
    private String authorName;

    public String getEntityKey() {
        return entityKey;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
