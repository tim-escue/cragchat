package com.cragchat.mobile.repository.remote;

import com.cragchat.mobile.authentication.AuthenticatedUser;
import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.model.pojo.PojoArea;
import com.cragchat.mobile.model.pojo.PojoComment;
import com.cragchat.mobile.model.pojo.PojoImage;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoRoute;
import com.cragchat.mobile.model.pojo.PojoSend;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by timde on 11/9/2017.
 */

public interface CragChatRestApi {

    Observable<List<Datable>> getRecentActivity(String entityKey, List<String> areaIds, List<String> routeIds);

    Observable<List<PojoSend>> getSends(String entityKey);

    Observable<PojoSend> postSend(String user_token, String entityKey, int pitches,
                                  int attempts, String sendType, String climbingStyle);

    Observable<PojoComment> postCommentVote(String userToken, String vote, String commentKey);

    Observable<AuthenticatedUser> login(String userName, String password);

    Observable<ResponseBody> register(String userName, String password, String email);

    Observable<PojoComment> postCommentReply(String userToken, String comment, String entityKey,
                                             String table, String parentId, int depth);

    Observable<PojoImage> postImage(MultipartBody.Part image, RequestBody userToken,
                                    RequestBody caption, RequestBody entityKey, RequestBody entityType);

    Observable<List<PojoImage>> getImages(String entityKey);

    Observable<PojoComment> postCommentEdit(String userToken, String comment, String entityKey);

    Observable<List<PojoComment>> getComments(String entityId);

    Observable<List<PojoRating>> getRatings(String entityKey);

    Observable<PojoRating> postRating(String userToken, int stars, int yds, String entityKey);

    Observable<PojoComment> postComment(String userToken, String comment, String entityKey, String table);

    Observable<ResponseBody> getAreasContaining(String query);

    Observable<PojoArea> getArea(String areaKey, String areaName);

    Observable<List<PojoRoute>> getRoutes(String[] routeIds);

    Observable<List<PojoArea>> getAreas(String[] areaIds);

    Observable<PojoRoute> getRoute(String routeKey);
}
