package com.cragchat.mobile.data.local.model;

/**
 * Created by timde on 11/28/2017.
 */

public interface NewRatingRequest {

    String getUserToken();

    String getEntityKey();

    int getStars();

    int getYds();

    String getEntityName();

}
