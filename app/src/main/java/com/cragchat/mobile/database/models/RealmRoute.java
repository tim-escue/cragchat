package com.cragchat.mobile.database.models;

import com.cragchat.mobile.model.Route;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmRoute extends RealmObject implements Route {

    public static final String FIELD_KEY = "key";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_PARENT = "parent";
    public static final String FIELD_SUBAREAS = "subAreas";
    public static final String FIELD_ROUTES = "routes";

    @PrimaryKey
    private String key;
    private String name;
    private String type;
    private double latitude;
    private double longitude;
    private int yds;
    private double stars;
    private RealmArea parent;

    public RealmRoute() {}

    public RealmRoute(String key, String name, String type,
                      double latitude, double longitude, RealmArea parent) {
        this.setName(name);
        this.setType(type);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setParent(parent);
        this.setKey(key);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
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

    public RealmArea getParent() {
        return parent;
    }

    public void setParent(RealmArea parent) {
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
        builder.append("parent:").append(getParent().getName()).append(" ");
        builder.append("yds:").append(getYds()).append(" ");
        builder.append("stars:").append(getStars());
        return builder.toString();
    }
}