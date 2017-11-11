package com.cragchat.mobile.util;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by timde on 11/10/2017.
 */

public class RealmUtil {

    public static RealmList<String> convertListToRealmList(List<String> strings) {
        String[] subAreas = new String[strings.size()];
        for (int i = 0; i < subAreas.length; i++) {
            subAreas[i] = new String(strings.get(i));
        }
        return new RealmList<>(subAreas);
    }

}
