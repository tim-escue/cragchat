package com.cragchat.mobile.domain.model;

import android.os.Parcelable;

/**
 * Created by timde on 11/13/2017.
 */

public interface Image extends Parcelable {

    String getEntityKey();

    String getAuthorName();

    String getCaption();

    String getFilename();

    String getDate();

    String getEntityName();

}
