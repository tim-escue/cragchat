package com.cragchat.mobile.model.pojo;

import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.model.Send;

/**
 * Created by timde on 11/13/2017.
 */

public class PojoSend implements Datable, Send {

    private String key;
    private String entityKey;
    private int pitches;
    private String sendType; //redpoint, flash etc
    private int attempts;
    private String climbingStyle; //lead, toprope etc
    private String date;
    private String username;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getClimbingStyle() {
        return climbingStyle;
    }

    public void setClimbingStyle(String climbingStyle) {
        this.climbingStyle = climbingStyle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
