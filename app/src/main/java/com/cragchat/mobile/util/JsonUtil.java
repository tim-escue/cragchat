package com.cragchat.mobile.util;

import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.model.pojo.PojoComment;
import com.cragchat.mobile.model.pojo.PojoImage;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoSend;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by timde on 10/19/2017.
 */

public class JsonUtil {
    private static final JsonDeserializer<Datable> datableDeserializer = new JsonDeserializer<Datable>() {
        @Override
        public Datable deserialize(JsonElement arg0, Type arg1,
                                   JsonDeserializationContext arg2) throws JsonParseException {
            Gson g = new Gson();
            Datable datable;
            if (arg0.getAsJsonObject().has("comment")) {
                datable = (PojoComment) g.fromJson(arg0, PojoComment.class);
            } else if (arg0.getAsJsonObject().has("attempts")) {
                datable = (PojoSend) g.fromJson(arg0, PojoSend.class);
            } else if (arg0.getAsJsonObject().has("filename")) {
                datable = (PojoImage) g.fromJson(arg0, PojoImage.class);
            } else {
                datable = (PojoRating) g.fromJson(arg0, PojoRating.class);
            }
            return datable;
        }
    };

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
