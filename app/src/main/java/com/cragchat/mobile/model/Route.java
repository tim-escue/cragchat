package com.cragchat.mobile.model;

import java.util.HashMap;

/**
 * Created by timde on 9/27/2017.
 */

public interface Route {

    String getKey();

    String getName();

    String getParent();

    String getLongitude();

    String getLatitude();

    int getYds();

    double getStars();

    String getType();

    HashMap<String, Object> getMap();
}
