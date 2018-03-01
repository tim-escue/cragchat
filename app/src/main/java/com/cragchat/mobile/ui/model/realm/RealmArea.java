package com.cragchat.mobile.ui.model.realm;

import android.os.Parcel;

import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.pojo.PojoArea;
import com.cragchat.mobile.util.RealmUtil;

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
    private RealmList<String> subAreas;
    private RealmList<String> routes;
    private RealmList<String> images;

    //Required by Realm
    public RealmArea() {
    }

    public RealmArea(String name, String key,
                     String latitude, String longitude,
                     String parent, RealmList<String> subAreas,
                     RealmList<String> routes, RealmList<String> images) {
        this.setName(name);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setSubAreas(subAreas);
        this.setRoutes(routes);
        this.setParent(parent);
        this.setKey(key);
        this.setImages(images);
    }

    public static RealmArea from(Area area) {

        return new RealmArea(area.getName(), area.getKey(),
                area.getLatitude(), area.getLongitude(),
                area.getParent(), RealmUtil.convertListToRealmList(area.getSubAreas()),
                RealmUtil.convertListToRealmList(area.getRoutes()),
                RealmUtil.convertListToRealmList(area.getImages())
        );
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

    public RealmList<String> getSubAreas() {
        return subAreas;
    }

    public void setSubAreas(RealmList<String> subAreas) {
        this.subAreas = subAreas;
    }

    public RealmList<String> getRoutes() {
        return routes;
    }

    public void setRoutes(RealmList<String> routes) {
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
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.parent);
        dest.writeStringList(this.subAreas);
        dest.writeStringList(this.routes);
        dest.writeStringList(this.images);
    }

    protected RealmArea(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.parent = in.readString();
        this.subAreas = RealmUtil.convertListToRealmList(in.createStringArrayList());
        this.routes = RealmUtil.convertListToRealmList(in.createStringArrayList());
        this.images = RealmUtil.convertListToRealmList(in.createStringArrayList());
    }

    public static final Creator<RealmArea> CREATOR = new Creator<RealmArea>() {
        @Override
        public RealmArea createFromParcel(Parcel source) {
            return new RealmArea(source);
        }

        @Override
        public RealmArea[] newArray(int size) {
            return new RealmArea[size];
        }
    };
}
