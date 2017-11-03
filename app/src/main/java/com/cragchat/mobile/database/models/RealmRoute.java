package com.cragchat.mobile.database.models;

import com.cragchat.mobile.model.Route;

import java.util.HashMap;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class RealmRoute extends RealmObject implements Route {

    public static final String FIELD_KEY = "key";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_PARENT = "parent";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_STARS = "stars";
    public static final String FIELD_YDS = "yds";
    public static final String FIELD_IMAGES = "images";

    @Index
    @PrimaryKey
    private String key;
    private String name;
    @Index
    private String type;
    private String latitude;
    private String longitude;
    @Index
    private int yds;
    private double stars;
    @Index
    private String parent;
    private String sends;
    private String ratings;
    private RealmList<Tag> images;

    public RealmRoute() {
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public HashMap<String, Object> getMap() {
        HashMap<String, Object> routeMap = new HashMap<>();
        routeMap.put(RealmRoute.FIELD_KEY, key);
        routeMap.put(RealmRoute.FIELD_NAME, getName());
        routeMap.put(RealmRoute.FIELD_LATITUDE, getLatitude());
        routeMap.put(RealmRoute.FIELD_LONGITUDE, getLongitude());
        routeMap.put(RealmRoute.FIELD_PARENT, getParent());
        routeMap.put(RealmRoute.FIELD_TYPE, getType());
        routeMap.put(RealmRoute.FIELD_STARS, getStars());
        routeMap.put(RealmRoute.FIELD_YDS, getYds());
        return routeMap;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getYds() {
        return yds;
    }

    public void setYds(int yds) {
        this.yds = yds;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name:").append(getName()).append(" ");
        builder.append("parent:").append(getParent()).append(" ");
        builder.append("yds:").append(getYds()).append(" ");
        builder.append("stars:").append(getStars());
        return builder.toString();
    }

    public String getSends() {
        return sends;
    }

    public void setSends(String sends) {
        this.sends = sends;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public RealmList<Tag> getImages() {
        return images;
    }

    public void setImages(RealmList<Tag> images) {
        this.images = images;
    }
}