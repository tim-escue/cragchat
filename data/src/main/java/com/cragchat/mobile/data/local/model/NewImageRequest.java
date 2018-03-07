package com.cragchat.mobile.data.local.model;

/**
 * Created by timde on 11/28/2017.
 */

public interface NewImageRequest {
    String getCaptionString();

    String getEntityKey();

    String getEntityType();

    String getFilePath();

    String getEntityName();
}
