package com.cragchat.mobile.model.realm;

import com.cragchat.mobile.model.NewImageRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewImageRequest extends RealmObject implements NewImageRequest {

    private String captionString;
    private String entityKey;
    private String entityType;
    private String filePath;

    public String getCaptionString() {
        return captionString;
    }

    public void setCaptionString(String captionString) {
        this.captionString = captionString;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String fileUri) {
        this.filePath = fileUri;
    }
}
