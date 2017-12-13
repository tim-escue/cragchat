package com.cragchat.mobile.model.realm;

import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.model.pojo.PojoImage;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 10/23/2017.
 */

public class RealmImage extends RealmObject implements Image, Datable {

    public static final String FIELD_ENTITY_KEY = "entityKey";
    public static final String FIELD_AUTHOR_NAME = "authorName";
    public static final String FIELD_CAPTION = "caption";
    public static final String FIELD_FILENAME = "filename";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_ENTITY_NAME = "entityName";

    @Index
    private String entityKey;
    private String caption;
    @Index
    @PrimaryKey
    private String filename;
    private String date;
    @Index
    private String authorName;
    private String entityName;

    public static RealmImage from(PojoImage pojoImage) {
        RealmImage image = new RealmImage();
        image.setAuthorName(pojoImage.getAuthorName());
        image.setCaption(pojoImage.getCaption());
        image.setEntityKey(pojoImage.getEntityKey());
        image.setFilename(pojoImage.getFilename());
        image.setDate(pojoImage.getDate());
        image.setEntityName(pojoImage.getEntityName());
        return image;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    @Override
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
