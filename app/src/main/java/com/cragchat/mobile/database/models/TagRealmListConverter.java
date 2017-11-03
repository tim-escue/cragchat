package com.cragchat.mobile.database.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by timde on 10/19/2017.
 */

public class TagRealmListConverter implements JsonSerializer<RealmList<Tag>>,
        JsonDeserializer<RealmList<Tag>> {

    @Override
    public JsonElement serialize(RealmList<Tag> src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonArray ja = new JsonArray();
        for (Tag tag : src) {
            ja.add(context.serialize(tag));
        }
        return ja;
    }

    @Override
    public RealmList<Tag> deserialize(final JsonElement json, Type typeOfT,
                                      JsonDeserializationContext context)
            throws JsonParseException {
        final RealmList<Tag> tags = new RealmList<>();

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                JsonArray ja = json.getAsJsonArray();
                for (JsonElement je : ja) {
                    Tag tag = realm.createObject(Tag.class);
                    tag.setValue(je.getAsString());
                    tags.add(tag);
                }
            }
        });

        return tags;
    }

}
