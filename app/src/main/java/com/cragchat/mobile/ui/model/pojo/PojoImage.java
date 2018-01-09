package com.cragchat.mobile.ui.model.pojo;


import android.os.Parcel;

import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.model.Image;

public class PojoImage implements Datable, Image {

    private String entityKey;
    private String caption;
    private String filename;
    private String date;
    private String authorName;
    private String entityName;

    public String getEntityKey() {
        return entityKey;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

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

    public PojoImage() {
    }

    protected PojoImage(Parcel in) {
        this.entityKey = in.readString();
        this.caption = in.readString();
        this.filename = in.readString();
        this.date = in.readString();
        this.authorName = in.readString();
        this.entityName = in.readString();
    }

    public static final Creator<PojoImage> CREATOR = new Creator<PojoImage>() {
        @Override
        public PojoImage createFromParcel(Parcel source) {
            return new PojoImage(source);
        }

        @Override
        public PojoImage[] newArray(int size) {
            return new PojoImage[size];
        }
    };
}
