package com.cragchat.mobile.ui.model.realm;

import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.model.Send;
import com.cragchat.mobile.ui.model.pojo.PojoSend;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 11/3/2017.
 */

public class RealmSend extends RealmObject implements Send, Datable {

    public static final String FIELD_ENTITY_KEY = "entityKey";
    public static final String FIELD_PITCHES = "pitches";
    public static final String FIELD_SEND_TYPE = "sendType";
    public static final String FIELD_ATTEMPTS = "attempts";
    public static final String FIELD_CLIMBING_STYLE = "climbingStyle";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_ENTITY_NAME = "entityName";

    @PrimaryKey
    private String key;
    @Index
    private String entityKey;
    private int pitches;
    private String sendType; //redpoint, flash etc
    private int attempts;
    private String climbingStyle; //lead, toprope etc
    private String date;
    @Index
    private String username;
    private String entityName;

    public static RealmSend from(Send send) {
        RealmSend s = new RealmSend();
        s.setAttempts(send.getAttempts());
        s.setKey(send.getKey());
        s.setPitches(send.getPitches());
        s.setSendType(send.getSendType());
        s.setClimbingStyle(send.getClimbingStyle());
        s.setUsername(send.getUsername());
        s.setDate(send.getDate());
        s.setEntityKey(send.getEntityKey());
        s.setEntityName(send.getEntityName());
        return s;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
