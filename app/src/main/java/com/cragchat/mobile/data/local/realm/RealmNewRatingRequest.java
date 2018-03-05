package com.cragchat.mobile.data.local.realm;

import com.cragchat.mobile.data.local.model.NewRatingRequest;

import io.realm.RealmObject;

/**
 * Created by timde on 11/28/2017.
 */

public class RealmNewRatingRequest extends RealmObject implements NewRatingRequest {
    private String userToken;
    private int stars;
    private int yds;
    private String entityName;
    private String entityKey;

    public RealmNewRatingRequest(){}

    public RealmNewRatingRequest(String userToken, int stars, int yds, String entityName, String entityKey) {
        this.stars = stars;
        this.yds = yds;
        this.entityName = entityName;
        this.userToken = userToken;
        this.entityKey = entityKey;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getYds() {
        return yds;
    }

    public void setYds(int yds) {
        this.yds = yds;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
