package com.cragchat.mobile.repository.local;

import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Comment;
import com.cragchat.mobile.model.Datable;
import com.cragchat.mobile.model.Image;
import com.cragchat.mobile.model.NewCommentEditRequest;
import com.cragchat.mobile.model.NewCommentReplyRequest;
import com.cragchat.mobile.model.NewCommentRequest;
import com.cragchat.mobile.model.NewCommentVoteRequest;
import com.cragchat.mobile.model.NewImageRequest;
import com.cragchat.mobile.model.NewRatingRequest;
import com.cragchat.mobile.model.NewSendRequest;
import com.cragchat.mobile.model.Rating;
import com.cragchat.mobile.model.Route;
import com.cragchat.mobile.model.Send;
import com.cragchat.mobile.model.pojo.PojoArea;
import com.cragchat.mobile.model.pojo.PojoComment;
import com.cragchat.mobile.model.pojo.PojoImage;
import com.cragchat.mobile.model.pojo.PojoRating;
import com.cragchat.mobile.model.pojo.PojoRoute;
import com.cragchat.mobile.model.pojo.PojoSend;

import java.util.List;

/**
 * Created by timde on 9/21/2017.
 */

public interface CragChatDatabase {

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

    void update(final PojoSend send);

    void updateSends(final List<PojoSend> sends);

    void update(PojoImage image);

    void update(PojoArea area);

    void update(PojoRating rating);

    void update(PojoComment comment);

    void updateDatables(List<Datable> image);

    void updateImages(List<PojoImage> image);

    void updateRatings(List<PojoRating> ratings);

    void updateComments(List<PojoComment> comments);

    void updateAreas(List<PojoArea> areas);

    void updateRoutes(List<PojoRoute> routes);

    void update(final PojoRoute route);

    void addNewCommentRequest(String comment, String entityKey, String table);

    void addNewRatingRequest(int stars, int yds, String entityKey, String entityName);

    void addNewCommentEditRequest(String comment, String commentKey);

    void addNewImageRequest(String captionString, String entityKey, String entityType, String fileUri, String entityName);

    void addNewSendRequest(String entityKey, int pitches, int attempts, String sendType,
                           String climbingStyle, String entityName);

    void addNewCommentVoteRequest(String vote, String commentKey);

    void addNewCommentReplyRequest(String comment, String entityKey, String table, String parentId,
                                   int depth);

    List getQueryMatches(String query);

}
