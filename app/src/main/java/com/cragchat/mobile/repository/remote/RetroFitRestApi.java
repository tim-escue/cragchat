package com.cragchat.mobile.repository.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cragchat.mobile.model.pojo.PojoArea;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoRoute;
import com.cragchat.mobile.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by timde on 10/12/2017.
 */

public class RetroFitRestApi implements CragChatRestApi {

    private CragChatApi api;

    public RetroFitRestApi() {
        Gson gson = new GsonBuilder()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response response = chain.proceed(chain.request());
                                Log.w("Retrofit@Response", response.peekBody(Long.MAX_VALUE).string());
                                return response;
                            }
                        }
                )
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-54-148-84-77.us-west-2.compute.amazonaws.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(CragChatApi.class);
    }

    @Override
    public Observable<List<PojoRating>> getRatings(String entityKey) {
        return api.getRatings(entityKey);
    }

    @Override
    public Observable<PojoRating> postRating(String userToken, int stars, int yds, String entityKey) {
        return api.postRating(userToken, stars, yds, entityKey);
    }

    @Override
    public Observable<ResponseBody> getAreasContaining(String query) {
        return api.getAreasContaining(query);
    }

    @Override
    public Observable<PojoArea> getArea(String areaKey, String areaName) {
        return api.getArea(areaKey, areaName);
    }

    @Override
    public Observable<List<PojoRoute>> getRoutes(String[] routeIds) {
        return api.getRoutes(JsonUtil.stringArrayToJSon(routeIds));
    }

    @Override
    public Observable<List<PojoArea>> getAreas(String[] areaIds) {
        return api.getAreas(JsonUtil.stringArrayToJSon(areaIds));
    }

    @Override
    public Observable<PojoRoute> getRoute(String routeKey) {
        return null;
    }

}
