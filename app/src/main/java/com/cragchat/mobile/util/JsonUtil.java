package com.cragchat.mobile.util;

import com.google.gson.JsonArray;

/**
 * Created by timde on 10/19/2017.
 */

public class JsonUtil {
    public static String stringArrayToJSon(String[] array) {
        JsonArray jsonStringArray = new JsonArray();
        for (String i : array) {
            jsonStringArray.add(i);
        }
        return jsonStringArray.toString();
    }
}
