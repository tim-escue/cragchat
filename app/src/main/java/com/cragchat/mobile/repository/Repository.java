package com.cragchat.mobile.repository;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.di.ApplicationContext;
import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.remote.CragChatRestApi;
import com.cragchat.mobile.repository.remote.EntityRequestObserver;
import com.cragchat.mobile.repository.remote.RetroFitRestApi;
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
import com.cragchat.mobile.util.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by timde on 11/8/2017.
 */

public class Repository {

    private CragChatDatabase localDatabase;
    private CragChatRestApi networkApi;
    private Context applicationContext;

    @Inject
    public Repository(@ApplicationContext Context context, CragChatDatabase cragChatDatabase) {
        applicationContext = context;
        localDatabase = cragChatDatabase;
        networkApi = RetroFitRestApi.getInstance();
    }

    private void showQueueMessage(String queuedObjectType) {
        StringBuilder message = new StringBuilder();
        message.append(queuedObjectType);
        if (!NetworkUtil.isConnected(applicationContext)) {
            message.append(" cannot be added while offline.");
        } else {
            message.append(" could not be added, there was a network error.");
        }
        message.append("Adding to queue to retry when " +
                "connection is reestablished or app is restarted.");
        Toast.makeText(applicationContext, message.toString(), Toast.LENGTH_LONG).show();
    }

    private void showGetFailure(String getRequest) {
        Toast.makeText(applicationContext, "Could not get " + getRequest + ".",
                Toast.LENGTH_LONG).show();
    }

    private void showSentQueuedSent(String type) {
        Toast.makeText(applicationContext, "Queued " + type + " successfully added",
                Toast.LENGTH_LONG).show();
    }

