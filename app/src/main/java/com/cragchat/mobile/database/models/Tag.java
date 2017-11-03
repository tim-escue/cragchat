package com.cragchat.mobile.database.models;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class Tag extends RealmObject {
    private String value;

    public Tag() {
    }

    public Tag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}