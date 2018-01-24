package com.cragchat.mobile.repository.remote;

import android.util.Log;

import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.model.pojo.PojoArea;
import com.cragchat.mobile.ui.model.pojo.PojoComment;
import com.cragchat.mobile.ui.model.pojo.PojoImage;
import com.cragchat.mobile.ui.model.pojo.PojoRating;
import com.cragchat.mobile.ui.model.pojo.PojoRoute;
import com.cragchat.mobile.ui.model.pojo.PojoSend;
import com.cragchat.mobile.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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
        Gson gson = new GsonBuilder().registerTypeAdapter(Datable.class, JsonUtil.getDatableDeserializer())
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response response = chain.proceed(chain.request());
                                Log.w("RetrofitResponse", response.peekBody(Long.MAX_VALUE).string());
                                return response;
                            }
                        }
                ).addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Log.w("RetrofitRequest:", chain.request().toString());

                        return chain.proceed(chain.request());
                    }
                })
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
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
    public Observable<List<Datable>> getRecentActivity(String entityKey, List<String> areaIds, List<String> routeIds) {
        return api.getRecentActivity(entityKey, areaIds, routeIds);
    }

    @Override
    public Observable<List<PojoSend>> getSends(String entityKey) {
        return api.getSends(entityKey);
    }

    @Override
    public Observable<PojoSend> postSend(String user_token, String entityKey, int pitches,
                                         int attempts, String sendType, String climbingStyle,
                                         String entityName) {
        return api.postSend(user_token, entityKey, pitches, attempts, sendType, climbingStyle, entityName);
    }

    @Override
    public Observable<PojoComment> postCommentVote(String userToken, String vote, String commentKey) {
        return api.postCommentVote(userToken, vote, commentKey);
    }

    @Override
    public Observable<AuthenticatedUser> login(String userName, String password) {
        return api.login(userName, password);
    }

    @Override
    public Observable<ResponseBody> register(String userName, String password, String email) {
        return api.register(userName, password, email);
    }

    @Override
    public Observable<PojoComment> postCommentReply(String userToken, String comment, String entityKey,
                                                    String table, String parentId, int depth) {
        return api.postCommentReply(userToken, comment, entityKey, table, parentId, depth);
    }

    @Override
    public Observable<PojoImage> postImage(MultipartBody.Part image, RequestBody userToken,
                                           RequestBody caption, RequestBody entityKey,
                                           RequestBody entityType, RequestBody entityName) {
        return api.postImage(image, userToken, caption, entityKey, entityType, entityName);
    }

    @Override
    public Observable<List<PojoImage>> getImages(String entityKey) {
        return api.getImages(entityKey);
    }

    @Override
    public Observable<PojoComment> postCommentEdit(String userToken, String comment, String commentKey) {
        return api.postCommentEdit(userToken, comment, commentKey);
    }

    @Override
    public Observable<List<PojoComment>> getComments(String entityId) {
        return api.getComments(entityId);
    }

    @Override
    public Observable<List<PojoRating>> getRatings(String entityKey) {
        return api.getRatings(entityKey);
    }

    @Override
    public Observable<PojoRating> postRating(String userToken, int stars, int yds, String entityKey,
                                             String entityName) {
        return api.postRating(userToken, stars, yds, entityKey, entityName);
    }

    @Override
    public Observable<PojoComment> postComment(String userToken, String comment, String entityKey, String table) {
        return api.postComment(userToken, comment, entityKey, table);
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
        return api.getRoute(routeKey);
    }

}
