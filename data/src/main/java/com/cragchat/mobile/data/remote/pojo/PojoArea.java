package com.cragchat.mobile.data.remote.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.cragchat.mobile.domain.model.Area;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.parent);
        dest.writeStringList(this.subAreas);
        dest.writeStringList(this.routes);
        dest.writeStringList(this.images);
    }

    public PojoArea() {
    }

    protected PojoArea(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.parent = in.readString();
        this.subAreas = in.createStringArrayList();
        this.routes = in.createStringArrayList();
        this.images = in.createStringArrayList();
    }

    public static final Parcelable.Creator<PojoArea> CREATOR = new Parcelable.Creator<PojoArea>() {
        @Override
        public PojoArea createFromParcel(Parcel source) {
            return new PojoArea(source);
        }

        @Override
        public PojoArea[] newArray(int size) {
            return new PojoArea[size];
        }
    };
}
