package com.cragchat.mobile.database;

import io.realm.RealmObject;

public class RelmTest extends RealmObject {

        private String name;

        public RelmTest() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }