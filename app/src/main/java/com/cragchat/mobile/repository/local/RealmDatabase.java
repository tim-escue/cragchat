package com.cragchat.mobile.repository.local;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

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
import com.cragchat.mobile.ui.model.realm.RealmArea;
import com.cragchat.mobile.ui.model.realm.RealmComment;
import com.cragchat.mobile.ui.model.realm.RealmImage;
import com.cragchat.mobile.ui.model.realm.RealmNewCommentEditRequest;
import com.cragchat.mobile.ui.model.realm.RealmNewCommentReplyRequest;
import com.cragchat.mobile.ui.model.realm.RealmNewCommentRequest;
import com.cragchat.mobile.ui.model.realm.RealmNewCommentVoteRequest;
import com.cragchat.mobile.ui.model.realm.RealmNewImageRequest;
import com.cragchat.mobile.ui.model.realm.RealmNewRatingRequest;
import com.cragchat.mobile.ui.model.realm.RealmNewSendRequest;
import com.cragchat.mobile.ui.model.realm.RealmRating;
import com.cragchat.mobile.ui.model.realm.RealmRoute;
import com.cragchat.mobile.ui.model.realm.RealmSend;
import com.cragchat.mobile.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by timde on 9/28/2017.
 */

public class RealmDatabase implements CragChatDatabase {

    private Realm mRealm;

