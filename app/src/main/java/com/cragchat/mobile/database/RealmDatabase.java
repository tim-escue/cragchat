package com.cragchat.mobile.database;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by timde on 9/28/2017.
 */

public class RealmDatabase {

    private static Realm mRealm;

    public static void init() {
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(3)
                .name("cragchat.realm")
                .build());
        Realm.deleteRealm(Realm.getDefaultConfiguration());
        mRealm = Realm.getDefaultInstance();
        /*Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
        realm.close();*/
    }

    public static Realm getRealm() {
        return mRealm;
    }

    public static void close() {
        mRealm.close();
    }
    /*
    public static void updateBatch(String json, Realm realm) {
        try {
            //TODO: Implement Jackson JSON parsing for improved update times
            /*JsonParser parser = jsonFactory.createParser(json);
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                while (parser.nextToken() != JsonToken.END_OBJECT) {

                }
            }
            parser.close();

            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                String collection = array.getString(i);
                updateOrAddObject(collection, realm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        realm.close();
    }

    public static void updateSingle(String json, Realm realm) {
        try {
            updateOrAddObject(json, realm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        realm.close();
    }

    public static void updateOrAddObject(String json, Realm realm) {
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

    public static void updateOrAddRoute(JSONObject object, Realm realm) {
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
                area.setParent(parentObject.getFilename());
            }
        });
    }

    public static void updateOrAddArea(JSONObject object, Realm realm) {
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
                area.setParent(parentObject.getFilename());
            }
        });
    }

    private static void setAreas(RealmArea area, JSONArray areasArray, Realm realm) {
        try {
            RealmList<Tag> areasList = area.getSubAreas();
            areasList.clear();
            if (areasArray != null) {
                for (int i = 0; i < areasArray.length(); i++) {
                    RealmArea subArea = realm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, areasArray.getString(i)).findFirst();
                    if (subArea != null) {
                        areasList.add(new Tag(subArea.getFilename()));
                        subArea.setParent(area.getFilename());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setRoutes(RealmArea area, JSONArray jsonArray, Realm realm) {
        try {
            List<Tag> routesList = area.getRoutes();
            routesList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                RealmRoute route = realm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, jsonArray.getString(i)).findFirst();
                if (route != null) {
                    routesList.add(new Tag(route.getFilename()));
                    route.setParent(area.getFilename());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addChildAreaToParent(@NonNull RealmArea parentArea, RealmArea childArea) {
        List<Tag> parentSubAreas = parentArea.getSubAreas();
        boolean contains = false;
        for (Tag subArea : parentSubAreas) {
            if (subArea.getValue().equalsIgnoreCase(childArea.getFilename())) {
                contains = true;
            }
        }
        if (!contains) {
            parentArea.getSubAreas().add(new Tag(childArea.getFilename()));
        }
    }

    private static void addRouteToArea(@NonNull RealmArea parentArea, RealmRoute route) {
        List<Tag> routes = parentArea.getRoutes();
        boolean contains = false;
        for (Tag subArea : routes) {
            if (subArea.getValue().equalsIgnoreCase(route.getFilename())) {
                contains = true;
            }
        }
        if (!contains) {
            parentArea.getRoutes().add(new Tag(route.getFilename()));
        }
    }

    public static void create(final Context context) {
        Realm realm = Realm.getDefaultInstance();
        System.out.println("logged in successfully as timsqdev");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                System.out.println("Creating ozone and other areas");
                RealmArea ozone = new RealmArea("Ozone", null, "0", "0", null, new RealmList<Tag>(), new RealmList<Tag>());
                ozone = realm.copyToRealm(ozone);
                //RealmArea ozone = realm.where(RealmArea.class).equalTo("name", "Ozone").findFirst();

                Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.walls));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] params = line.split("#");
                    RealmArea newArea = new RealmArea(params[0], params[0] + ozone.getName(), "0", "0", ozone, new RealmList<RealmArea>(), new RealmList<RealmRoute>());
                    newArea = realm.copyToRealm(newArea);
                    ozone.getSubAreas().add(newArea);
                }
                scanner.close();

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
                    RealmRoute route = new RealmRoute(params[0] + parent.getName(), params[0], type, "0", "0", parent.getFilename(), 0, 0);
                    route = realm.copyToRealm(route);
                    parent.getRoutes().add(new Tag(route.getFilename()));
                    ozone.getRoutes().add(new Tag(route.getFilename()));
                }

                System.out.println("Finished copying to realm");
                List<RealmArea> areas = realm.where(RealmArea.class).findAll();
                System.out.println("Areas(" + areas.size() + "): ");
                for (RealmArea area : areas) {
                    System.out.println("\t" + area.getName());
                    for (Tag subArea : area.getSubAreas()) {
                        System.out.println("\t\t" + subArea.getValue());
                    }
                    for (Tag route : area.getRoutes()) {
                        System.out.println("\t\t" + route.getValue());
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
    public static class MyMigration implements RealmMigration {
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

            if(oldVersion == 3) {
                schema.get("RealmArea")
                        .addField("parent_temp", String.class)
                        .transform(new RealmObjectSchema.Function() {
                            @Override
                            public void apply(DynamicRealmObject dynamicRealmObject) {
                                dynamicRealmObject.setString("parent_temp", String.valueOf(dynamicRealmObject.getObject("parent")));
                            }
                        })
                        .removeField("parent")
                        .renameField("parent_temp", "parent")
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
    }*/

}
