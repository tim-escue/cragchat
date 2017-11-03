package com.cragchat.mobile.model;

import com.cragchat.mobile.database.models.Tag;

import java.util.HashMap;

import io.realm.RealmList;

/**
 * Created by timde on 9/27/2017.
 */

public interface Area {

    String getKey();

    String getName();

    String getParent();

    RealmList<Tag> getSubAreas();

    RealmList<Tag> getRoutes();

    String getLongitude();

    String getLatitude();

    HashMap<String, Object> getMap();

}
