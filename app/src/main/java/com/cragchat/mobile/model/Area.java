package com.cragchat.mobile.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by timde on 9/27/2017.
 */

public interface Area {

    String getKey();
    String getName();
    Area getParent();
    List<? extends Area> getSubAreas();
    List<? extends Route> getRoutes();
    String getLongitude();
    String getLatitude();
    HashMap<String, Object> getMap();

}
