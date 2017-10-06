package com.cragchat.mobile.database.models;

import com.cragchat.mobile.model.Area;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by timde on 9/27/2017.
 */

public class RealmArea extends RealmObject implements Area {

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
    private double latitude;
    private double longitude;
    private RealmArea parent;
    private RealmList<RealmArea> subAreas;
    private RealmList<RealmRoute> routes;

    /*
        Empty constructor for RealmObject
     */
    public RealmArea() {}

    public RealmArea(String name, String key, double latitude, double longitude, RealmArea parent, RealmList<RealmArea> subAreas, RealmList<RealmRoute> routes) {
        this.setName(name);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setSubAreas(subAreas);
        this.setRoutes(routes);
        this.setParent(parent);
        this.setKey(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<RealmArea> getSubAreas() {
        return subAreas;
    }

    public void setSubAreas(RealmList<RealmArea> subAreas) {
        this.subAreas = subAreas;
    }

    public List<RealmRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(RealmList<RealmRoute> routes) {
        this.routes = routes;
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
        if (getParent() != null) {
            builder.append("parent:").append(getParent().getName()).append(" ");
        }
        builder.append("routes:").append(getRoutes().size()).append(" ");
        builder.append("areas:").append(getSubAreas().size());
        return builder.toString();
    }
}
