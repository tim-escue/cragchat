package com.cragchat.mobile.ui.model.realm;

import com.cragchat.mobile.ui.model.NewSendRequest;

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
}
