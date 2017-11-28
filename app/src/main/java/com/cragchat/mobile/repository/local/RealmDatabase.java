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
import com.cragchat.mobile.model.realm.RealmArea;
import com.cragchat.mobile.model.realm.RealmComment;
import com.cragchat.mobile.model.realm.RealmDerivable;
import com.cragchat.mobile.model.realm.RealmImage;
import com.cragchat.mobile.model.realm.RealmNewCommentEditRequest;
import com.cragchat.mobile.model.realm.RealmNewCommentReplyRequest;
import com.cragchat.mobile.model.realm.RealmNewCommentRequest;
import com.cragchat.mobile.model.realm.RealmNewCommentVoteRequest;
import com.cragchat.mobile.model.realm.RealmNewImageRequest;
import com.cragchat.mobile.model.realm.RealmNewRatingRequest;
import com.cragchat.mobile.model.realm.RealmNewSendRequest;
import com.cragchat.mobile.model.realm.RealmRating;
import com.cragchat.mobile.model.realm.RealmRoute;
import com.cragchat.mobile.model.realm.RealmSend;

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

    public RealmDatabase() {
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .schemaVersion(1)
                .name("cragchat.realm")
                .build());
        //Realm.deleteRealm(Realm.getDefaultConfiguration());
        mRealm = Realm.getDefaultInstance();
    }


    public void close() {
        mRealm.close();
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
            public void execute(Realm realm) {
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
    public void update(final PojoImage image) {
        insertOrUpdate(RealmImage.from(image));
    }

    @Override
    public void update(final PojoSend send) {
        insertOrUpdate(RealmSend.from(send));
    }

    @Override
    public void updateImages(final List<PojoImage> image) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoImage comment : image) {
                    realm.insertOrUpdate(RealmImage.from(comment));
                }
            }
        });
    }

    @Override
    public void updateDatables(final List<Datable> datables) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Datable i : datables) {
                    if (i instanceof PojoRating) {
                        realm.insertOrUpdate(RealmRating.from((PojoRating) i));
                    } else if (i instanceof PojoComment) {
                        realm.insertOrUpdate(RealmComment.from((PojoComment) i));
                    } else if (i instanceof PojoImage) {
                        realm.insertOrUpdate(RealmImage.from((PojoImage) i));
                    } else {
                        realm.insertOrUpdate(RealmSend.from((PojoSend) i));
                    }
                }
            }
        });
    }

    @Override
    public void updateSends(final List<PojoSend> sends) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoSend i : sends) {
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
    public void update(final PojoRoute route) {
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

    private static final String QUEUE = "QUEUE";

    @Override
    public void addNewRatingRequest(final int stars, final int yds, final String entityKey) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewRatingRequest rating = realm.createObject(RealmNewRatingRequest.class);
                rating.setStars(stars);
                rating.setYds(yds);
                rating.setEntityKey(entityKey);
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
    public void addNewImageRequest(final String captionString, final String entityKey, final String entityType, final String fileUri) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewImageRequest req = realm.createObject(RealmNewImageRequest.class);
                req.setCaptionString(captionString);
                req.setEntityKey(entityKey);
                req.setFilePath(fileUri);
                req.setEntityType(entityType);
            }
        });
    }

    @Override
    public void addNewRealmSendRequest(final String entityKey, final int pitches,
                                       final int attempts, final String sendType, final String climbingStyle) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmNewSendRequest send = realm.createObject(RealmNewSendRequest.class);
                send.setEntityKey(entityKey);
                send.setPitches(pitches);
                send.setAttempts(attempts);
                send.setSendType(sendType);
                send.setClimbingStyle(climbingStyle);
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
    public void updateComments(final List<PojoComment> comments) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoComment comment : comments) {
                    realm.insertOrUpdate(RealmComment.from(comment));
                }
            }
        });

    }

    @Override
    public void updateRoutes(final List<PojoRoute> routes) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoRoute route : routes) {
                    realm.insertOrUpdate(RealmRoute.from(route));
                }
            }
        });
    }

    @Override
    public void update(final PojoArea area) {
        insertOrUpdate(RealmArea.from(area));
    }

    @Override
    public void update(final PojoRating rating) {
        insertOrUpdate(RealmRating.from(rating));
    }

    @Override
    public void update(PojoComment comment) {
        insertOrUpdate(RealmComment.from(comment));
    }

    @Override
    public void updateRatings(final List<PojoRating> ratings) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoRating rating : ratings) {
                    realm.insertOrUpdate(RealmRating.from(rating));
                }
            }
        });
    }

    @Override
    public void updateAreas(final List<PojoArea> areas) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (PojoArea i : areas) {
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

    private void insertOrUpdate(final List<Object> object, final Class<? extends RealmDerivable> clazz) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Object i : object) {

                }
            }
        });
    }

    /*
    public static void create(final Context context) {
        Realm realm = Realm.getDefaultInstance();
        System.out.println("logged in successfully as timsqdev");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void onSuccess(Realm realm) {
                System.out.println("Creating ozone and other areas");
                RealmArea ozone = new RealmArea("Ozone", null, "0", "0", null, new RealmList<String>(), new RealmList<String>());
                ozone = realm.copyToRealm(ozone);
                //RealmArea ozone = realm.where(RealmArea.class).equalTo("name", "Ozone").findFirst();

                Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.walls));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] params = line.split("#");
                    RealmArea newArea = new RealmArea(params[0], params[0] + ozone.getName(), "0", "0", ozone, new RealmList<RealmArea>(), new RealmList<RealmRoute>());
                    newArea = realm.copyToRealm(newArea);
                    ozone.getSubAreas().add(newArea);
                }
                scanner.close();

                System.out.println("Creating routes");
                Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.routes));
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] params = line.split("#");

                    String type = params[3];
                    if (type.contains("Top Rope")) {
                        type = "Trad"; // only one rout ehas tope rope and its other type is trad
                    }

                    RealmArea parent = realm.where(RealmArea.class).equalTo("name", params[2]).findFirst();
                    RealmRoute route = new RealmRoute(params[0] + parent.getName(), params[0], type, "0", "0", parent.getFilename(), 0, 0);
                    route = realm.copyToRealm(route);
                    parent.getRoutes().add(new String(route.getFilename()));
                    ozone.getRoutes().add(new String(route.getFilename()));
                }

                System.out.println("Finished copying to realm");
                List<RealmArea> areas = realm.where(RealmArea.class).findAll();
                System.out.println("Areas(" + areas.size() + "): ");
                for (RealmArea area : areas) {
                    System.out.println("\t" + area.getName());
                    for (String subArea : area.getSubAreas()) {
                        System.out.println("\t\t" + subArea.getValue());
                    }
                    for (String route : area.getRoutes()) {
                        System.out.println("\t\t" + route.getValue());
                    }
                }
                List<RealmRoute> routes = realm.where(RealmRoute.class).findAll();
                System.out.println("Routes(" + routes.size() + "):  ");
                for (RealmRoute route : routes) {
                    System.out.println("\t" + route.getName());
                }
            }
        });
    }*/

}
