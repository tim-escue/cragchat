package com.cragchat.mobile.model.pojo;

import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 11/10/2017.
 */

public class PojoRating {

    private String entityKey;
    private String key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
}