    public void sendQueuedRequests() {
        String userToken = Authentication.getAuthenticatedUser(applicationContext).getToken();

        for (NewSendRequest req : localDatabase.getNewSendRequests()) {
            addSend(userToken, req.getEntityKey(), req.getPitches(), req.getAttempts(),
                    req.getSendType(), req.getClimbingStyle(), req.getEntityName(), new Callback<Send>() {
                        @Override
                        public void onSuccess(Send object) {
                            showSentQueuedSent("send");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
        }

        for (NewRatingRequest req : localDatabase.getNewRatingRequests()) {
            addRating(userToken, req.getStars(), req.getYds(), req.getEntityKey(), req.getEntityName(),
                    new Callback<Rating>() {
                        @Override
                        public void onSuccess(Rating object) {
                            showSentQueuedSent("rating");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
        }

        for (NewCommentRequest req : localDatabase.getNewCommentRequests()) {
            addComment(userToken, req.getComment(), req.getEntityKey(), req.getTable(),
                    new Callback<Comment>() {
                        @Override
                        public void onSuccess(Comment object) {
                            showSentQueuedSent("comment");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
        }

        for (NewCommentReplyRequest req : localDatabase.getNewCommentReplyRequests()) {
            replyToComment(userToken, req.getComment(), req.getEntityKey(), req.getTable(),
                    req.getParentId(), req.getDepth(), new Callback<Comment>() {
                        @Override
                        public void onSuccess(Comment object) {
                            showSentQueuedSent("comment reply");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
        }

        for (NewCommentEditRequest req : localDatabase.getNewCommentEditRequests()) {
            editComment(userToken, req.getComment(), req.getCommentKey(), new Callback<Comment>() {
                @Override
                public void onSuccess(Comment object) {
                    showSentQueuedSent("comment edit");
                }

                @Override
                public void onFailure() {

                }
            });
        }

        for (NewCommentVoteRequest req : localDatabase.getNewCommentVoteRequests()) {
            addCommentVote(userToken, req.getVote(), req.getCommentKey(), new Callback<Comment>() {
                @Override
                public void onSuccess(Comment object) {
                    showSentQueuedSent("comment vote");
                }

                @Override
                public void onFailure() {

                }
            });
        }

        for (NewImageRequest req : localDatabase.getNewImageRequsts()) {
            try {
                final File file = new File(req.getFilePath());
                addImage(req.getCaptionString(), req.getEntityKey(), req.getEntityType(), file,
                        req.getEntityName(), new Callback<Image>() {
                            @Override
                            public void onSuccess(Image object) {
                                file.delete();
                                showSentQueuedSent("image");
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                /*
                    The image to be uploaded no longer exists. NewImageRequest is not queued to resend.
                 */
                Toast.makeText(applicationContext, "Queued image could not be uploaded",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public List getQueryMatches(final String query, final Callback<List> updateCallback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getAreasContaining(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<ResponseBody>() {
                        @Override
                        public void onNext(ResponseBody object) {
                            Gson gson = new Gson();
                            Type areaType = new TypeToken<PojoArea>() {
                            }.getType();
                            Type routeType = new TypeToken<PojoRoute>() {
                            }.getType();
                            List<Object> objs = new ArrayList<>();
                            try {
                                JSONArray array = new JSONArray(object.string());
                                for (int i = 0; i < array.length(); i++) {
                                    String result = array.getString(i);
                                    if (result.contains("routes")) {
                                        PojoArea area = gson.fromJson(result, areaType);
                                        localDatabase.update(area);
                                        objs.add(area);
                                    } else {
                                        PojoRoute route = gson.fromJson(result, routeType);
                                        localDatabase.update(route);
                                        objs.add(route);
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (objs.size() > 0) {
                                if (updateCallback != null) {
                                    updateCallback.onSuccess(objs);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("query matches for: " + query);
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }

        return localDatabase.getQueryMatches(query);
    }

    public void addCommentVote(String userToken, final String vote, final String commentKey,
                               final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.postCommentVote(userToken, vote, commentKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<PojoComment>() {
                                @Override
                                public void onNext(PojoComment object) {
                                    localDatabase.update(object);
                                    if (callback != null) {
                                        callback.onSuccess(object);
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    showQueueMessage("Comment vote");
                                    localDatabase.addNewCommentVoteRequest(vote, commentKey);
                                    if (callback != null) {
                                        callback.onFailure();
                                    }
                                }
                            }
                    );
        } else {
            showQueueMessage("Comment vote");
            localDatabase.addNewCommentVoteRequest(vote, commentKey);
        }
    }

    public void addSend(String userToken, final String entityKey, final int pitches, final int attempts,
                        final String sendType, final String climbingStyle, final String entityName,
                        final Callback<Send> callback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.postSend(userToken, entityKey, pitches, attempts, sendType, climbingStyle, entityName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<PojoSend>() {
                                @Override
                                public void onNext(PojoSend send) {
                                    localDatabase.update(send);
                                    if (callback != null) {
                                        callback.onSuccess(send);
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    localDatabase.addNewSendRequest(entityKey, pitches, attempts,
                                            sendType, climbingStyle, entityName);
                                    showQueueMessage("send");
                                    if (callback != null) {
                                        callback.onFailure();
                                    }
                                }
                            }
                    );
        } else {
            localDatabase.addNewSendRequest(entityKey, pitches, attempts,
                    sendType, climbingStyle, entityName);
            showQueueMessage("send");
            if (callback != null) {
                callback.onFailure();
            }
        }
    }

    public List<Send> getSends(final String entityId, final Callback<List<Send>> updateCallback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getSends(entityId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<List<PojoSend>>() {
                                @Override
                                public void onNext(List<PojoSend> sends) {
                                    localDatabase.updateSends(sends);
                                    if (updateCallback != null) {
                                        updateCallback.onSuccess(localDatabase.getSends(entityId));
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    showGetFailure("sends");
                                    if (updateCallback != null) {
                                        updateCallback.onFailure();
                                    }
                                }
                            }
                    );
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getSends(entityId);
    }

    public List<Comment> getComments(final String entityId, final String table,
                                     final Callback<List<Comment>> updateCallback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getComments(entityId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<List<PojoComment>>() {
                                @Override
                                public void onNext(List<PojoComment> comments) {
                                    localDatabase.updateComments(comments);
                                    if (updateCallback != null) {
                                        updateCallback.onSuccess(localDatabase.getComments(entityId, table));
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    throwable.printStackTrace();
                                    showGetFailure("comments");
                                    if (updateCallback != null) {
                                        updateCallback.onFailure();
                                    }
                                }
                            }
                    );
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getComments(entityId, table);

    }

    public void addImage(final String captionString,
                         final String entityKey, final String entityType, final File imageFile,
                         final String entityName, final Callback<Image> callback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload",
                    imageFile.getName(), reqFile);
            RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), captionString);
            RequestBody entityTypeRequest = RequestBody.create(MediaType.parse("text/plain"), entityType);
            RequestBody entityKeyRequest = RequestBody.create(MediaType.parse("text/plain"), entityKey);
            RequestBody userToken = RequestBody.create(MediaType.parse("text/plain"),
                    Authentication.getAuthenticatedUser(applicationContext).getToken());
            RequestBody entityNameRequest = RequestBody.create(MediaType.parse("text/plain"), entityName);
            networkApi.postImage(body, userToken, caption, entityKeyRequest, entityTypeRequest, entityNameRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoImage>() {
                        @Override
                        public void onNext(PojoImage image1) {
                            localDatabase.update(image1);
                            if (callback != null) {
                                callback.onSuccess(image1);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                            if (throwable instanceof FileNotFoundException) {
                                Toast.makeText(applicationContext, "Image could no longer be found" +
                                        " to be uploaded", Toast.LENGTH_LONG).show();
                            } else {
                                localDatabase.addNewImageRequest(captionString, entityKey, entityType, Uri.fromFile(imageFile).getEncodedPath(), entityName);
                                showQueueMessage("image");
                            }
                            if (callback != null) {
                                callback.onFailure();
                            }
                        }
                    });

        } else {
            showQueueMessage("image");
            localDatabase.addNewImageRequest(captionString, entityKey, entityType, Uri.fromFile(imageFile).getEncodedPath(), entityName);
            if (callback != null) {
                callback.onFailure();
            }
        }
    }

    public List<Image> getImages(final String key, final Callback<List<Image>> updateCallback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getImages(key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<PojoImage>>() {
                        @Override
                        public void onNext(List<PojoImage> images) {
                            localDatabase.updateImages(images);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(localDatabase.getImages(key));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("images");
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getImages(key);
    }

    public void replyToComment(String userToken, final String comment, final String entityKey,
                               final String table, final String parentId, final int depth, final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.postCommentReply(userToken, comment, entityKey, table, parentId, depth)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<PojoComment>() {
                                @Override
                                public void onNext(PojoComment comment1) {
                                    localDatabase.update(comment1);
                                    if (callback != null) {
                                        callback.onSuccess(comment1);
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    localDatabase.addNewCommentReplyRequest(comment, entityKey, table,
                                            parentId, depth);
                                    showQueueMessage("comment reply");
                                }
                            }
                    );
        } else {
            localDatabase.addNewCommentReplyRequest(comment, entityKey, table,
                    parentId, depth);
            showQueueMessage("comment reply");
        }
    }


    public void addComment(final String userToken, final String comment, final String entityKey,
                           final String table, final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.postComment(userToken, comment, entityKey, table)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoComment>() {
                        @Override
                        public void onNext(PojoComment object) {
                            localDatabase.update(object);
                            if (callback != null) {
                                callback.onSuccess(object);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            localDatabase.addNewCommentRequest(comment, entityKey, table);
                            showQueueMessage("Comment");
                        }
                    });
        } else {
            localDatabase.addNewCommentRequest(comment, entityKey, table);
            showQueueMessage("Comment");
        }
    }

    public void editComment(final String userToken, final String comment, final String commentKey,
                            final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.postCommentEdit(userToken, comment, commentKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoComment>() {
                        @Override
                        public void onNext(PojoComment object) {
                            localDatabase.update(object);
                            if (callback != null) {
                                callback.onSuccess(object);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            localDatabase.addNewCommentEditRequest(comment, commentKey);
                            showQueueMessage("Comment edit");
                        }
                    });
        } else {
            localDatabase.addNewCommentEditRequest(comment, commentKey);
            showQueueMessage("Comment edit");
        }
    }

    public void addRating(final String userToken, final int stars, final int yds, final String entityKey,
                          final String entityName, final Callback<Rating> callback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.postRating(userToken, stars, yds, entityKey, entityName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoRating>() {
                        @Override
                        public void onNext(PojoRating object) {
                            localDatabase.update(object);
                            if (callback != null) {
                                callback.onSuccess(object);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            localDatabase.addNewRatingRequest(stars, yds, entityKey, entityName);
                            showQueueMessage("Rating");
                            if (callback != null) {
                                callback.onFailure();
                            }
                        }
                    });
        } else {
            localDatabase.addNewRatingRequest(stars, yds, entityKey, entityName);
            showQueueMessage("Rating");
            if (callback != null) {
                callback.onFailure();
            }
        }
    }

    public List<Rating> getRatings(final String entityKey, final Callback<List<Rating>> updateCallback) {
        if (entityKey == null || entityKey.isEmpty()) {
            return null;
        }
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getRatings(entityKey).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<PojoRating>>() {
                        @Override
                        public void onNext(List<PojoRating> object) {
                            localDatabase.updateRatings(object);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(localDatabase.getRatings(entityKey));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                            showGetFailure("Ratings");
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getRatings(entityKey);
    }

    public Area getArea(String areaKey, final Callback<Area> updateCallback) {
        if (areaKey == null || areaKey.isEmpty()) {
            return null;
        }
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getArea(areaKey, null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoArea>() {
                        @Override
                        public void onNext(PojoArea areas) {
                            localDatabase.update(areas);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(areas);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("Area");
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getArea(areaKey);
    }

    public Route getRoute(String entityKey, final Callback<Route> updateCallback) {
        if (entityKey == null || entityKey.isEmpty()) {
            return null;
        }
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getRoute(entityKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoRoute>() {
                        @Override
                        public void onNext(PojoRoute routes) {
                            localDatabase.update(routes);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(routes);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("Route");
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getRoute(entityKey);
    }

    public List<Datable> getRecentActivity(String entityKey, List<String> areaIds, List<String> routeIds,
                                           final Callback<List<Datable>> updateCallback) {
        if (entityKey == null || entityKey.isEmpty()) {
            return null;
        }
        List<String> areaList = (areaIds != null && !areaIds.isEmpty()) ? new ArrayList<String>() : null;
        List<String> routeList = (routeIds != null && !routeIds.isEmpty()) ? new ArrayList<String>() : null;
        if (areaList != null) {
            areaList.addAll(areaIds);
        }
        if (routeList != null) {
            routeList.addAll(routeIds);
        }
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getRecentActivity(entityKey, areaList, routeList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<Datable>>() {
                        @Override
                        public void onNext(List<Datable> objects) {
                            localDatabase.updateDatables(objects);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(objects);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("Recent activity");
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getRecentActivity(entityKey,
                (areaIds != null && !areaIds.isEmpty()) ? areaIds.toArray(new String[areaIds.size()]) : null,
                (routeIds != null && !routeIds.isEmpty()) ? routeIds.toArray(new String[routeIds.size()]) : null);
    }

    public List<Datable> getRecentActivity(String entityKey, Callback<List<Datable>> updateCallback) {
        return getRecentActivity(entityKey, null, null, updateCallback);
    }

    public Area getAreaByName(final String areaName, final Callback<Area> updateCallback) {
        if (areaName == null || areaName.isEmpty()) {
            return null;
        }
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getArea(null, areaName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoArea>() {
                        @Override
                        public void onNext(PojoArea area) {
                            localDatabase.update(area);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(area);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("Area by name \"" + areaName + "\"");
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getAreaByName(areaName);
    }

    public List<Route> getRoutes(final String[] routeIds, final Callback<List<Route>> updateCallback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getRoutes(routeIds)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<PojoRoute>>() {
                        @Override
                        public void onNext(List<PojoRoute> routes) {
                            localDatabase.updateRoutes(routes);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(localDatabase.getRoutes(routeIds));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("Routes");
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }

        return localDatabase.getRoutes(routeIds);
    }

    public List<Area> getAreas(final String[] areaIds, final Callback<List<Area>> updateCallback) {
        if (NetworkUtil.isConnected(applicationContext)) {
            networkApi.getAreas(areaIds)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<PojoArea>>() {
                        @Override
                        public void onNext(List<PojoArea> areas) {
                            localDatabase.updateAreas(areas);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(localDatabase.getAreas(areaIds));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                            showGetFailure("Areas");
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return localDatabase.getAreas(areaIds);
    }


}
