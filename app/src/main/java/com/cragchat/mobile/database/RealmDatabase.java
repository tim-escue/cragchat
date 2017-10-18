package com.cragchat.mobile.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cragchat.mobile.R;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;
import com.cragchat.networkapi.CragChatApi;
import com.cragchat.networkapi.NetworkApi;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Scanner;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by timde on 9/28/2017.
 */

public class RealmDatabase implements CragChatDatasource {

    private JsonFactory jsonFactory;

    public RealmDatabase(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(3)
                .name("cragchat.realm")
                .migration(new MyMigration())
                .build());
        jsonFactory = new JsonFactory();
       /* List<? extends Area> areas = Realm.getDefaultInstance().where(RealmArea.class).findAll();
        for (Area i : areas) {
            System.out.println("Area:" + i.getKey());
        }*/
        // create(context);
        //
        /*Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });/*
        create(context);
        NetworkApi.addArea("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OWRlNzMzYTZkYjE4OTI3YmFmYTJhODIifQ.Ib_REb18GU3KXdc5eQYhgppJFald4bs7NK0lXZjpzLg",
                new RealmArea("Ozone", "", "0", "0", null, new RealmList<RealmArea>(), new RealmList<RealmRoute>()));

        create(context);
        for (Route i : Realm.getDefaultInstance().where(RealmRoute.class).findAll())
        NetworkApi.addRoute("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OWRlNzMzYTZkYjE4OTI3YmFmYTJhODIifQ.Ib_REb18GU3KXdc5eQYhgppJFald4bs7NK0lXZjpzLg",
                i);

        for (Area i : Realm.getDefaultInstance().where(RealmArea.class).findAll()) {
            if (!i.getName().equals("Ozone"))
            NetworkApi.addArea("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OWRlNzMzYTZkYjE4OTI3YmFmYTJhODIifQ.Ib_REb18GU3KXdc5eQYhgppJFald4bs7NK0lXZjpzLg",
                    i);
        }*/
    }

    @Override
    public Area getArea(String key) {
        Realm realm = Realm.getDefaultInstance();
        Area area = realm.copyFromRealm(realm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, key).findFirst());
        realm.close();
        return area;
    }

    @Override
    public List<? extends Area> getAllAreas() {
        Realm realm = Realm.getDefaultInstance();
        List<? extends Area> list = realm.copyFromRealm(realm.where(RealmArea.class).findAll());
        realm.close();
        return list;
    }

    @Override
    public List<? extends Route> getAllRoutes() {
        Realm realm = Realm.getDefaultInstance();
        List<? extends Route> list = realm.copyFromRealm(realm.where(RealmRoute.class).findAll());
        realm.close();
        return list;
    }

    @Override
    public void updateBatch(String json) {
        Realm realm = Realm.getDefaultInstance();
        try {
            JsonParser parser = jsonFactory.createParser(json);
            JsonToken token = parser.nextToken();

            /*
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                String collection = array.getString(i);
                updateOrAddObject(collection, realm);
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        realm.close();
    }

    @Override
    public void updateSingle(String json) {
        Realm realm = Realm.getDefaultInstance();
        try {
                updateOrAddObject(json, realm);
                System.out.println("UPDATING: " + json);

        } catch (Exception e) {
            e.printStackTrace();
        }
        realm.close();
    }

    public void updateOrAddObject(String json, Realm realm) {
        JSONObject object= null;
        String type = null;
        try {
           object = new JSONObject(json);
            type = object.getString("object_type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type.equalsIgnoreCase(CragChatApi.TYPE_AREA)) {
            updateOrAddArea(object, realm);
        } else {
            updateOrAddRoute(object, realm);
        }
    }

    public void updateOrAddRoute(JSONObject object, Realm realm) {
        final String key;
        final String name;
        final String latitude;
        final String longitude;
        final String parent;
        final String type;
        final int yds;
        final double stars;
        try {
            key = object.getString(RealmArea.FIELD_KEY);
            name = object.getString(RealmArea.FIELD_NAME);
            type = object.getString(RealmRoute.FIELD_TYPE);
            yds = Integer.parseInt(object.getString(RealmRoute.FIELD_YDS));
            stars = Double.parseDouble(object.getString(RealmRoute.FIELD_STARS));
            latitude = object.getString(RealmArea.FIELD_LATITUDE);
            longitude = object.getString(RealmArea.FIELD_LONGITUDE);
            parent = object.getString(RealmArea.FIELD_PARENT);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmRoute area = realm.where(RealmRoute.class).equalTo(RealmArea.FIELD_KEY, key).findFirst();
                if (area == null) {
                    area = realm.createObject(RealmRoute.class, key);
                }
                area.setName(name);
                area.setLatitude(latitude);
                area.setLongitude(longitude);
                area.setType(type);
                area.setYds(yds);
                area.setStars(stars);
                RealmArea parentObject = realm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, parent).findFirst();
                if (parentObject != null) {
                    addRouteToArea(parentObject, area);
                }
                area.setParent(parentObject);
            }
        });
    }

    public void updateOrAddArea(JSONObject object, Realm realm) {
        final String key;
        final String name;
        final String latitude;
        final String longitude;
        final String parent;
        final JSONArray routesArray;
        final JSONArray areasArray;
        try {
            key = object.getString(RealmArea.FIELD_KEY);
            name = object.getString(RealmArea.FIELD_NAME);
            latitude = object.getString(RealmArea.FIELD_LATITUDE);
            longitude = object.getString(RealmArea.FIELD_LONGITUDE);
            parent = object.getString(RealmArea.FIELD_PARENT);
            routesArray = object.getJSONArray(RealmArea.FIELD_ROUTES);
            areasArray = object.getJSONArray(RealmArea.FIELD_SUBAREAS);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmArea area = realm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, key).findFirst();
                if (area == null) {
                    area = realm.createObject(RealmArea.class, key);
                }
                area.setName(name);
                area.setLatitude(latitude);
                area.setLongitude(longitude);
                setAreas(area, areasArray, realm);
                setRoutes(area, routesArray, realm);
                RealmArea parentObject = realm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, parent).findFirst();
                if (parentObject != null) {
                    addChildAreaToParent(parentObject, area);
                }
                area.setParent(parentObject);
            }
        });
    }

    private void setAreas(RealmArea area, JSONArray areasArray, Realm realm) {
        try {
            List<RealmArea> areasList = area.getSubAreas();
            areasList.clear();
            if (areasArray != null) {
                for (int i = 0; i < areasArray.length(); i++) {
                    RealmArea subArea = realm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, areasArray.getString(i)).findFirst();
                    if (subArea != null) {
                        areasList.add(subArea);
                        subArea.setParent(area);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRoutes(RealmArea area, JSONArray jsonArray, Realm realm) {
        try {
            List<RealmRoute> routesList = area.getRoutes();
            routesList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                RealmRoute route = realm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, jsonArray.getString(i)).findFirst();
                if (route != null) {
                    routesList.add(route);
                    route.setParent(area);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addChildAreaToParent(@NonNull RealmArea parentArea, RealmArea childArea) {
        List<RealmArea> parentSubAreas = parentArea.getSubAreas();
        boolean contains = false;
        for (RealmArea subArea : parentSubAreas) {
            if (subArea.getKey().equalsIgnoreCase(childArea.getKey())) {
                contains = true;
            }
        }
        if (!contains) {
            parentArea.getSubAreas().add(childArea);
        }
    }

    private void addRouteToArea(@NonNull RealmArea parentArea, RealmRoute route) {
        List<RealmRoute> routes = parentArea.getRoutes();
        boolean contains = false;
        for (RealmRoute subArea : routes) {
            if (subArea.getKey().equalsIgnoreCase(route.getKey())) {
                contains = true;
            }
        }
        if (!contains) {
            parentArea.getRoutes().add(route);
        }
    }

    @Override
    public Route getRoute(String key) {
        Realm realm = Realm.getDefaultInstance();
        Route area = realm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, key).findFirst();
        realm.close();
        return area;
    }

    public List<? extends Area> getAreas() {
        Realm realm = Realm.getDefaultInstance();
        List<? extends Area> areas = realm.copyFromRealm(realm.where(RealmArea.class).findAll());
        realm.close();
        return areas;
    }

    public static void create(final Context context) {
        Realm realm = Realm.getDefaultInstance();
        System.out.println("logged in successfully as timsqdev");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                System.out.println("Creating ozone and other areas");
                RealmArea ozone = new RealmArea("Ozone", null, "0", "0", null, new RealmList<RealmArea>(), new RealmList<RealmRoute>());
                ozone = realm.copyToRealm(ozone);
                //RealmArea ozone = realm.where(RealmArea.class).equalTo("name", "Ozone").findFirst();

                /*Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.walls));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] params = line.split("#");
                    RealmArea newArea = new RealmArea(params[0], params[0] + ozone.getName(), "0", "0", ozone, new RealmList<RealmArea>(), new RealmList<RealmRoute>());
                    newArea = realm.copyToRealm(newArea);
                    ozone.getSubAreas().add(newArea);
                }
                scanner.close();*/

                System.out.println("Creating routes");
                Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.routes));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] params = line.split("#");

                    String type = params[3];
                    if (type.contains("Top Rope")) {
                        type = "Trad"; // only one rout ehas tope rope and its other type is trad
                    }

                    RealmArea parent = realm.where(RealmArea.class).equalTo("name", params[2]).findFirst();
                    RealmRoute route = new RealmRoute(params[0] + parent.getName(), params[0], type, "0", "0", parent);
                    route = realm.copyToRealm(route);
                    parent.getRoutes().add(route);
                    ozone.getRoutes().add(route);
                }

                System.out.println("Finished copying to realm");
                List<RealmArea> areas = realm.where(RealmArea.class).findAll();
                System.out.println("Areas(" + areas.size() + "): ");
                for (RealmArea area : areas) {
                    System.out.println("\t" + area.getName());
                    for (RealmArea subArea : area.getSubAreas()) {
                        System.out.println("\t\t" + subArea.getName());
                    }
                    for (RealmRoute route : area.getRoutes()) {
                        System.out.println("\t\t" + route.getName());
                    }
                }
                List<RealmRoute> routes = realm.where(RealmRoute.class).findAll();
                System.out.println("Routes(" + routes.size() + "):  ");
                for (RealmRoute route : routes) {
                    System.out.println("\t" + route.getName());
                }
            }
        });
    }

    // Example migration adding a new class
    public class MyMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();
            Log.d("oldversion", oldVersion + "");
            // Migrate to version 1: Add a new class.
            // Example:
            // public Person extends RealmObject {
            //     private String name;
            //     private int age;
            //     // getters and setters left out for brevity
            // }
            if (oldVersion == 2) {
                schema.get("RealmRoute")
                        .addField("longitude_temp", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject dynamicRealmObject) {
                                dynamicRealmObject.setString("longitude_temp", String.valueOf(dynamicRealmObject.getDouble("longitude")));
                            }
                        })
                        .removeField("longitude")
                        .renameField("longitude_temp", "longitude")
                        .addField("latitude_temp", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject dynamicRealmObject) {
                                dynamicRealmObject.setString("latitude_temp", String.valueOf(dynamicRealmObject.getDouble("latitude")));
                            }
                        })
                        .removeField("latitude")
                        .renameField("latitude_temp", "latitude");
                oldVersion++;
            }

            // Migrate to version 2: Add a primary key + object references
            // Example:
            // public Person extends RealmObject {
            //     private String name;
            //     @PrimaryKey
            //     private int age;
            //     private Dog favoriteDog;
            //     private RealmList<Dog> dogs;
            //     // getters and setters left out for brevity

        }
    }

}
