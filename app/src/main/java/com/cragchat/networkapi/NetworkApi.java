package com.cragchat.networkapi;

import com.cragchat.mobile.database.Database;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.database.models.RealmRoute;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.util.FormatUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by timde on 10/12/2017.
 */

public class NetworkApi {

    private static CragChatApi api;

    static {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-54-148-84-77.us-west-2.compute.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(CragChatApi.class);
    }

    public static CragChatApi getInstance() {
        return api;
    }

    /*public static void addRoute(String userToken, Route route) {
        String json = new JSONObject(route.getMap()).toString();
        api.addObject(userToken, CragChatApi.TYPE_ROUTE, json).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    System.out.println("Responsecode:" + response.code() + " response:" + body);
                    if (response.code() == 200) {
                        JSONObject object = new JSONObject(body);
                        //Database.getInstance().updateOrAddRoute(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    public static void addArea(String userToken, Area area) {
        String json = new JSONObject(area.getMap()).toString();
        System.out.println("Sending json:" + json);
        api.addObject(userToken, CragChatApi.TYPE_AREA, json).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    System.out.println("Responsecode:" + response.code() + " response:" + body);
                    if (response.code() == 200) {
                        JSONObject object = new JSONObject(body);
                      //  Database.getInstance().updateOrAddArea(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }*/

}
