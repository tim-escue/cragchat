package com.cragchat.mobile.model;

/**
 * Created by timde on 11/28/2017.
 */

public interface NewSendRequest {

    String getEntityKey();

    int getPitches();

    int getAttempts();

    String getSendType();

    String getClimbingStyle();

}
