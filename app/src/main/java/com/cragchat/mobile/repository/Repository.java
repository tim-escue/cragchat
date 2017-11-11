package com.cragchat.mobile.repository;

import android.content.Context;

import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.model.pojo.PojoArea;
import com.cragchat.mobile.model.pojo.PojoComment;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoRoute;
import com.cragchat.mobile.model.realm.RealmRating;
import com.cragchat.mobile.network.Network;
import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.local.RealmDatabase;
import com.cragchat.mobile.repository.remote.CragChatRestApi;
import com.cragchat.mobile.repository.remote.ErrorHandlingObserverable;
import com.cragchat.mobile.repository.remote.RetroFitRestApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by timde on 11/8/2017.
 */

public class Repository {

    private static CragChatDatabase localDatabase;
    private static CragChatRestApi networkApi;

    public static void init(Context context) {
        Realm.init(context);
        localDatabase = new RealmDatabase();
        networkApi = new RetroFitRestApi();
    }

    public static List getQueryMatches(String query, Context context) {
        if (context != null) {
            if (Network.isConnected(context)) {
                networkApi.getAreasContaining(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ErrorHandlingObserverable<ResponseBody>() {
                            @Override
                            public void onSuccess(ResponseBody object) {
                                long start = System.currentTimeMillis();
                                Gson gson = new Gson();
                                Type areaType = new TypeToken<PojoArea>() {
                                }.getType();
                                Type routeType = new TypeToken<PojoRoute>() {
                                }.getType();
                                try {
                                    JSONArray array = new JSONArray(object.string());
                                    for (int i = 0; i < array.length(); i++) {
                                        String result = array.getString(i);
                                        System.out.println("response:" + result);
                                        if (result.contains("routes")) {
                                            PojoArea area = gson.fromJson(result, areaType);
                                            localDatabase.update(area);
                                        } else {
                                            PojoRoute route = gson.fromJson(result, routeType);
                                            localDatabase.update(route);
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }
        return localDatabase.getQueryMatches(query);
    }

    public static void addComment(String userToken,String comment,String entityKey,String table) {
        networkApi.postComment(userToken, comment, entityKey, table)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandlingObserverable<PojoComment>() {
                    @Override
                    public void onSuccess(PojoComment object) {
                        localDatabase.update(object);
                    }
                });
    }

    public static void addRating(String userToken, int stars, int yds, String entityKey) {
        networkApi.postRating(userToken, stars, yds, entityKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandlingObserverable<PojoRating>() {
                    @Override
                    public void onSuccess(PojoRating object) {
                        localDatabase.update(object);
                    }
                });
    }

    public static List<Rating> getRatings(String entityKey) {
        networkApi.getRatings(entityKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandlingObserverable<List<PojoRating>>() {
                    @Override
                    public void onSuccess(List<PojoRating> object) {
                        localDatabase.updateRatings(object);
                    }
                });
        return localDatabase.getRatings(entityKey);
    }

    public static Area getArea(String areaKey, @Nullable Context context) {
        if (context != null) {
            if (Network.isConnected(context)) {
                networkApi.getArea(areaKey, null)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ErrorHandlingObserverable<PojoArea>() {
                            @Override
                            public void onSuccess(final PojoArea areas) {
                                localDatabase.update(areas);
                            }
                        });
            }
        }
        return localDatabase.getArea(areaKey);
    }

    public static Area getAreaByName(String areaName, @Nullable Context context) {
        if (context != null) {
            if (Network.isConnected(context)) {
                networkApi.getArea(null, areaName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ErrorHandlingObserverable<PojoArea>() {
                            @Override
                            public void onSuccess(final PojoArea areas) {
                                localDatabase.update(areas);
                            }
                        });
            }
        }
        return localDatabase.getAreaByName(areaName);
    }

    public static List<Route> getRoutes(String[] routeIds, Context context) {
        if (context != null) {
            if (Network.isConnected(context)) {
                networkApi.getRoutes(routeIds)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ErrorHandlingObserverable<List<PojoRoute>>() {
                            @Override
                            public void onSuccess(final List<PojoRoute> routes) {
                                localDatabase.updateRoutes(routes);
                            }
                        });
            }
        }
        return localDatabase.getRoutes(routeIds);
    }

    public static List<Area> getAreas(String[] areaIds, Context context) {
        if (context != null) {
            if (Network.isConnected(context)) {
                networkApi.getAreas(areaIds)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ErrorHandlingObserverable<List<PojoArea>>() {
                            @Override
                            public void onSuccess(List<PojoArea> object) {
                                localDatabase.updateAreas(object);
                            }
                        });
            }
        }
        return localDatabase.getAreas(areaIds);
    }


}
