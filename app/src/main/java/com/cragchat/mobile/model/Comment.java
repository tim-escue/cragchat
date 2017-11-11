package com.cragchat.mobile.model;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by timde on 11/10/2017.
 */

public interface Comment {

     String getComment();

     int getScore();

     String getDate();

     String getKey();

     String getEntityId();

     String getParentId();

     int getDepth();

     String getAuthorName();

     List<String> getChildrenIds();

     String getTable();

}
