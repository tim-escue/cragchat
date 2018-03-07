package com.cragchat.mobile.data.local;

import com.cragchat.mobile.domain.model.Area;
import com.cragchat.mobile.domain.model.Comment;
import com.cragchat.mobile.domain.model.Datable;
import com.cragchat.mobile.domain.model.Image;
import com.cragchat.mobile.data.local.model.NewCommentEditRequest;
import com.cragchat.mobile.data.local.model.NewCommentReplyRequest;
import com.cragchat.mobile.data.local.model.NewCommentRequest;
import com.cragchat.mobile.data.local.model.NewCommentVoteRequest;
import com.cragchat.mobile.data.local.model.NewImageRequest;
import com.cragchat.mobile.data.local.model.NewRatingRequest;
import com.cragchat.mobile.data.local.model.NewSendRequest;
import com.cragchat.mobile.domain.model.Rating;
import com.cragchat.mobile.domain.model.Route;
import com.cragchat.mobile.domain.model.Send;

import java.util.List;

/**
 * Created by timde on 9/21/2017.
 */

public interface ClimbrLocalDatabase {

    Area getArea(String areaKey);

    Area getAreaByName(String areaName);

    Route getRoute(String entityKey);

    List<Datable> getRecentActivity(String entityKey, String[] routeIds, String[] areaIds);

    List<Route> getRoutes(String[] routeIds);

    List<Area> getAreas(String[] areaIds);

    List<Rating> getRatings(String entityKey);

    List<Image> getImages(String entityKey);

    List<Comment> getComments(String entityKey, String table);

    List<Send> getSends(String entityKey);

    List<NewSendRequest> getNewSendRequests();

    List<NewCommentEditRequest> getNewCommentEditRequests();

    List<NewCommentReplyRequest> getNewCommentReplyRequests();

    List<NewCommentRequest> getNewCommentRequests();

    List<NewCommentVoteRequest> getNewCommentVoteRequests();

    List<NewImageRequest> getNewImageRequsts();

    List<NewRatingRequest> getNewRatingRequests();

    void update(final Send send);

    void updateSends(final List<Send> sends);

    void update(Image image);

    void update(Area area);

    void update(Rating rating);

    void update(Comment comment);

    void updateDatables(List<Datable> image);

    void updateImages(List<Image> image);

    void updateRatings(List<Rating> ratings);

    void updateComments(List<Comment> comments);

    void updateAreas(List<Area> areas);

    void updateRoutes(List<Route> routes);

    void update(final Route route);

    void addNewCommentRequest(String userToken, String comment, String entityKey, String table);

    void addNewRatingRequest(String userToken,int stars, int yds, String entityKey, String entityName);

    void addNewCommentEditRequest(String userToken, String comment, String commentKey);

    void addNewImageRequest(String userToken, String captionString, String entityKey, String entityType, String fileUri, String entityName);

    void addNewSendRequest(String userToken, String entityKey, int pitches, int attempts, String sendType,
                           String climbingStyle, String entityName);

    void addNewCommentVoteRequest(String userToken, String vote, String commentKey);

    void addNewCommentReplyRequest(String userToken, String comment, String entityKey, String table, String parentId,
                                   int depth);

    List getQueryMatches(String query);

}
