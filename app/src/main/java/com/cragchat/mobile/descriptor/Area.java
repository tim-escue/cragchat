package com.cragchat.mobile.descriptor;

import android.content.Context;

import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.comments.CommentManager;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.ArrayList;
import java.util.List;

public class Area extends Displayable {

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_LATITUDE = 2;
    public static final int COLUMN_LONGITUDE = 3;
    public static final int COLUMN_REVISION = 4;

    private String name;
    private double latitude;
    private double longitude;
    private int id;
    private int routeCount;

    private int areaCount;
    private int commentCount;
    private List<Area> subAreas;
    private List<Displayable> routes;

    public Area(int id, String name, double latitude, double longitude, int revision) {
        super(id, revision);
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.routeCount = -1;
        this.areaCount = -1;
        this.commentCount = -1;
    }

    public Area() {
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public List<Area> getSubAreas() {
        return subAreas;
    }

    public List<Displayable> getRoutes() {
        return routes;
    }

    public void loadStatistics(Context context) {
        LocalDatabase db = LocalDatabase.getInstance(context);
        routes = db.findRoutesWithin(this);
        routeCount = routes.size();
        subAreas = db.findAreasWithin(this);
        areaCount = subAreas.size();
        commentCount = 0;
        for (Area area : subAreas) {
            List<Comment> parentComments = db.getComments(area.getId(), "");
            CommentManager manager = new CommentManager();

            //Log.d("ROUTE_COMMENTS", "Attempting to load comments for " + getArguments().getString("display_id"));
            for (Comment i : parentComments) {
                manager.addComment(i);
            }
            manager.sortByScore();
            commentCount += manager.getCommentList().size();
        }
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRouteCount() {
        return routeCount;
    }

    public int getAreaCount() {
        return areaCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public int getId() {
        return id;
    }

   //N public FireBaseArea toFireBaseArea() {
//        List<Long> areas = new ArrayList<>(subAreas.size());
       // for (Area i : subAreas) {
           // areas.add(Long.valueOf(i.getId()));
       // }
       // List<Long> route = new ArrayList<>(routes.size());
        //for (Displayable i : routes) {
         //   route.add(Long.valueOf(i.getId()));
        //}
    //    return new FireBaseArea(name, latitude, longitude, null, null, );
   // }
}
