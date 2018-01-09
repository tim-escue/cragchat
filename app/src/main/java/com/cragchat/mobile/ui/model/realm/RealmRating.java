package com.cragchat.mobile.ui.model.realm;

import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.model.Rating;
import com.cragchat.mobile.ui.model.pojo.PojoRating;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 11/1/2017.
 */

public class RealmRating extends RealmObject implements Rating, Datable {

    public static final String FIELD_ENTITY_KEY = "entityKey";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_YDS = "yds";
    public static final String FIELD_STARS = "stars";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_ENTITY_NAME = "entityName";

    @Index
    private String entityKey;
    @PrimaryKey
    private String key;
    @Index
    private String username;
    private int yds;
    private int stars;
    private String date;
    private String entityName;


    public static RealmRating from(PojoRating pojo) {
        RealmRating rating = new RealmRating();
        rating.setEntityKey(pojo.getEntityKey());
        rating.setKey(pojo.getKey());
        rating.setUsername(pojo.getUsername());
        rating.setYds(pojo.getYds());
        rating.setStars(pojo.getStars());
        rating.setDate(pojo.getDate());
        rating.setEntityName(pojo.getEntityName());
        return rating;
    }

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

    @Override
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
