package com.cragchat.mobile.model;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by timde on 9/27/2017.
 */

public interface Route extends Parcelable {

    String getKey();

    String getName();

    String getParent();

    String getLongitude();

    String getLatitude();

    int getYds();

    double getStars();

    String getType();

    List<String> getImages();

    String getRatings();

    String getSends();

    Type getRouteType();

    enum Type {
        Sport,
        Trad,
        Mixed
    }

}
