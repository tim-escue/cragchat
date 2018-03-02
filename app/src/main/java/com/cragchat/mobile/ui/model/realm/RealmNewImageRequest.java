package com.cragchat.mobile.ui.model.realm;

import com.cragchat.mobile.ui.model.NewImageRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewImageRequest extends RealmObject implements NewImageRequest {

    private String captionString;
    private String entityKey;
    private String entityType;
    private String filePath;
    private String entityName;
    private String userToken;

    public RealmNewImageRequest(String captionString, String entityKey, String entityType, String filePath, String entityName, String userToken) {
        this.captionString = captionString;
        this.entityKey = entityKey;
        this.entityType = entityType;
        this.filePath = filePath;
        this.entityName = entityName;
        this.userToken = userToken;
    }

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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
