package com.cragchat.mobile.repository;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
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

    public Observable<List<Image>> observeImages(@NonNull final String key) {
        return getObserver(mLocalDatabase.getImages(key), mRestApi.getImages(key), "images");
    }

    public Observable<List<Rating>> observeRatings(@NonNull final String entityKey) {
        return getObserver(mLocalDatabase.getRatings(entityKey), mRestApi.getRatings(entityKey), "ratings");
    }

    public Observable<Route> observeRoute(@NonNull final String entityKey) {
        return getObserver(mLocalDatabase.getRoute(entityKey), mRestApi.getRoute(entityKey), "route");
    }

    public Observable<List<Datable>> observeRecentActivity(@NonNull final String entityKey, List<String> areaIds, List<String> routeIds) {
        return getObserver(mLocalDatabase.getRecentActivity(entityKey,
                (areaIds != null && !areaIds.isEmpty()) ? areaIds.toArray(new String[areaIds.size()]) : null,
                (routeIds != null && !routeIds.isEmpty()) ? routeIds.toArray(new String[routeIds.size()]) : null),
                mRestApi.getRecentActivity(entityKey,
                        areaIds != null ? areaIds : Collections.emptyList(),
                        routeIds != null ? routeIds : Collections.emptyList()),
                "recent activity");

    }

    public Observable<Area> observeAreaByName(@NonNull final String areaName) {
        return getObserver(mLocalDatabase.getAreaByName(areaName), mRestApi.getArea(null, areaName), "area by name");
    }

    public Observable<List<Route>> observeRoutes(@NonNull final String[] routeIds) {
        return getObserver(mLocalDatabase.getRoutes(routeIds), mRestApi.getRoutes(routeIds), "routes");
    }

    public Observable<Area> observeArea(@NonNull final String areaId) {
        return getObserver(mLocalDatabase.getArea(areaId), mRestApi.getArea(areaId, null), "area");
    }

    public Observable<List<Area>> observeAreas(@NonNull final String[] areaKeys) {
        return getObserver(mLocalDatabase.getAreas(areaKeys), mRestApi.getAreas(areaKeys), "ratings");
    }

    public Observable<List<Send>> observeSends(@NonNull final String entityKey) {
        return getObserver(mLocalDatabase.getSends(entityKey), mRestApi.getSends(entityKey), "sends");
    }


    public Observable<List<Comment>> observeComment(@NonNull final String entityKey, final String table) {
        return getObserver(mLocalDatabase.getComments(entityKey, table), mRestApi.getComments(entityKey), "comments");
    }

    public <T> Observable<T> getObserver(T localValue, Observable<T> networkObserver, String errorString) {
        return Observable.create(emitter -> {
            emitter.onNext(localValue);
            if (NetworkUtil.isConnected(mApplicationContext)) {
                networkObserver
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new EntityRequestObserver<T>() {
                            @Override
                            public void onNext(T areas) {
                                mLocalDatabase.update(areas);
                                emitter.onNext(areas);
                                emitter.onComplete();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                showGetFailure(errorString);
                                throwable.printStackTrace();
                                emitter.onError(throwable);
                            }
                        });
            } });
    }

    public Area getArea(@NonNull final String areaKey) {
        return mLocalDatabase.getArea(areaKey);
    }

    public Route getRoute(@NonNull final String routeKey) {
        return mLocalDatabase.getRoute(routeKey);
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
                            Type areaType = new TypeToken<Area>() {
                            }.getType();
                            Type routeType = new TypeToken<Route>() {
                            }.getType();
                            List<Object> objs = new ArrayList<>();
                            try {
                                JSONArray array = new JSONArray(object.string());
                                for (int i = 0; i < array.length(); i++) {
                                    String result = array.getString(i);
                                    if (result.contains("routes")) {
                                        Area area = gson.fromJson(result, areaType);
                                        mLocalDatabase.update(area);
                                        objs.add(area);
                                    } else {
                                        Route route = gson.fromJson(result, routeType);
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
                            new EntityRequestObserver<Comment>() {
                                @Override
                                public void onNext(Comment object) {
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
                            new EntityRequestObserver<Send>() {
                                @Override
                                public void onNext(Send send) {
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
                    .subscribe(new EntityRequestObserver<Image>() {
                        @Override
                        public void onNext(Image image1) {
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

    public void replyToComment(String userToken, final String comment, final String entityKey,
                               final String table, final String parentId, final int depth, final Callback<Comment> callback) {
        if (NetworkUtil.isConnected(mApplicationContext)) {
            mRestApi.postCommentReply(userToken, comment, entityKey, table, parentId, depth)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new EntityRequestObserver<Comment>() {
                                @Override
                                public void onNext(Comment comment1) {
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
                    .subscribe(new EntityRequestObserver<Comment>() {
                        @Override
                        public void onNext(Comment object) {
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
                    .subscribe(new EntityRequestObserver<Comment>() {
                        @Override
                        public void onNext(Comment object) {
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
                    .subscribe(new EntityRequestObserver<Rating>() {
                        @Override
                        public void onNext(Rating object) {
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

}
