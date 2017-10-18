package com.cragchat.networkapi;

import com.cragchat.mobile.database.CragChatDatasource;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by timde on 10/12/2017.
 */

public interface CragChatApi {

    public static final String TYPE_AREA = "area";
    public static final String TYPE_ROUTE = "route";

    @FormUrlEncoded
    @POST("/store")
    Call<ResponseBody> addObject(
            @Field("user_token") String user_token,

            @Field("type") String type,
            @Field("jsonString") String jsonString
    );

    @GET("/retrieve")
    Observable<ResponseBody> getObject(
            @Query("type") String type,
            @Query("key") String key,
            @Query("name") String name
    );
}
