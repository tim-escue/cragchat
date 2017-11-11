package com.cragchat.mobile.model.pojo;

import com.cragchat.mobile.model.Area;

import java.util.List;

/**
 * Created by timde on 11/9/2017.
 */

public class PojoArea implements Area {

    private String key;
    private String name;
    private String latitude;
    private String longitude;
    private String parent;
    private List<String> subAreas;
    private List<String> routes;
    private List<String> images;

    public PojoArea() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<String> getSubAreas() {
        return subAreas;
    }

    public void setSubAreas(List<String> subAreas) {
        this.subAreas = subAreas;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
