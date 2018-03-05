package com.cragchat.mobile.data.local.realm;

import com.cragchat.mobile.data.local.model.NewSendRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewSendRequest extends RealmObject implements NewSendRequest {

    private String entityKey;
    private int pitches;
    private int attempts;
    private String sendType;
    private String climbingStyle;
    private String entityName;
    private String userToken;

    public RealmNewSendRequest(){}

    public RealmNewSendRequest(String entityKey, int pitches, int attempts, String sendType, String climbingStyle, String entityName, String userToken) {
        this.entityKey = entityKey;
        this.pitches = pitches;
        this.attempts = attempts;
        this.sendType = sendType;
        this.climbingStyle = climbingStyle;
        this.entityName = entityName;
        this.userToken = userToken;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public int getPitches() {
        return pitches;
    }

    public void setPitches(int pitches) {
        this.pitches = pitches;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getClimbingStyle() {
        return climbingStyle;
    }

    public void setClimbingStyle(String climbingStyle) {
        this.climbingStyle = climbingStyle;
    }

    @Override
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
