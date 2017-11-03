package com.cragchat.mobile.database.models;

import com.cragchat.mobile.model.Area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
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
    public static final String FIELD_IMAGES = "images";

    @Index
    @PrimaryKey
    private String key;
    private String name;
    private String latitude;
    private String longitude;
    @Index
    private String parent;
    private RealmList<Tag> subAreas;
    private RealmList<Tag> routes;
    private RealmList<Tag> images;


    /*
        Empty constructor for RealmObject
     */
    public RealmArea() {
    }

    public RealmArea(String name, String key,
                     String latitude, String longitude,
                     String parent, RealmList<Tag> subAreas,
                     RealmList<Tag> routes, RealmList<Tag> images) {
        this.setName(name);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setSubAreas(subAreas);
        this.setRoutes(routes);
        this.setParent(parent);
        this.setKey(key);
        this.setImages(images);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    @Override
    public HashMap<String, Object> getMap() {
        HashMap<String, Object> areaMap = new HashMap<>();
        areaMap.put(RealmArea.FIELD_KEY, getKey());
        areaMap.put(RealmArea.FIELD_NAME, getName());
        areaMap.put(RealmArea.FIELD_PARENT, getParent() != null ? getParent() : "");
        areaMap.put(RealmArea.FIELD_LATITUDE, getLatitude());
        areaMap.put(RealmArea.FIELD_LONGITUDE, getLongitude());
        List<String> subareas = new ArrayList<>();
        for (Tag i : getSubAreas()) {
            subareas.add(i.getValue());
        }
        areaMap.put(RealmArea.FIELD_SUBAREAS, subareas);
        List<String> routes = new ArrayList<>();
        for (Tag i : getRoutes()) {
            routes.add(i.getValue());
        }
        areaMap.put(RealmArea.FIELD_ROUTES, routes);
        return areaMap;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public RealmList<Tag> getSubAreas() {
        return subAreas;
    }

    public void setSubAreas(RealmList<Tag> subAreas) {
        this.subAreas = subAreas;
    }

    public RealmList<Tag> getRoutes() {
        return routes;
    }

    public void setRoutes(RealmList<Tag> routes) {
        this.routes = routes;
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
        if (getParent() != null) {
            builder.append("parent:").append(getParent()).append(" ");
        }
        builder.append("routes:").append(getRoutes().size()).append(" ");
        builder.append("areas:").append(getSubAreas().size());
        return builder.toString();
    }

    public RealmList<Tag> getImages() {
        return images;
    }

    public void setImages(RealmList<Tag> images) {
        this.images = images;
    }
}
