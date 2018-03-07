package com.cragchat.mobile.domain.model;


import android.os.Parcelable;

import java.util.List;

/**
 * Created by timde on 9/27/2017.
 */

public interface Area extends Parcelable {

    String getName();

    String getLatitude();

    String getLongitude();

    List<String> getSubAreas();

    List<String> getRoutes();

    String getParent();

    String getKey();

    List<String> getImages();

}