    public RealmDatabase(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(1)
                .name("cragchat.realm")
                .build());
        mRealm = Realm.getDefaultInstance();
    }

    @SuppressWarnings("unchecked")
    public List getQueryMatches(String query) {
        List results = mRealm.copyFromRealm(mRealm.where(RealmArea.class).contains(RealmArea.FIELD_NAME, query, Case.INSENSITIVE).findAll());
        results.addAll(mRealm.copyFromRealm(mRealm.where(RealmRoute.class).contains(RealmRoute.FIELD_NAME, query, Case.INSENSITIVE).findAll()));
        return results;
    }

    @Override
    public Route getRoute(String entityKey) {
        return mRealm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, entityKey).findFirst();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Route> getRoutes(String[] routeIds) {
        return (List<Route>)(List<?>)mRealm.where(RealmRoute.class).in(RealmRoute.FIELD_KEY, routeIds).findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Area> getAreas(String[] areaIds) {
        return (List<Area>)(List<?>)mRealm.where(RealmArea.class).in(RealmArea.FIELD_KEY, areaIds).findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Rating> getRatings(String entityKey) {
        return (List<Rating>)(List<?>)mRealm.where(RealmRating.class).
                equalTo(RealmRating.FIELD_ENTITY_KEY, entityKey).findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Image> getImages(String entityKey) {
        return (List<Image>)(List<?>)mRealm.where(RealmImage.class).
                equalTo(RealmImage.FIELD_ENTITY_KEY, entityKey).findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> getComments(String entityKey, String table) {
        return (List<Comment>)(List<?>)mRealm.where(RealmComment.class)
                .equalTo(RealmComment.FIELD_ENTITY_ID, entityKey)
                .equalTo(RealmComment.FIELD_TABLE, table).findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Send> getSends(String entityKey) {
        return (List<Send>)(List<?>)mRealm.where(RealmSend.class).
                equalTo(RealmSend.FIELD_ENTITY_KEY, entityKey).findAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NewSendRequest> getNewSendRequests() {
        return getRequestAndDelete(RealmNewSendRequest.class);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getRequestAndDelete(Class<? extends RealmModel> realmType) {
        List<T> list = (List<T>) mRealm.copyFromRealm(mRealm.where(realmType).findAll());
        doInBackGround(realm -> realm.delete(realmType));
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NewCommentRequest> getNewCommentRequests() {
        return getRequestAndDelete(RealmNewCommentRequest.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NewCommentEditRequest> getNewCommentEditRequests() {
        return getRequestAndDelete(RealmNewCommentEditRequest.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NewCommentReplyRequest> getNewCommentReplyRequests() {
        return getRequestAndDelete(RealmNewCommentReplyRequest.class);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NewCommentVoteRequest> getNewCommentVoteRequests() {
        return getRequestAndDelete(RealmNewCommentVoteRequest.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NewImageRequest> getNewImageRequsts() {
        return getRequestAndDelete(RealmNewImageRequest.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NewRatingRequest> getNewRatingRequests() {
        return getRequestAndDelete(RealmNewRatingRequest.class);
    }
    
    @Override
    public void update(final Image image) {
        insertOrUpdate(RealmImage.from(image));
    }

    @Override
    public void update(final Send send) {
        insertOrUpdate(RealmSend.from(send));
    }

    @Override
    public void updateImages(final List<Image> images) {
        doInBackGround(realm -> realm.insertOrUpdate(FormatUtil.transform(images, RealmImage::from)));
    }

    private void doInBackGround(Realm.Transaction transaction) {
        AsyncTask.execute(() -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(transaction);
            realm.close();
        });
    }

    @Override
    public void updateDatables(final List<Datable> datables) {
        doInBackGround(realm -> {
                for (int i = 0; i < datables.size(); i++) {
                    Datable datable = datables.get(i);
                    if (datable instanceof Rating) {
                        realm.insertOrUpdate(RealmRating.from((Rating) datable));
                    } else if (datable instanceof Comment) {
                        realm.insertOrUpdate(RealmComment.from((Comment) datable));
                    } else if (datable instanceof Image) {
                        realm.insertOrUpdate(RealmImage.from((Image) datable));
                    } else {
                        realm.insertOrUpdate(RealmSend.from((Send) datable));
                    }
                }
        });
    }

    @Override
    public void updateSends(final List<Send> sends) {
        doInBackGround(realm -> realm.insertOrUpdate(FormatUtil.transform(sends, RealmSend::from)));
    }

    @Override
    public RealmArea getArea(String areaKey) {
        return mRealm.where(RealmArea.class).equalTo(RealmArea.FIELD_KEY, areaKey).findFirst();
    }

    @Override
    public RealmArea getAreaByName(String areaName) {
        return mRealm.where(RealmArea.class).equalTo(RealmArea.FIELD_NAME, areaName).findFirst();
    }

    @Override
    public void update(final Route route) {
        insertOrUpdate(RealmRoute.from(route));
    }

    @Override
    public void addNewCommentRequest(final String userToken, final String comment, final String entityKey,
                                     final String table) {
        doInBackGround(realm -> realm.insert(new RealmNewCommentRequest(comment, entityKey, table, userToken)));
    }

    @Override
    public void addNewRatingRequest(final String userToken, final int stars, final int yds, final String entityKey, final String entityName) {
        doInBackGround(realm -> realm.insert(new RealmNewRatingRequest(userToken, stars, yds, entityKey, entityName)));
    }

    @Override
    public void addNewCommentEditRequest(final String userToken, final String comment, final String commentKey) {
        doInBackGround(realm -> realm.insert(new RealmNewCommentEditRequest(userToken, comment, commentKey)));
    }

    @Override
    public void addNewImageRequest(final String userToken, final String captionString, final String entityKey, final String entityType, final String fileUri, final String entityName) {
        doInBackGround(realm -> realm.insert(new RealmNewImageRequest(captionString, entityKey, entityType, fileUri, entityName, userToken)));
    }

    @Override
    public void addNewSendRequest(final String userToken, final String entityKey, final int pitches,
                                  final int attempts, final String sendType,
                                  final String climbingStyle, final String entityName) {
        doInBackGround(realm -> realm.insert(new RealmNewSendRequest(entityKey, pitches, attempts, sendType, climbingStyle, entityName, userToken)));
    }

    @Override
    public void addNewCommentVoteRequest(final String userToken, final String vote, final String commentKey) {
        doInBackGround(realm -> realm.insert(new RealmNewCommentVoteRequest(vote, commentKey, userToken)));
    }

    @Override
    public void addNewCommentReplyRequest(final String userToken, final String comment, final String entityKey,
                                          final String table, final String parentId, final int depth) {
        doInBackGround(realm -> realm.insert(new RealmNewCommentReplyRequest(comment, entityKey, table, parentId, depth, userToken)));
    }


    @Override
    public void updateComments(final List<Comment> comments) {
        doInBackGround(realm -> realm.insertOrUpdate(FormatUtil.transform(comments, RealmComment::from)));
    }

    @Override
    public void updateRoutes(final List<Route> routes) {
        doInBackGround(realm -> realm.insertOrUpdate(FormatUtil.transform(routes, RealmRoute::from)));
    }

    @Override
    public void update(final Area area) {
        doInBackGround(realm -> realm.insertOrUpdate(RealmArea.from(area)));
    }

    @Override
    public void update(final Rating rating) {
        doInBackGround(realm -> realm.insertOrUpdate(RealmRating.from(rating)));
    }

    @Override
    public void update(Comment comment) {
        doInBackGround(realm -> realm.insertOrUpdate(RealmComment.from(comment)));
    }

    @Override
    public void updateRatings(final List<Rating> ratings) {
        doInBackGround(realm -> realm.insertOrUpdate(FormatUtil.transform(ratings, RealmRating::from)));
    }

    @Override
    public void updateAreas(final List<Area> areas) {
        doInBackGround(realm -> realm.insertOrUpdate(FormatUtil.transform(areas, RealmArea::from)));
    }

    private void insertOrUpdate(final RealmObject object) {
        doInBackGround(realm -> realm.insertOrUpdate(object));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List getRecentActivity(String entityKey, String[] routeIds, String[] areaIds) {
        List results = mRealm.copyFromRealm(mRealm.where(RealmComment.class).equalTo(RealmComment.FIELD_ENTITY_ID, entityKey, Case.INSENSITIVE).findAll());
        if (routeIds != null) {
            results.addAll(mRealm.copyFromRealm(mRealm.where(RealmComment.class).in(RealmComment.FIELD_ENTITY_ID, routeIds, Case.INSENSITIVE).findAll()));
            results.addAll(mRealm.copyFromRealm(mRealm.where(RealmRating.class).in(RealmRating.FIELD_ENTITY_KEY, routeIds, Case.INSENSITIVE).findAll()));
            results.addAll(mRealm.copyFromRealm(mRealm.where(RealmSend.class).in(RealmSend.FIELD_ENTITY_KEY, routeIds, Case.INSENSITIVE).findAll()));
            results.addAll(mRealm.copyFromRealm(mRealm.where(RealmImage.class).in(RealmImage.FIELD_ENTITY_KEY, routeIds, Case.INSENSITIVE).findAll()));
        }
        if (areaIds != null) {
            results.addAll(mRealm.copyFromRealm(mRealm.where(RealmComment.class).in(RealmComment.FIELD_ENTITY_ID, areaIds, Case.INSENSITIVE).findAll()));
            results.addAll(mRealm.copyFromRealm(mRealm.where(RealmImage.class).in(RealmImage.FIELD_ENTITY_KEY, areaIds, Case.INSENSITIVE).findAll()));
        }

        results.addAll(mRealm.copyFromRealm(mRealm.where(RealmRating.class).equalTo(RealmRating.FIELD_ENTITY_KEY, entityKey, Case.INSENSITIVE).findAll()));
        results.addAll(mRealm.copyFromRealm(mRealm.where(RealmSend.class).equalTo(RealmSend.FIELD_ENTITY_KEY, entityKey, Case.INSENSITIVE).findAll()));
        results.addAll(mRealm.copyFromRealm(mRealm.where(RealmImage.class).equalTo(RealmImage.FIELD_ENTITY_KEY, entityKey, Case.INSENSITIVE).findAll()));
        return results;
    }

}
