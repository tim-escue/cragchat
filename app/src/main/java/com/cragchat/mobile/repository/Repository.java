package com.cragchat.mobile.repository;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.remote.CragChatRestApi;
import com.cragchat.mobile.repository.remote.EntityRequestObserver;
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
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
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

    private CragChatDatabase mLocalDatabase;
    private CragChatRestApi mRestApi;
    private Context mApplicationContext;
    private Authentication mAuthentication;

    @Inject
    public Repository(Context context, CragChatDatabase cragChatDatabase,
                      CragChatRestApi restApi, Authentication authentication) {
        mApplicationContext = context;
        mLocalDatabase = cragChatDatabase;
        mRestApi = restApi;
        mAuthentication = authentication;
    }

    private void showQueueMessage(String queuedObjectType) {
        StringBuilder message = new StringBuilder();
        message.append(queuedObjectType);
        if (!NetworkUtil.isConnected(mApplicationContext)) {
            message.append(" cannot be added while offline.");
        } else {
            message.append(" could not be added, there was a network error.");
        }
        message.append(" Adding to queue to retry when " +
                "connection is re-established or app is restarted.");
        Toast.makeText(mApplicationContext, message.toString(), Toast.LENGTH_LONG).show();
    }

    private void showGetFailure(String getRequest) {
        Toast.makeText(mApplicationContext, "Could not get " + getRequest + ".",
                Toast.LENGTH_LONG).show();
    }

    private void showSentQueuedSent(String type) {
        Toast.makeText(mApplicationContext, "Queued " + type + " successfully added",
                Toast.LENGTH_LONG).show();
    }

    public void sendQueuedRequests() {
        String userToken = mAuthentication.getAuthenticatedUser(mApplicationContext).getToken();

        for (NewSendRequest req : mLocalDatabase.getNewSendRequests()) {
            addSend(userToken, req.getEntityKey(), req.getPitches(), req.getAttempts(),
                    req.getSendType(), req.getClimbingStyle(), req.getEntityName(), new Callback<Send>() {
                        @Override
                        public void onSuccess(Send object) {
                            showSentQueuedSent("Send");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
        }

        for (NewRatingRequest req : mLocalDatabase.getNewRatingRequests()) {
            addRating(userToken, req.getStars(), req.getYds(), req.getEntityKey(), req.getEntityName(),
                    new Callback<Rating>() {
                        @Override
                        public void onSuccess(Rating object) {
                            showSentQueuedSent("Rating");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
        }

        for (NewCommentRequest req : mLocalDatabase.getNewCommentRequests()) {
            addComment(userToken, req.getComment(), req.getEntityKey(), req.getTable(),
                    new Callback<Comment>() {
                        @Override
                        public void onSuccess(Comment object) {
                            showSentQueuedSent("Comment");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
        }

        for (NewCommentReplyRequest req : mLocalDatabase.getNewCommentReplyRequests()) {
            replyToComment(userToken, req.getComment(), req.getEntityKey(), req.getTable(),
                    req.getParentId(), req.getDepth(), new Callback<Comment>() {
                        @Override
                        public void onSuccess(Comment object) {
                            showSentQueuedSent("Comment reply");
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
        }

        for (NewCommentEditRequest req : mLocalDatabase.getNewCommentEditRequests()) {
            editComment(userToken, req.getComment(), req.getCommentKey(), new Callback<Comment>() {
                @Override
                public void onSuccess(Comment object) {
                    showSentQueuedSent("Comment edit");
                }

                @Override
                public void onFailure() {

                }
            });
        }

        for (NewCommentVoteRequest req : mLocalDatabase.getNewCommentVoteRequests()) {
            addCommentVote(userToken, req.getVote(), req.getCommentKey(), new Callback<Comment>() {
                @Override
                public void onSuccess(Comment object) {
                    showSentQueuedSent("Comment vote");
                }

                @Override
                public void onFailure() {

                }
            });
        }

        for (NewImageRequest req : mLocalDatabase.getNewImageRequsts()) {
            try {
                final File file = new File(req.getFilePath());
                addImage(req.getCaptionString(), req.getEntityKey(), req.getEntityType(), file,
                        req.getEntityName(), new Callback<Image>() {
                            @Override
                            public void onSuccess(Image object) {
                                file.delete();
                                showSentQueuedSent("Image");
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
                Toast.makeText(mApplicationContext, "Queued image no longer exists and could not be uploaded",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public List getQueryMatches(final String query, final Callback<List> updateCallback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getAreasContaining(query)
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
                                        mLocalDatabase.update(area);
                                        objs.add(area);
                                    } else {
                                        PojoRoute route = gson.fromJson(result, routeType);
                                        mLocalDatabase.update(route);
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

        return mLocalDatabase.getQueryMatches(query);
    }

    public void addCommentVote(String userToken, final String vote, final String commentKey,
                               final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.postCommentVote(userToken, vote, commentKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<PojoComment>() {
                                @Override
                                public void onNext(PojoComment object) {
                                    mLocalDatabase.update(object);
                                    if (callback != null) {
                                        callback.onSuccess(object);
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    showQueueMessage("Comment vote");
                                    mLocalDatabase.addNewCommentVoteRequest(vote, commentKey);
                                    if (callback != null) {
                                        callback.onFailure();
                                    }
                                }
                            }
                    );
        } else {
            showQueueMessage("Comment vote");
            mLocalDatabase.addNewCommentVoteRequest(vote, commentKey);
        }
    }

    public void addSend(String userToken, final String entityKey, final int pitches, final int attempts,
                        final String sendType, final String climbingStyle, final String entityName,
                        final Callback<Send> callback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.postSend(userToken, entityKey, pitches, attempts, sendType, climbingStyle, entityName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<PojoSend>() {
                                @Override
                                public void onNext(PojoSend send) {
                                    mLocalDatabase.update(send);
                                    if (callback != null) {
                                        callback.onSuccess(send);
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    mLocalDatabase.addNewSendRequest(entityKey, pitches, attempts,
                                            sendType, climbingStyle, entityName);
                                    showQueueMessage("Send");
                                    if (callback != null) {
                                        callback.onFailure();
                                    }
                                }
                            }
                    );
        } else {
            mLocalDatabase.addNewSendRequest(entityKey, pitches, attempts,
                    sendType, climbingStyle, entityName);
            showQueueMessage("Send");
            if (callback != null) {
                callback.onFailure();
            }
        }
    }

    public List<Send> getSends(final String entityId, final Callback<List<Send>> updateCallback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getSends(entityId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<List<PojoSend>>() {
                                @Override
                                public void onNext(List<PojoSend> sends) {
                                    mLocalDatabase.updateSends(sends);
                                    if (updateCallback != null) {
                                        updateCallback.onSuccess(mLocalDatabase.getSends(entityId));
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
        return mLocalDatabase.getSends(entityId);
    }

    public List<Comment> getComments(final String entityId, final String table,
                                     final Callback<List<Comment>> updateCallback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getComments(entityId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<List<PojoComment>>() {
                                @Override
                                public void onNext(List<PojoComment> comments) {
                                    mLocalDatabase.updateComments(comments);
                                    if (updateCallback != null) {
                                        updateCallback.onSuccess(mLocalDatabase.getComments(entityId, table));
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
        return mLocalDatabase.getComments(entityId, table);

    }

    public void addImage(final String captionString,
                         final String entityKey, final String entityType, final File imageFile,
                         final String entityName, final Callback<Image> callback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload",
                    imageFile.getName(), reqFile);
            RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), captionString);
            RequestBody entityTypeRequest = RequestBody.create(MediaType.parse("text/plain"), entityType);
            RequestBody entityKeyRequest = RequestBody.create(MediaType.parse("text/plain"), entityKey);
            RequestBody userToken = RequestBody.create(MediaType.parse("text/plain"),
                    mAuthentication.getAuthenticatedUser(mApplicationContext).getToken());
            RequestBody entityNameRequest = RequestBody.create(MediaType.parse("text/plain"), entityName);
            mRestApi.postImage(body, userToken, caption, entityKeyRequest, entityTypeRequest, entityNameRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoImage>() {
                        @Override
                        public void onNext(PojoImage image1) {
                            mLocalDatabase.update(image1);
                            if (callback != null) {
                                callback.onSuccess(image1);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                            if (throwable instanceof FileNotFoundException) {
                                Toast.makeText(mApplicationContext, "Image could no longer be found" +
                                        " to be uploaded", Toast.LENGTH_LONG).show();
                            } else {
                                mLocalDatabase.addNewImageRequest(captionString, entityKey, entityType, Uri.fromFile(imageFile).getEncodedPath(), entityName);
                                showQueueMessage("Image");
                            }
                            if (callback != null) {
                                callback.onFailure();
                            }
                        }
                    });

        } else {
            showQueueMessage("Image");
            mLocalDatabase.addNewImageRequest(captionString, entityKey, entityType, Uri.fromFile(imageFile).getEncodedPath(), entityName);
            if (callback != null) {
                callback.onFailure();
            }
        }
    }

    public List<Image> getImages(final String key, final Callback<List<Image>> updateCallback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getImages(key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<PojoImage>>() {
                        @Override
                        public void onNext(List<PojoImage> images) {
                            mLocalDatabase.updateImages(images);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(mLocalDatabase.getImages(key));
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
        return mLocalDatabase.getImages(key);
    }

    public void replyToComment(String userToken, final String comment, final String entityKey,
                               final String table, final String parentId, final int depth, final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.postCommentReply(userToken, comment, entityKey, table, parentId, depth)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<PojoComment>() {
                                @Override
                                public void onNext(PojoComment comment1) {
                                    mLocalDatabase.update(comment1);
                                    if (callback != null) {
                                        callback.onSuccess(comment1);
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    mLocalDatabase.addNewCommentReplyRequest(comment, entityKey, table,
                                            parentId, depth);
                                    showQueueMessage("Comment reply");
                                }
                            }
                    );
        } else {
            mLocalDatabase.addNewCommentReplyRequest(comment, entityKey, table,
                    parentId, depth);
            showQueueMessage("Comment reply");
        }
    }


    public void addComment(final String userToken, final String comment, final String entityKey,
                           final String table, final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.postComment(userToken, comment, entityKey, table)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoComment>() {
                        @Override
                        public void onNext(PojoComment object) {
                            mLocalDatabase.update(object);
                            if (callback != null) {
                                callback.onSuccess(object);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mLocalDatabase.addNewCommentRequest(comment, entityKey, table);
                            showQueueMessage("Comment");
                        }
                    });
        } else {
            mLocalDatabase.addNewCommentRequest(comment, entityKey, table);
            showQueueMessage("Comment");
        }
    }

    public void editComment(final String userToken, final String comment, final String commentKey,
                            final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.postCommentEdit(userToken, comment, commentKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoComment>() {
                        @Override
                        public void onNext(PojoComment object) {
                            mLocalDatabase.update(object);
                            if (callback != null) {
                                callback.onSuccess(object);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mLocalDatabase.addNewCommentEditRequest(comment, commentKey);
                            showQueueMessage("Comment edit");
                        }
                    });
        } else {
            mLocalDatabase.addNewCommentEditRequest(comment, commentKey);
            showQueueMessage("Comment edit");
        }
    }

    public void addRating(final String userToken, final int stars, final int yds, final String entityKey,
                          final String entityName, final Callback<Rating> callback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.postRating(userToken, stars, yds, entityKey, entityName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoRating>() {
                        @Override
                        public void onNext(PojoRating object) {
                            mLocalDatabase.update(object);
                            if (callback != null) {
                                callback.onSuccess(object);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mLocalDatabase.addNewRatingRequest(stars, yds, entityKey, entityName);
                            showQueueMessage("Rating");
                            if (callback != null) {
                                callback.onFailure();
                            }
                        }
                    });
        } else {
            mLocalDatabase.addNewRatingRequest(stars, yds, entityKey, entityName);
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
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getRatings(entityKey).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<PojoRating>>() {
                        @Override
                        public void onNext(List<PojoRating> object) {
                            mLocalDatabase.updateRatings(object);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(mLocalDatabase.getRatings(entityKey));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                            showGetFailure("ratings");
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return mLocalDatabase.getRatings(entityKey);
    }

    public Area getArea(String areaKey, final Callback<Area> updateCallback) {
        if (areaKey == null || areaKey.isEmpty()) {
            return null;
        }
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getArea(areaKey, null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoArea>() {
                        @Override
                        public void onNext(PojoArea areas) {
                            mLocalDatabase.update(areas);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(areas);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("area");
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
        return mLocalDatabase.getArea(areaKey);
    }

    public Route getRoute(String entityKey, final Callback<Route> updateCallback) {
        if (entityKey == null || entityKey.isEmpty()) {
            return null;
        }
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getRoute(entityKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoRoute>() {
                        @Override
                        public void onNext(PojoRoute routes) {
                            mLocalDatabase.update(routes);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(routes);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("route");
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
        return mLocalDatabase.getRoute(entityKey);
    }

    public Observable<List<Datable>> recentActivity(String entityKey, List<String> areaIds, List<String> routeIds) {
        return Observable.create(observableEmitter -> {
            observableEmitter.onNext(mLocalDatabase.getRecentActivity(entityKey,
                    (areaIds != null && !areaIds.isEmpty()) ? areaIds.toArray(new String[areaIds.size()]) : null,
                    (routeIds != null && !routeIds.isEmpty()) ? routeIds.toArray(new String[routeIds.size()]) : null));
            if (NetworkUtil.isConnected(mApplicationContext)) {
                mRestApi.getRecentActivity(entityKey,
                        areaIds != null ? areaIds : Collections.emptyList(),
                        routeIds != null ? routeIds : Collections.emptyList())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new EntityRequestObserver<List<Datable>>() {
                            @Override
                            public void onNext(List<Datable> objects) {
                                mLocalDatabase.updateDatables(objects);
                                observableEmitter.onNext(objects);
                                observableEmitter.onComplete();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                showGetFailure("recent activity");
                                observableEmitter.onError(throwable);
                                throwable.printStackTrace();
                            }
                        });
            } else {

            }
        });
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
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getRecentActivity(entityKey, areaList, routeList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<Datable>>() {
                        @Override
                        public void onNext(List<Datable> objects) {
                            mLocalDatabase.updateDatables(objects);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(objects);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("recent activity");
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
        return mLocalDatabase.getRecentActivity(entityKey,
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
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getArea(null, areaName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<PojoArea>() {
                        @Override
                        public void onNext(PojoArea area) {
                            mLocalDatabase.update(area);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(area);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("area by name \"" + areaName + "\"");
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
        return mLocalDatabase.getAreaByName(areaName);
    }

    public List<Route> getRoutes(final String[] routeIds, final Callback<List<Route>> updateCallback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getRoutes(routeIds)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<PojoRoute>>() {
                        @Override
                        public void onNext(List<PojoRoute> routes) {
                            mLocalDatabase.updateRoutes(routes);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(mLocalDatabase.getRoutes(routeIds));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            showGetFailure("routes");
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

        return mLocalDatabase.getRoutes(routeIds);
    }

    public Observable<Area> observeArea(final String areaId) {
        return Observable.create(emitter -> {
                emitter.onNext(mLocalDatabase.getArea(areaId));
                    if (NetworkUtil.isConnected(mApplicationContext)) {
                        mRestApi.getArea(areaId, null)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new EntityRequestObserver<PojoArea>() {
                                    @Override
                                    public void onNext(PojoArea area) {
                                        mLocalDatabase.update(area);
                                        emitter.onNext(area);
                                        emitter.onComplete();
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        showGetFailure("areas");
                                        emitter.onComplete();
                                    }
                                });
                    }
                }
        );
    }

    public List<Area> getAreas(final String[] areaIds, final Callback<List<Area>> updateCallback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.getAreas(areaIds)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<List<PojoArea>>() {
                        @Override
                        public void onNext(List<PojoArea> areas) {
                            mLocalDatabase.updateAreas(areas);
                            if (updateCallback != null) {
                                updateCallback.onSuccess(mLocalDatabase.getAreas(areaIds));
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            if (updateCallback != null) {
                                updateCallback.onFailure();
                            }
                            showGetFailure("areas");
                        }
                    });
        } else {
            if (updateCallback != null) {
                updateCallback.onFailure();
            }
        }
        return mLocalDatabase.getAreas(areaIds);
    }


}
