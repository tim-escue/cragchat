package com.cragchat.mobile.model.realm;

import io.realm.RealmObject;

/**
 * Created by timde on 11/13/2017.
 */

public interface RealmDerivable {
    RealmObject from(Object object);
}