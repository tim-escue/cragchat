package com.cragchat.mobile.data.local.realm;

import android.os.Parcel;

import com.cragchat.mobile.domain.model.Datable;
import com.cragchat.mobile.domain.model.Image;

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

    public static RealmImage from(Image pojoImage) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.entityKey);
        dest.writeString(this.caption);
        dest.writeString(this.filename);
        dest.writeString(this.date);
        dest.writeString(this.authorName);
        dest.writeString(this.entityName);
    }

    public RealmImage() {
    }

    protected RealmImage(Parcel in) {
        this.entityKey = in.readString();
        this.caption = in.readString();
        this.filename = in.readString();
        this.date = in.readString();
        this.authorName = in.readString();
        this.entityName = in.readString();
    }

    public static final Creator<RealmImage> CREATOR = new Creator<RealmImage>() {
        @Override
        public RealmImage createFromParcel(Parcel source) {
            return new RealmImage(source);
        }

        @Override
        public RealmImage[] newArray(int size) {
            return new RealmImage[size];
        }
    };
}
