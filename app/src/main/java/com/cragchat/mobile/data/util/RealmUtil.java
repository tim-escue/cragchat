package com.cragchat.mobile.data.util;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by timde on 11/10/2017.
 */

public class RealmUtil {

    public static RealmList<String> convertListToRealmList(List<String> strings) {
        return new RealmList<>(strings.toArray(new String[strings.size()]));
    }

}
