package com.cragchat.mobile.data.util;

import com.cragchat.mobile.domain.model.Datable;
import com.cragchat.mobile.data.remote.pojo.PojoComment;
import com.cragchat.mobile.data.remote.pojo.PojoImage;
import com.cragchat.mobile.data.remote.pojo.PojoRating;
import com.cragchat.mobile.data.remote.pojo.PojoSend;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * Created by timde on 10/19/2017.
 */

public class JsonUtil {

    private static Gson gson = new Gson();

    private static final JsonDeserializer<Datable> datableDeserializer =
            (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
                if (json.getAsJsonObject().has("comment")) {
                    return gson.fromJson(json, PojoComment.class);
                } else if (json.getAsJsonObject().has("attempts")) {
                    return gson.fromJson(json, PojoSend.class);
                } else if (json.getAsJsonObject().has("filename")) {
                    return gson.fromJson(json, PojoImage.class);
                } else {
                    return gson.fromJson(json, PojoRating.class);
                }
            };


    public static final <T> JsonDeserializer<T> getDeserializer(Class<T> clazz) {
        return (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> gson.fromJson(json, clazz);
    }

    public static String stringArrayToJSon(String[] array) {
        JsonArray jsonStringArray = new JsonArray();
        for (String i : array) {
            jsonStringArray.add(i);
        }
        return jsonStringArray.toString();
    }

    public static JsonDeserializer<Datable> getDatableDeserializer() {
        return datableDeserializer;
    }
}
