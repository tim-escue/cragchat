package com.cragchat.mobile.repository.local;

import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.model.pojo.PojoArea;
import com.cragchat.mobile.model.pojo.PojoComment;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoRoute;
import com.cragchat.mobile.model.realm.RealmArea;
import com.cragchat.mobile.model.realm.RealmComment;
import com.cragchat.mobile.model.realm.RealmRating;
import com.cragchat.mobile.model.realm.RealmRoute;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

/**
 * Created by timde on 9/28/2017.
 */

public class RealmDatabase implements CragChatDatabase {

    private Realm mRealm;

    public RealmDatabase() {
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

    public Realm getRealm() {
        return mRealm;
    }

    public void close() {
        mRealm.close();
    }

    public List getQueryMatches(String query) {
        Realm realm = getRealm();
        List results = realm.copyFromRealm(realm.where(RealmArea.class).contains(RealmArea.FIELD_NAME, query, Case.INSENSITIVE).findAll());
        results.addAll(realm.copyFromRealm(realm.where(RealmRoute.class).contains(RealmRoute.FIELD_NAME, query, Case.INSENSITIVE).findAll()));
        return results;
    }

    @Override
    public List<Route> getRoutes(String[] routeIds) {
        List<Route> routes = new ArrayList<>();
        routes.addAll(mRealm.where(RealmRoute.class).in(RealmRoute.FIELD_KEY, routeIds).findAll());
        return routes;
    }

    @Override
    public List<Area> getAreas(String[] areaIds) {
        List<Area> areas = new ArrayList<>();
        areas.addAll(mRealm.where(RealmArea.class).in(RealmArea.FIELD_KEY, areaIds).findAll());
        return areas;
    }

    @Override
    public List<Rating> getRatings(String entityKey) {
        List<Rating> ratings = new ArrayList<>();
        ratings.addAll(mRealm.where(RealmRating.class).equalTo(RealmRating.FIELD_ENTITY_KEY, entityKey).findAll());
        return ratings;
    }

    @Override
    public RealmArea getArea(String areaKey) {
        return mRealm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, areaKey).findFirst();
    }

    @Override
    public RealmArea getAreaByName(String areaName) {
        return mRealm.where(RealmArea.class).equalTo(RealmArea.FIELD_NAME, areaName).findFirst();
    }

    @Override
    public void update(final PojoRoute route) {
        insertOrUpdate(RealmRoute.from(route));

    }

    @Override
    public void updateRoutes(final List<PojoRoute> routes) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoRoute route : routes) {
                    realm.insertOrUpdate(RealmRoute.from(route));
                }
            }
        });
    }

    @Override
    public void update(final PojoArea area) {
        insertOrUpdate(RealmArea.from(area));
    }

    @Override
    public void update(final PojoRating rating) {
        insertOrUpdate(RealmRating.from(rating));
    }

    @Override
    public void update(PojoComment comment) {
        insertOrUpdate(RealmComment.from(comment));
    }

    @Override
    public void updateRatings(final List<PojoRating> ratings) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoRating rating : ratings) {
                    realm.insertOrUpdate(RealmRating.from(rating));
                }
            }
        });
    }

    @Override
    public void updateAreas(final List<PojoArea> areas) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoArea i : areas) {
                    realm.insertOrUpdate(RealmArea.from(i));
                }
            }
        });
    }

    private void insertOrUpdate(final RealmObject object) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(object);
            }
        });
    }


    /*
    public static void create(final Context context) {
        Realm realm = Realm.getDefaultInstance();
        System.out.println("logged in successfully as timsqdev");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                System.out.println("Creating ozone and other areas");
                RealmArea ozone = new RealmArea("Ozone", null, "0", "0", null, new RealmList<String>(), new RealmList<String>());
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
                    parent.getRoutes().add(new String(route.getFilename()));
                    ozone.getRoutes().add(new String(route.getFilename()));
                }

                System.out.println("Finished copying to realm");
                List<RealmArea> areas = realm.where(RealmArea.class).findAll();
                System.out.println("Areas(" + areas.size() + "): ");
                for (RealmArea area : areas) {
                    System.out.println("\t" + area.getName());
                    for (String subArea : area.getSubAreas()) {
                        System.out.println("\t\t" + subArea.getValue());
                    }
                    for (String route : area.getRoutes()) {
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
    }*/

}
