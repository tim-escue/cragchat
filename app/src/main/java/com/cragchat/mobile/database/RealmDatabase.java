package com.cragchat.mobile.database;

import android.content.Context;

import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;

import io.realm.Realm;

/**
 * Created by timde on 9/28/2017.
 */

public class RealmDatabase implements CragChatDatasource {

    public RealmDatabase(Context context) {
        Realm.init(context);
    }

    @Override
    public Area getArea(String key) {
        Realm realm = Realm.getDefaultInstance();
        Area area = realm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, key).findFirst();
        realm.close();
        return area;
    }

    @Override
    public Route getRoute(String key) {
        Realm realm = Realm.getDefaultInstance();
        Route area = realm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, key).findFirst();
        realm.close();
        return area;
    }

     /*
        final Realm m = Realm.getDefaultInstance();


        LegacyArea legacyOzone = (LegacyArea) LocalDatabase.getInstance(this).findExact("Ozone");
        legacyOzone.loadStatistics(this);
        final RealmArea ozone = new RealmArea(legacyOzone.getName(), legacyOzone.getName(), legacyOzone.getLatitude(), legacyOzone.getLongitude(), null,
                new RealmList<RealmArea>(), new RealmList<RealmRoute>());
        m.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(ozone);
            }
        });
        final RealmArea ozoneNew = m.where(RealmArea.class).equalTo("name", "Ozone").findFirst();
        for (final LegacyArea legacyArea : legacyOzone.getSubAreas()) {
            legacyArea.loadStatistics(this);

            m.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final RealmArea newArea = new RealmArea(legacyArea.getName(), legacyArea.getName() + ozoneNew.getName(), legacyArea.getLatitude(), legacyArea.getLongitude(), ozoneNew,
                            new RealmList<RealmArea>(), new RealmList<RealmRoute>());
                    RealmArea created = m.copyToRealm(newArea);
                    ozoneNew.getSubAreas().add(created);
                }
            });
            final RealmArea newArea = m.where(RealmArea.class).equalTo("name", legacyArea.getName()).findFirst();


            for (Displayable disp : legacyArea.getRoutes()) {
                final LegacyRoute route = (LegacyRoute) disp;
                m.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmRoute r = realm.createObject(RealmRoute.class, route.getName() + newArea.getName());
                        r.setName(route.getName());
                        r.setLatitude(route.getLatitude());
                        r.setLongitude(route.getLongitude());
                        r.setType(route.getType());
                        r.setStars(route.getStars(AreaActivity.this));
                        r.setYds(route.getYds(AreaActivity.this));
                        r.setParent(newArea);
                        newArea.getRoutes().add(r);
                    }
                });
            }
        }
        for (final Displayable disp : legacyOzone.getRoutes()) {
            final RealmRoute route = m.where(RealmRoute.class).equalTo("name", disp.getName()).findFirst();
            m.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ozoneNew.getRoutes().add(route);
                }
            });
        }

        for (RealmArea ma : m.where(RealmArea.class).findAll()) {
            Log.d("AREA:", ma.toString());
        }
        m.close();
     */
}
