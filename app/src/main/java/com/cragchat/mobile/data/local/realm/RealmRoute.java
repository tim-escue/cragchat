package com.cragchat.mobile.data.local.realm;

import android.os.Parcel;

import com.cragchat.mobile.domain.model.Route;
import com.cragchat.mobile.data.util.RealmUtil;

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
    private RealmList<String> images;

    public RealmRoute() {
    }

    public RealmRoute(String key, String name,
                      String type, String latitude,
                      String longitude, int yds,
                      double stars, String parent,
                      String sends, String ratings,
                      RealmList<String> images) {
        setKey(key);
        setName(name);
        setType(type);
        setLatitude(latitude);
        setLongitude(longitude);
        setYds(yds);
        setSends(sends);
        setStars(stars);
        setParent(parent);
        setRatings(ratings);
        setImages(images);
    }

    public static RealmRoute from(Route route) {
        return new RealmRoute(route.getKey(), route.getName(),
                route.getType(), route.getLatitude(),
                route.getLongitude(), route.getYds(),
                route.getStars(), route.getParent(),
                route.getSends(), route.getRatings(),
                RealmUtil.convertListToRealmList(route.getImages()));
    }

    public Route.Type getRouteType() {
        return type.equalsIgnoreCase("Trad") ? Type.Trad :
                type.equalsIgnoreCase("Sport") ? Type.Sport : Type.Mixed;
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

    public RealmList<String> getImages() {
        return images;
    }

    public void setImages(RealmList<String> images) {
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

    protected RealmRoute(Parcel in) {
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
        this.images = RealmUtil.convertListToRealmList(in.createStringArrayList());
    }

    public static final Creator<RealmRoute> CREATOR = new Creator<RealmRoute>() {
        @Override
        public RealmRoute createFromParcel(Parcel source) {
            return new RealmRoute(source);
        }

        @Override
        public RealmRoute[] newArray(int size) {
            return new RealmRoute[size];
        }
    };
}