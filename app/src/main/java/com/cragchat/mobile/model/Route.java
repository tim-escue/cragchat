package com.cragchat.mobile.model;

/**
 * Created by timde on 9/27/2017.
 */

public interface Route {

    String getKey();
    String getName();
    Area getParent();
    double getLongitude();
    double getLatitude();
    int getYds();
    double getStars();
    String getType();
}
