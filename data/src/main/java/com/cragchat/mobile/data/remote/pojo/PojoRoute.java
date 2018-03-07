package com.cragchat.mobile.data.remote.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.cragchat.mobile.domain.model.Route;

import java.util.List;

/**
 * Created by timde on 11/9/2017.
 */

public class PojoRoute implements Route {

    private String key;
    private String name;
    private String type;
    private String latitude;
    private String longitude;
    private int yds;
    private double stars;
    private String parent;
    private String sends;
    private String ratings;
    private List<String> images;

    public Route.Type getRouteType() {
        return type.equalsIgnoreCase("Trad") ? Type.Trad :
                type.equalsIgnoreCase("Sport") ? Type.Sport : Type.Mixed;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        dest.writeString(this.type);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeInt(this.yds);
        dest.writeDouble(this.stars);
        dest.writeString(this.parent);
        dest.writeString(this.sends);
        dest.writeString(this.ratings);
        dest.writeStringList(this.images);
    }

    public PojoRoute() {
    }

    protected PojoRoute(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.yds = in.readInt();
        this.stars = in.readDouble();
        this.parent = in.readString();
        this.sends = in.readString();
        this.ratings = in.readString();
        this.images = in.createStringArrayList();
    }

    public static final Parcelable.Creator<PojoRoute> CREATOR = new Parcelable.Creator<PojoRoute>() {
        @Override
        public PojoRoute createFromParcel(Parcel source) {
            return new PojoRoute(source);
        }

        @Override
        public PojoRoute[] newArray(int size) {
            return new PojoRoute[size];
        }
    };
}
