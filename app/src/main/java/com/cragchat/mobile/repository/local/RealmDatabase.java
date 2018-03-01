package com.cragchat.mobile.repository.local;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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

    public List getQueryMatches(String query) {
        List results = mRealm.copyFromRealm(mRealm.where(RealmArea.class).contains(RealmArea.FIELD_NAME, query, Case.INSENSITIVE).findAll());
        results.addAll(mRealm.copyFromRealm(mRealm.where(RealmRoute.class).contains(RealmRoute.FIELD_NAME, query, Case.INSENSITIVE).findAll()));
        return results;
    }

    @Override
    public Route getRoute(String entityKey) {
        return mRealm.where(RealmRoute.class).equalTo(RealmRoute.FIELD_KEY, entityKey).findFirst();
    }

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

    @Override
    public List<Route> getRoutes(String[] routeIds) {
        List<Route> routes = new ArrayList<>();
        routes.addAll(mRealm.where(RealmRoute.class).in(RealmRoute.FIELD_KEY, routeIds).findAll());
        return routes;
    }

    @Override
    public List<Area> getAreas(String[] areaIds) {
        List<Area> areas = new ArrayList<>();
        areas.addAll(mRealm.where(RealmArea.class).in(RealmArea.FIELD_KEY, areaIds).findAll());
        return areas;
    }

    @Override
    public List<Rating> getRatings(String entityKey) {
        List<Rating> ratings = new ArrayList<>();
        ratings.addAll(mRealm.where(RealmRating.class).equalTo(RealmRating.FIELD_ENTITY_KEY, entityKey).findAll());
        return ratings;
    }

    @Override
    public List<Image> getImages(String entityKey) {
        List<Image> images = new ArrayList<>();
        images.addAll(mRealm.where(RealmImage.class).equalTo(RealmImage.FIELD_ENTITY_KEY, entityKey).findAll());
        return images;
    }

    @Override
    public List<Comment> getComments(String entityKey, String table) {
        List<Comment> list = new ArrayList<>();
        list.addAll(mRealm.where(RealmComment.class)
                .equalTo(RealmComment.FIELD_ENTITY_ID, entityKey)
                .equalTo(RealmComment.FIELD_TABLE, table)
                .findAll());
        return list;
    }

    @Override
    public List<Send> getSends(String entityKey) {
        List<Send> images = new ArrayList<>();
        images.addAll(mRealm.where(RealmSend.class).equalTo(RealmSend.FIELD_ENTITY_KEY, entityKey).findAll());
        return images;
    }

    @Override
    public List<NewSendRequest> getNewSendRequests() {
        final List<NewSendRequest> sends = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                sends.addAll(realm.copyFromRealm(realm.where(RealmNewSendRequest.class).findAll()));
                realm.delete(RealmNewSendRequest.class);
            }
        });

        return sends;
    }

    @Override
    public List<NewCommentEditRequest> getNewCommentEditRequests() {
        final List<NewCommentEditRequest> sends = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sends.addAll(realm.copyFromRealm(realm.where(RealmNewCommentEditRequest.class).findAll()));
                realm.delete(RealmNewCommentEditRequest.class);
            }
        });
        return sends;
    }

    @Override
    public List<NewCommentReplyRequest> getNewCommentReplyRequests() {
        final List<NewCommentReplyRequest> sends = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sends.addAll(realm.copyFromRealm(realm.where(RealmNewCommentReplyRequest.class).findAll()));
                realm.delete(RealmNewCommentReplyRequest.class);
            }
        });
        return sends;
    }

    @Override
    public List<NewCommentRequest> getNewCommentRequests() {
        final List<NewCommentRequest> sends = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sends.addAll(realm.copyFromRealm(realm.where(RealmNewCommentRequest.class).findAll()));
                realm.delete(RealmNewCommentRequest.class);
            }
        });
        return sends;
    }

    @Override
    public List<NewCommentVoteRequest> getNewCommentVoteRequests() {
        final List<NewCommentVoteRequest> sends = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sends.addAll(realm.copyFromRealm(realm.where(RealmNewCommentVoteRequest.class).findAll()));
                realm.delete(RealmNewCommentVoteRequest.class);
            }
        });
        return sends;
    }

    @Override
    public List<NewImageRequest> getNewImageRequsts() {
        final List<NewImageRequest> sends = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sends.addAll(realm.copyFromRealm(realm.where(RealmNewImageRequest.class).findAll()));
                realm.delete(RealmNewImageRequest.class);
            }
        });
        return sends;
    }

    @Override
    public List<NewRatingRequest> getNewRatingRequests() {
        final List<NewRatingRequest> sends = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sends.addAll(realm.copyFromRealm(realm.where(RealmNewRatingRequest.class).findAll()));
                realm.delete(RealmNewRatingRequest.class);
            }
        });
        return sends;
    }

    @Override
    public void update(Object value) {
        if (value instanceof Send) {
            update((Send) value);
        } else if (value instanceof Image) {
            update((Image) value);
        }else if (value instanceof Area) {
            update((Area) value);
        }else if (value instanceof Rating) {
            update((Rating) value);
        }else if (value instanceof Comment) {
            update((Comment) value);
        }else if (value instanceof Route) {
            update((Route) value);
        } else if (value instanceof List) {
            List list = (List) value;
            if (list.size() > 0) {
                Object obj = list.get(0);
                if (obj instanceof Send) {
                    updateSends((List<Send>) list);
                } else if (obj instanceof Image) {
                    updateImages((List<Image>) list);
                }else if (obj instanceof Area) {
                    updateAreas((List<Area>) list);
                }else if (obj instanceof Rating) {
                    updateRatings((List<Rating>) list);
                }else if (obj instanceof Comment) {
                    updateComments((List<Comment>) list);
                }else if (obj instanceof Route) {
                    updateRoutes((List<Route>) list);
                } else {
                    System.exit(487639);
                }
            }
        } else {
            System.exit(488639);
        }
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
    public void updateImages(final List<Image> image) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Image comment : image) {
                    realm.insertOrUpdate(RealmImage.from(comment));
                }
            }
        });
    }

    @Override
    public void updateDatables(final List<Datable> datables) {
        String t = Thread.currentThread().getName();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Datable i : datables) {
                    if (i instanceof Rating) {
                        realm.insertOrUpdate(RealmRating.from((Rating) i));
                    } else if (i instanceof Comment) {
                        realm.insertOrUpdate(RealmComment.from((Comment) i));
                    } else if (i instanceof Image) {
                        realm.insertOrUpdate(RealmImage.from((Image) i));
                    } else {
                        realm.insertOrUpdate(RealmSend.from((Send) i));
                    }
                }
            }
        });
    }

    @Override
    public void updateSends(final List<Send> sends) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Send i : sends) {
                    realm.insertOrUpdate(RealmSend.from(i));
                }
            }
        });
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
    public void addNewCommentRequest(final String comment, final String entityKey,
                                     final String table) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewCommentRequest req = realm.createObject(RealmNewCommentRequest.class);
                req.setComment(comment);
                req.setTable(table);
                req.setEntityKey(entityKey);
            }
        });
    }

    @Override
    public void addNewRatingRequest(final int stars, final int yds, final String entityKey, final String entityName) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewRatingRequest rating = realm.createObject(RealmNewRatingRequest.class);
                rating.setStars(stars);
                rating.setYds(yds);
                rating.setEntityKey(entityKey);
                rating.setEntityName(entityName);
            }
        });
    }

    @Override
    public void addNewCommentEditRequest(final String comment, final String commentKey) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewCommentEditRequest req = realm.createObject(RealmNewCommentEditRequest.class);
                req.setComment(comment);
                req.setCommentKey(commentKey);
            }
        });
    }

    @Override
    public void addNewImageRequest(final String captionString, final String entityKey, final String entityType, final String fileUri, final String entityName) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewImageRequest req = realm.createObject(RealmNewImageRequest.class);
                req.setCaptionString(captionString);
                req.setEntityKey(entityKey);
                req.setFilePath(fileUri);
                req.setEntityType(entityType);
                req.setEntityName(entityName);
            }
        });
    }

    @Override
    public void addNewSendRequest(final String entityKey, final int pitches,
                                  final int attempts, final String sendType, final String climbingStyle, final String entityName) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewSendRequest send = realm.createObject(RealmNewSendRequest.class);
                send.setEntityKey(entityKey);
                send.setPitches(pitches);
                send.setAttempts(attempts);
                send.setSendType(sendType);
                send.setClimbingStyle(climbingStyle);
                send.setEntityName(entityName);
            }
        });
    }

    @Override
    public void addNewCommentVoteRequest(final String vote, final String commentKey) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewCommentVoteRequest req = realm.createObject(RealmNewCommentVoteRequest.class);
                req.setVote(vote);
                req.setCommentKey(commentKey);
            }
        });
    }

    @Override
    public void addNewCommentReplyRequest(final String comment, final String entityKey,
                                          final String table, final String parentId, final int depth) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewCommentReplyRequest req = realm.createObject(RealmNewCommentReplyRequest.class);
                req.setComment(comment);
                req.setTable(table);
                req.setParentId(parentId);
                req.setDepth(depth);
                req.setEntityKey(entityKey);
            }
        });
    }


    @Override
    public void updateComments(final List<Comment> comments) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Comment comment : comments) {
                    realm.insertOrUpdate(RealmComment.from(comment));
                }
            }
        });

    }

    @Override
    public void updateRoutes(final List<Route> routes) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Route route : routes) {
                    realm.insertOrUpdate(RealmRoute.from(route));
                }
            }
        });
    }

    @Override
    public void update(final Area area) {
        insertOrUpdate(RealmArea.from(area));
    }

    @Override
    public void update(final Rating rating) {
        insertOrUpdate(RealmRating.from(rating));
    }

    @Override
    public void update(Comment comment) {
        insertOrUpdate(RealmComment.from(comment));
    }

    @Override
    public void updateRatings(final List<Rating> ratings) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Rating rating : ratings) {
                    Log.d("Rating", rating.toString() + " REALM:" + RealmRating.from(rating).toString());
                    realm.insertOrUpdate(RealmRating.from(rating));
                }
            }
        });
    }

    @Override
    public void updateAreas(final List<Area> areas) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Area i : areas) {
                    realm.insertOrUpdate(RealmArea.from(i));
                }
            }
        });
    }

    private void insertOrUpdate(final RealmObject object) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(object);
            }
        });
    }



}
