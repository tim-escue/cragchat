package com.cragchat.mobile.repository.remote;

import android.content.Context;

import com.cragchat.mobile.model.pojo.PojoArea;
import com.cragchat.mobile.model.pojo.PojoComment;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoRoute;
import com.cragchat.mobile.model.realm.RealmComment;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;

/**
 * Created by timde on 11/9/2017.
 */

public interface CragChatRestApi {

    Observable<List<PojoRating>> getRatings(String entityKey);

    Observable<PojoRating> postRating(String userToken, int stars, int yds, String entityKey);

    Observable<PojoComment> postComment(String userToken, String comment, String entityKey, String table);

    Observable<ResponseBody> getAreasContaining(String query);

    Observable<PojoArea> getArea(String areaKey, String areaName);

    Observable<List<PojoRoute>> getRoutes(String[] routeIds);

    Observable<List<PojoArea>> getAreas(String[] areaIds);

    Observable<PojoRoute> getRoute(String routeKey);
}
