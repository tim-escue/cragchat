package com.cragchat.mobile.database.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 11/1/2017.
 */

public class RealmRating extends RealmObject{

    public static final String FIELD_ENTITY_KEY = "entityKey";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_YDS = "yds";
    public static final String FIELD_STARS = "stars";
    public static final String FIELD_DATE = "date";

    @Index
    private String entityKey;
    @PrimaryKey
    private String key;
    @Index
    private String username;
    private int yds;
    private int stars;
    private String date;

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYds() {
        return yds;
    }

    public void setYds(int yds) {
        this.yds = yds;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
