package com.cragchat.mobile.data.remote.retrofit;

import android.util.Log;

import com.cragchat.mobile.data.authentication.AuthenticatedUser;
import com.cragchat.mobile.data.remote.ClimbrRemoteDatasource;
import com.cragchat.mobile.domain.model.Area;
import com.cragchat.mobile.domain.model.Comment;
import com.cragchat.mobile.domain.model.Datable;
import com.cragchat.mobile.domain.model.Image;
import com.cragchat.mobile.domain.model.Rating;
import com.cragchat.mobile.domain.model.Route;
import com.cragchat.mobile.domain.model.Send;
import com.cragchat.mobile.data.remote.pojo.PojoArea;
import com.cragchat.mobile.data.remote.pojo.PojoComment;
import com.cragchat.mobile.data.remote.pojo.PojoImage;
import com.cragchat.mobile.data.remote.pojo.PojoRating;
import com.cragchat.mobile.data.remote.pojo.PojoRoute;
import com.cragchat.mobile.data.remote.pojo.PojoSend;
import com.cragchat.mobile.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by timde on 10/12/2017.
 */

public class RetrofitRestAPI implements ClimbrRemoteDatasource {

    private CragChatApi api;

    public RetrofitRestAPI() {
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
                .addInterceptor(chain -> {
                    try {
                        return chain.proceed(chain.request());
                    } catch (Exception e) {
                        Log.d("retrofit", "Caught exception");
                        e.printStackTrace();
                    }
                    return chain.proceed(chain.request());
                })
                .addInterceptor(logging)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
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
    public Observable<Send> addSend(String user_token, String entityKey, int pitches,
                                    int attempts, String sendType, String climbingStyle,
                                    String entityName) {
        return api.postSend(user_token, entityKey, pitches, attempts, sendType, climbingStyle, entityName);
    }

    @Override
    public Observable<Comment> addCommentVote(String userToken, String vote, String commentKey) {
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
    public Observable<Comment> addCommentReply(String userToken, String comment, String entityKey,
                                               String table, String parentId, int depth) {
        return api.postCommentReply(userToken, comment, entityKey, table, parentId, depth);
    }

    @Override
    public Observable<Image> addImage(MultipartBody.Part image, RequestBody userToken,
                                      RequestBody caption, RequestBody entityKey,
                                      RequestBody entityType, RequestBody entityName) {
        return api.postImage(image, userToken, caption, entityKey, entityType, entityName);
    }

    @Override
    public Observable<List<Image>> getImages(String entityKey) {
        return api.getImages(entityKey);
    }

    @Override
    public Observable<Comment> addCommentEdit(String userToken, String comment, String commentKey) {
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
    public Observable<Rating> addRating(String userToken, int stars, int yds, String entityKey,
                                        String entityName) {
        return api.postRating(userToken, stars, yds, entityKey, entityName);
    }

    @Override
    public Observable<Comment> addComment(String userToken, String comment, String entityKey, String table) {
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
