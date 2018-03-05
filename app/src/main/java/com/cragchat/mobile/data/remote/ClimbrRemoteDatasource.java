package com.cragchat.mobile.data.remote;

import com.cragchat.mobile.data.authentication.AuthenticatedUser;
import com.cragchat.mobile.domain.model.Area;
import com.cragchat.mobile.domain.model.Comment;
import com.cragchat.mobile.domain.model.Datable;
import com.cragchat.mobile.domain.model.Image;
import com.cragchat.mobile.domain.model.Rating;
import com.cragchat.mobile.domain.model.Route;
import com.cragchat.mobile.domain.model.Send;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by timde on 11/9/2017.
 */

public interface ClimbrRemoteDatasource {

    Observable<List<Datable>> getRecentActivity(String entityKey, List<String> areaIds, List<String> routeIds);

    Observable<List<Send>> getSends(String entityKey);

    Observable<Send> addSend(String user_token, String entityKey, int pitches,
                             int attempts, String sendType, String climbingStyle,
                             String entityName);

    Observable<Comment> addCommentVote(String userToken, String vote, String commentKey);

    Observable<AuthenticatedUser> login(String userName, String password);

    Observable<ResponseBody> register(String userName, String password, String email);

    Observable<Comment> addCommentReply(String userToken, String comment, String entityKey,
                                        String table, String parentId, int depth);

    Observable<Image> addImage(MultipartBody.Part image, RequestBody userToken,
                               RequestBody caption, RequestBody entityKey,
                               RequestBody entityType, RequestBody entityName);

    Observable<List<Image>> getImages(String entityKey);

    Observable<Comment> addCommentEdit(String userToken, String comment, String entityKey);

    Observable<List<Comment>> getComments(String entityId);

    Observable<List<Rating>> getRatings(String entityKey);

    Observable<Rating> addRating(String userToken, int stars, int yds, String entityKey,
                                 String entityName);

    Observable<Comment> addComment(String userToken, String comment, String entityKey, String table);

    Observable<ResponseBody> getAreasContaining(String query);

    Observable<Area> getArea(String areaKey, String areaName);

    Observable<List<Route>> getRoutes(String[] routeIds);

    Observable<List<Area>> getAreas(String[] areaIds);

    Observable<Route> getRoute(String routeKey);
}
