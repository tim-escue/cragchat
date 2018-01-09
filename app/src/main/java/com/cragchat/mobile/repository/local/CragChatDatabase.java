package com.cragchat.mobile.repository.local;

import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Comment;
import com.cragchat.mobile.ui.model.Datable;
import com.cragchat.mobile.ui.model.Image;
import com.cragchat.mobile.ui.model.NewCommentEditRequest;
import com.cragchat.mobile.ui.model.NewCommentReplyRequest;
import com.cragchat.mobile.ui.model.NewCommentRequest;
import com.cragchat.mobile.ui.model.NewCommentVoteRequest;
import com.cragchat.mobile.ui.model.NewImageRequest;
import com.cragchat.mobile.ui.model.NewRatingRequest;
import com.cragchat.mobile.ui.model.NewSendRequest;
import com.cragchat.mobile.ui.model.Rating;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.model.Send;
import com.cragchat.mobile.ui.model.pojo.PojoArea;
import com.cragchat.mobile.ui.model.pojo.PojoComment;
import com.cragchat.mobile.ui.model.pojo.PojoImage;
import com.cragchat.mobile.ui.model.pojo.PojoRating;
import com.cragchat.mobile.ui.model.pojo.PojoRoute;
import com.cragchat.mobile.ui.model.pojo.PojoSend;

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
