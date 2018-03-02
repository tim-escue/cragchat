package com.cragchat.mobile.repository.remote;

import android.util.Log;

import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Comment;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.model.Image;
import com.cragchat.mobile.ui.model.Rating;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.model.Send;
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
import okhttp3.logging.HttpLoggingInterceptor;
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
                .registerTypeAdapter(Send.class, JsonUtil.getDeserializer(PojoSend.class))
                .registerTypeAdapter(Comment.class, JsonUtil.getDeserializer(PojoComment.class))
                .registerTypeAdapter(Area.class, JsonUtil.getDeserializer(PojoArea.class))
                .registerTypeAdapter(Route.class, JsonUtil.getDeserializer(PojoRoute.class))
                .registerTypeAdapter(Rating.class, JsonUtil.getDeserializer(PojoRating.class))
                .registerTypeAdapter(Image.class, JsonUtil.getDeserializer(PojoImage.class))
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
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
    public Observable<List<Send>> getSends(String entityKey) {
        return api.getSends(entityKey);
    }

    @Override
    public Observable<Send> postSend(String user_token, String entityKey, int pitches,
                                     int attempts, String sendType, String climbingStyle,
                                     String entityName) {
        return api.postSend(user_token, entityKey, pitches, attempts, sendType, climbingStyle, entityName);
    }

    @Override
    public Observable<Comment> postCommentVote(String userToken, String vote, String commentKey) {
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
    public Observable<Comment> postCommentReply(String userToken, String comment, String entityKey,
                                                String table, String parentId, int depth) {
        return api.postCommentReply(userToken, comment, entityKey, table, parentId, depth);
    }

    @Override
    public Observable<Image> postImage(MultipartBody.Part image, RequestBody userToken,
                                       RequestBody caption, RequestBody entityKey,
                                       RequestBody entityType, RequestBody entityName) {
        return api.postImage(image, userToken, caption, entityKey, entityType, entityName);
    }

    @Override
    public Observable<List<Image>> getImages(String entityKey) {
        return api.getImages(entityKey);
    }

    @Override
    public Observable<Comment> postCommentEdit(String userToken, String comment, String commentKey) {
        return api.postCommentEdit(userToken, comment, commentKey);
    }

    @Override
    public Observable<List<Comment>> getComments(String entityId) {
        return api.getComments(entityId);
    }

    @Override
    public Observable<List<Rating>> getRatings(String entityKey) {
        return api.getRatings(entityKey);
    }

    @Override
    public Observable<Rating> postRating(String userToken, int stars, int yds, String entityKey,
                                             String entityName) {
        return api.postRating(userToken, stars, yds, entityKey, entityName);
    }

    @Override
    public Observable<Comment> postComment(String userToken, String comment, String entityKey, String table) {
        return api.postComment(userToken, comment, entityKey, table);
    }

    @Override
    public Observable<ResponseBody> getAreasContaining(String query) {
        return api.getAreasContaining(query);
    }

    @Override
    public Observable<Area> getArea(String areaKey, String areaName) {
        return api.getArea(areaKey, areaName);
    }

    @Override
    public Observable<List<Route>> getRoutes(String[] routeIds) {
        return api.getRoutes(JsonUtil.stringArrayToJSon(routeIds));
    }

    @Override
    public Observable<List<Area>> getAreas(String[] areaIds) {
        return api.getAreas(JsonUtil.stringArrayToJSon(areaIds));
    }

    @Override
    public Observable<Route> getRoute(String routeKey) {
        return api.getRoute(routeKey);
    }

}
