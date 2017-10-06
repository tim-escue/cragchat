package com.cragchat.mobile.authentication;

import com.cragchat.mobile.database.Database;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by timde on 9/26/2017.
 */

public class RealmAuthenticationProvider implements AuthenticationProvider {

    private static String AUTHENTICATION_URL = "http://ec2-52-32-143-228.us-west-2.compute.amazonaws.com:9080/auth";
    private static String REALM_URL = "realm://ec2-52-32-143-228.us-west-2.compute.amazonaws.com:9080/ozone";

    private static SyncUser mUser;

    public static SyncUser getCurrentUser() {
        return mUser;
    }

    @Override
    public void logIn(String name, String password, final AuthenticationCallback callback) {
        authenticate(name, password, false, callback);
    }

    @Override
    public void register(String name, String password, AuthenticationCallback callback) {
        authenticate(name, password, true, callback);
    }

    private void authenticate(String name, String password, boolean register, final AuthenticationCallback callback) {
        SyncCredentials credentials = SyncCredentials.usernamePassword(name, password, register);
        SyncUser.loginAsync(credentials, AUTHENTICATION_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser syncUser) {
                mUser = syncUser;
                AuthenticatedUser user = new AuthenticatedUser(syncUser.getIdentity(), syncUser.toJson());
                callback.onAuthenticateSuccess(user);
                setDefaultSyncConfiguration();
            }

            @Override
            public void onError(ObjectServerError objectServerError) {
                callback.onAuthenticateFailed();
            }
        });
    }

    @Override
    public AuthenticatedUser getAuthenticatedUser(String token) {
        mUser = SyncUser.fromJson(token);
        setDefaultSyncConfiguration();
        return new AuthenticatedUser(mUser.getIdentity(), token);
    }

    private void setDefaultSyncConfiguration() {
        SyncConfiguration configuration = new SyncConfiguration.Builder(mUser, REALM_URL).build();
        Realm.setDefaultConfiguration(configuration);
    }

    @Override
    public void logout() {
        mUser.logout();
        mUser = null;
    }

    //used to initially populate realm
       /* Realm.deleteRealm(Realm.getDefaultConfiguration());
            final Realm m = Realm.getDefaultInstance();


        m.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });

        final RealmArea ozone = new RealmArea(area.getName(), area.getName(), area.getLatitude(), area.getLongitude(), null,
                new RealmList<RealmArea>(), new RealmList<RealmRoute>());
        m.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                m.copyToRealm(ozone);
            }
        });
        final RealmArea ozoneNew = m.where(RealmArea.class).equalTo("name", "Ozone").findFirst();
        for (final LegacyArea legacyArea : area.getSubAreas()) {
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
        for (final Displayable disp : area.getRoutes()) {
                final RealmRoute route = m.where(RealmRoute.class).equalTo("name", disp.getName()).findFirst();
                m.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        ozoneNew.getRoutes().add(route);
                    }
                });
        }
        m.close();*/

}
