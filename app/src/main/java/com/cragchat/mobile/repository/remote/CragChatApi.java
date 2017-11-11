package com.cragchat.mobile.repository.remote;

import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.model.pojo.PojoArea;
import com.cragchat.mobile.model.pojo.PojoComment;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoRoute;
import com.cragchat.mobile.model.realm.RealmComment;
import com.cragchat.mobile.model.realm.RealmImage;
import com.cragchat.mobile.model.realm.RealmRating;
import com.cragchat.mobile.model.realm.RealmSend;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by timde on 10/12/2017.
 */

public interface CragChatApi {

    public static final String TYPE_AREA = "area";
    public static final String TYPE_ROUTE = "route";

    @GET("/api/sends")
    Observable<List<RealmSend>> getSends(
            @Query("entity_key") String entityKey
    );

    @FormUrlEncoded
    @POST("/api/sends")
    Observable<RealmSend> postSend(
            @Field("user_token") String user_token,
            @Field("entity_key") String entityKey,
            @Field("pitches") int pitches,
            @Field("attempts") int attempts,
            @Field("send_type") String sendType,
            @Field("climbing_style") String climbingStyle
    );

    @GET("/api/rating")
    Observable<List<PojoRating>> getRatings(
            @Query("entity_key") String entityKey
    );

    @FormUrlEncoded
    @POST("/api/rating")
    Observable<PojoRating> postRating(
            @Field("user_token") String user_token,
            @Field("stars") int stars,
            @Field("yds") int yds,
            @Field("entity_key") String entityKey
    );

    @Multipart
    @POST("/api/image")
    Observable<RealmImage> postImage(
            @Part MultipartBody.Part image,
            @Part("user_token") RequestBody userToken,
            @Part("caption") RequestBody caption,
            @Part("entity_key") RequestBody entityKey,
            @Part("entity_type") RequestBody entityType
    );

    @GET("/api/image")
    Observable<List<RealmImage>> getImages(
            @Query("entity_key") String entity_key
    );

    @FormUrlEncoded
    @POST("/store")
    Call<ResponseBody> addObject(
            @Field("user_token") String user_token,
            @Field("type") String type,
            @Field("jsonString") String jsonString
    );

    @FormUrlEncoded
    @POST("/api/addcomment")
    Observable<PojoComment> postComment(
            @Field("user_token") String userToken,
            @Field("comment") String comment,
            @Field("entityId") String entityKey,
            @Field("table") String table
    );

    @FormUrlEncoded
    @POST("/api/addcomment")
    Observable<PojoComment> postCommentReply(
            @Field("user_token") String userToken,
            @Field("comment") String comment,
            @Field("entityId") String entityKey,
            @Field("table") String table,
            @Field("parentId") String parentId,
            @Field("depth") int depth
    );

    @FormUrlEncoded
    @POST("/api/editcomment")
    Observable<PojoComment> postCommentEdit(
            @Field("user_token") String userToken,
            @Field("new_comment") String newComment,
            @Field("comment_key") String commentKey
    );

    @FormUrlEncoded
    @POST("/api/vote")
    Observable<PojoComment> postCommentVote(
            @Field("user_token") String userToken,
            @Field("vote") String vote,
            @Field("commentKey") String commentKey
    );

    @GET("api/getcomments")
    Observable<List<PojoComment>> getComments(
            @Query("key") String entityId
    );

    @GET("/authenticate")
    Observable<AuthenticatedUser> login(
            @Query("username") String userName,
            @Query("password") String password
    );

    @GET("/route")
    Observable<List<PojoRoute>> getRoutes(
            @Query("multiple") String stringJsonArray
    );

    @GET("/area")
    Observable<List<PojoArea>> getAreas(
            @Query("multiple") String areaKeysAsJsonArray
    );

    @GET("/area")
    Observable<ResponseBody> getAreasContaining(
            @Query("containing") String query
    );

    @GET("/area")
    Observable<PojoArea> getArea(
            @Query("key") String key,
            @Query("name") String name
    );

    @GET("/area")
    Observable<List<PojoArea>> getAreas();

}
