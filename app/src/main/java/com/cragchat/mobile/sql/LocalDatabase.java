package com.cragchat.mobile.sql;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.comments.Comment;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.descriptor.Image;
import com.cragchat.mobile.descriptor.Rating;
import com.cragchat.mobile.descriptor.Route;
import com.cragchat.mobile.descriptor.Send;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LocalDatabase {

    private static SQLiteDatabase db;
    private static LocalDatabase ref;
    public static final String BETA = "BETA";
    public static final String DISCUSSION = "DISCUSSION";

    /*
        Singleton Model - never needs to be more than one db object.
     */
    private LocalDatabase() {
        try {
            db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.cragchat.mobile/routedb", null);
            db.execSQL("CREATE TABLE IF NOT EXISTS ROUTES (ID INTEGER(11), NAME VARCHAR(255), ROUTE_TYPE VARCHAR(255), LATITUDE DOUBLE(12,8), LONGITUDE DOUBLE(12,8), REVISION INTEGER(11))");
            db.execSQL("CREATE TABLE IF NOT EXISTS AREAS (ID INTEGER(11), NAME VARCHAR(255), LATITUDE DOUBLE(12,8), LONGITUDE DOUBLE(12,8), REVISION INTEGER(11))");
            db.execSQL("CREATE TABLE IF NOT EXISTS IDS_DISPLAYABLE (ID INTEGER(11), PARENT_ID INTEGER(11), REVISION INTEGER(11))");
            db.execSQL("CREATE TABLE IF NOT EXISTS IMAGES (PARENT_ID INTEGER(11), CAPTION VARCHAR(500), AUTHOR VARCHAR(32),  NAME VARCHAR(255), DATE VARCHAR(255))");
            db.execSQL("CREATE TABLE IF NOT EXISTS BETA (SCORE INTEGER(11), ID INTEGER(11), DATE VARCHAR(100), TEXT VARCHAR(5000), DISPLAY_ID INTEGER(11)," +
                    " PARENT_ID INTEGER(11), DEPTH INTEGER(11), AUTHOR_NAME VARCHAR(255), TABLE_NAME VARCHAR(20))");
            db.execSQL("CREATE TABLE IF NOT EXISTS PENDING (SUBMISSION VARCHAR(11000))");
            db.execSQL("CREATE TABLE IF NOT EXISTS RATINGS (ROUTE_ID INTEGER(11), YDS INTEGER(11), STARS INTEGER(11)" +
                    ", DATE VARCHAR(255), USERNAME VARCHAR(100))");
            db.execSQL("CREATE TABLE IF NOT EXISTS SEND (ROUTE_ID INTEGER(11), PITCHES INTEGER(11)" +
                    ", DATE VARCHAR(255), SEND_TYPE VARCHAR(100), ATTEMPTS INTEGER(11), STYLE VARCHAR(32), USERNAME VARCHAR(32))");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LocalDatabase getInstance(Context context) {
        if (ref == null)
            ref = new LocalDatabase();
        //File f = context.getDatabasePath("/data/data/chat.crag.cragchat/routedb");
        //long dbSize = f.length();
        ////System.out.println("DATABASE SIZE:" + dbSize);
        return ref;
    }

    /*
        Pulls all data from remote server
     */
    public void updateAll(Activity act) {
        //Log.d("LocalDatabase", "UpdateAllIfNecessary");
        // Cursor c = db.rawQuery("SELECT * FROM AREAS", null);
        // if (c.getCount() == 0) {
        new UpdateLocalDisplayablesTask(this).execute();
        new UpdateLocalIdsTask(this).execute();
        new UpdateAllCommentsImagesTask(this).execute();
        new UpdateRatingsTask(act).execute();
        new UpdateSendsTask(act).execute();

        List<String> pending = getPending();
        //System.out.println("PENDINGSIZE:" + pending.size());
        if (pending.size() > 0) {
            new SendPending(act, pending).execute();
        }

        // }
        // c.close();
    }

    public void store(Context act, String str) {
        //System.out.println("STORING " + str);
        Toast.makeText(act, "Submission will be posted next time application obtains a data connection.", Toast.LENGTH_SHORT).show();
        ContentValues vals = new ContentValues();
        vals.put("SUBMISSION", str);
        db.insert("PENDING", null, vals);
    }

    public List<String> getPending() {
        List<String> comments = new LinkedList<>();
        Cursor c = query("SELECT * FROM PENDING");
        while (c.moveToNext()) {
            comments.add(c.getString(0));
        }
        c.close();
        db.execSQL("DELETE FROM PENDING");
        //Log.d("LocalDatabase", "COMMENTS TOTAL: " + comments.size());
        return comments;
    }

    public List<Comment> allBETA() {
        List<Comment> comments = new LinkedList<>();
        Cursor c = query("SELECT * FROM BETA");
        while (c.moveToNext()) {
            Comment cur = new Comment(c.getString(Comment.COLUMN_TEXT),
                    c.getInt(Comment.COLUMN_ID),
                    c.getInt(Comment.COLUMN_SCORE),
                    c.getString(Comment.COLUMN_DATE),
                    c.getInt(Comment.COLUMN_DISPLAY_ID),
                    c.getInt(Comment.COLUMN_PARENT_ID),
                    c.getInt(Comment.COLUMN_DEPTH),
                    c.getString(Comment.COLUMN_AUTHOR_NAME),
                    c.getString(Comment.COLUMN_TABLE_NAME));
            comments.add(cur);
            //Log.d("CommentFOUND:", cur.getId() + ":ID parentid:" + cur.getParent());
        }
        c.close();
        //Log.d("LocalDatabase", "COMMENTS TOTAL: " + comments.size());
        return comments;
    }

    public List<Comment> getCommentsForTable(int id, String table) {
        return getComments(id, " AND TABLE_NAME LIKE '" + table + "'");
    }

    public List<Comment> getAllComments(int id) {
        return getComments(id, "");
    }

    /*
        Recursively retrieves all comments for a Displayable
     */
    public List<Comment> getComments(int id, String paramString) {
        List<Comment> comments = new LinkedList<>();
        Cursor c = query("SELECT * FROM BETA WHERE DISPLAY_ID='" + id + "' AND DEPTH='0'" + paramString);
        int j = 0;
        while (c.moveToNext()) {
            Comment cur = new Comment(
                    c.getString(Comment.COLUMN_TEXT),
                    c.getInt(Comment.COLUMN_ID),
                    c.getInt(Comment.COLUMN_SCORE),
                    c.getString(Comment.COLUMN_DATE),
                    c.getInt(Comment.COLUMN_DISPLAY_ID),
                    c.getInt(Comment.COLUMN_PARENT_ID),
                    c.getInt(Comment.COLUMN_DEPTH),
                    c.getString(Comment.COLUMN_AUTHOR_NAME),
                    c.getString(Comment.COLUMN_TABLE_NAME));
            comments.add(cur);
            getCommentsFor(cur, paramString);
        }
        c.close();
        //Log.d("LocalDatabase", "parent comments found: " + comments.size());
        return comments;
    }

    /*
        Recursive part of function
     */
    private void getCommentsFor(Comment parent, String paramString) {
        Cursor c = query("SELECT * FROM BETA WHERE PARENT_ID='" + parent.getId()+ "'" + paramString);

        while (c.moveToNext()) {
            Comment current = new Comment(c.getString(Comment.COLUMN_TEXT),
                    c.getInt(Comment.COLUMN_ID), c.getInt(Comment.COLUMN_SCORE), c.getString(Comment.COLUMN_DATE),
                    c.getInt(Comment.COLUMN_DISPLAY_ID), c.getInt(Comment.COLUMN_PARENT_ID), c.getInt(Comment.COLUMN_DEPTH), c.getString(Comment.COLUMN_AUTHOR_NAME), c.getString(Comment.COLUMN_TABLE_NAME));
            parent.addChild(current);
            getCommentsFor(current, paramString);
        }
        c.close();
    }

    public List<Comment> getCommentsForProfile(Activity con, String username) {
        List<Comment> comments = new LinkedList<>();

        Cursor c = query("SELECT * FROM BETA WHERE AUTHOR_NAME='" + username + "'");
        while (c.moveToNext()) {
            Comment cur = new Comment(c.getString(Comment.COLUMN_TEXT),
                    c.getInt(Comment.COLUMN_ID), c.getInt(Comment.COLUMN_SCORE), c.getString(Comment.COLUMN_DATE),
                    c.getInt(Comment.COLUMN_DISPLAY_ID), c.getInt(Comment.COLUMN_PARENT_ID), c.getInt(Comment.COLUMN_DEPTH), c.getString(Comment.COLUMN_AUTHOR_NAME), c.getString(Comment.COLUMN_TABLE_NAME));
            comments.add(cur);
        }
        c.close();
        //Log.d("LocalDatabase", "Found " + comments.size() + " for user " + User.userName(con));
        return comments;
    }

    public List<Image> getImagesForProfile(Activity act, String username) {
        List<Image> images = new LinkedList<>();
        Cursor c = query("SELECT * FROM IMAGES WHERE AUTHOR='" + username + "'");
        while (c.moveToNext()) {
            Image image = new Image(c.getInt(Image.COLUMN_DISPLAY_ID), c.getString(Image.COLUMN_CAPTION), c.getString(Image.COLUMN_AUTHOR), c.getString(Image.COLUMN_NAME), c.getString(Image.COLUMN_DATE));
            images.add(image);
        }
        Log.d("IMAGESDATAASE", images.size() + "");
        c.close();
        return images;
    }

    public List<Image> getImagesFor(int id) {
        List<Image> images = new LinkedList<>();
        Cursor c = query("SELECT * FROM IMAGES WHERE PARENT_ID='" + id + "'");
        while (c.moveToNext()) {
            Image image = new Image(c.getInt(Image.COLUMN_DISPLAY_ID), c.getString(Image.COLUMN_CAPTION), c.getString(Image.COLUMN_AUTHOR),
                    c.getString(Image.COLUMN_NAME), c.getString(Image.COLUMN_DATE));
            images.add(image);
        }
        c.close();
        //Log.d("LocalDatabase", "parent comments found: " + images.size());
        return images;
    }

    public List<Rating> getRatingsFor(int id) {
        List<Rating> ratings = new LinkedList<>();
        Cursor c = query("SELECT * FROM RATINGS WHERE ROUTE_ID='" + id + "'");
        while (c.moveToNext()) {
            Rating r = new Rating(c.getInt(Rating.COLUMN_ROUTE_ID), c.getInt(Rating.COLUMN_YDS), c.getInt(Rating.COLUMN_STARS),
                    c.getString(Rating.COLUMN_USERNAME), c.getString(Rating.COLUMN_DATE));
            ratings.add(r);
        }
        c.close();
        //Log.d("LocalDatabase", "ratings found: " + ratings.size());
        return ratings;
    }

    public List<Send> getSendsFor(int id) {
        List<Send> sends = new LinkedList<>();
        Cursor c = query("SELECT * FROM SEND WHERE ROUTE_ID='" + id + "'");
        while (c.moveToNext()) {
            Send r = new Send(c.getInt(Send.COLUMN_ROUTE_ID), c.getInt(Send.COLUMN_PITCHES), c.getString(Send.COLUMN_DATE), c.getString(Send.COLUMN_SEND_TYPE),
                    c.getInt(Send.COLUMN_ATTEMPTS), c.getString(Send.COLUMN_STYLE), c.getString(Send.COLUMN_USERNAME));
            sends.add(r);
        }
        c.close();
        //Log.d("LocalDatabase", "ratings found: " + ratings.size());
        return sends;
    }

    public List<Rating> getProfileRatings(Activity act, String username) {
        List<Rating> ratings = new LinkedList<>();
        Cursor c = query("SELECT * FROM RATINGS WHERE USERNAME='" + username + "'");
        while (c.moveToNext()) {
            Rating r = new Rating(c.getInt(Rating.COLUMN_ROUTE_ID), c.getInt(Rating.COLUMN_YDS), c.getInt(Rating.COLUMN_STARS),
                    c.getString(Rating.COLUMN_USERNAME), c.getString(Rating.COLUMN_DATE));
            ratings.add(r);
        }
        c.close();
        //Log.d("LocalDatabase", "ratings found: " + ratings.size());
        return ratings;
    }


    public void updateComment(int score, int id) {
        String sql = "UPDATE BETA SET SCORE='" + score + "' WHERE ID='" + id + "'";
        db.execSQL(sql);
    }

    public void updateRouteRating(int id, double stars, String yds, int num) {
        String sql = "UPDATE ROUTES SET YDS='" + yds + "', STARS='" + stars + "', NUM_RATINGS='" + num + "' WHERE ID='" + id + "'";
        //System.out.println("UPDATingSQL:" + sql);
        db.execSQL(sql);
    }

    /*
        Utility method for basic raw query on database
     */
    private Cursor query(String query) {
        return db.rawQuery(query, null);
    }

    /*
        Deletes all rows from tables - no reason to remove the tables themselves.
     */
    public void deleteAll() {
        db.execSQL("DELETE FROM AREAS");
        db.execSQL("DELETE FROM ROUTES");
        db.execSQL("DELETE FROM IDS_DISPLAYABLE");
    }

    public void deleteComments() {
        db.execSQL("DELETE FROM BETA");
    }

    /*
        Insert into database.
     */
    public void insert(Comment c) {
        //System.out.println("inserting comment:" + c.getId());
        ContentValues vals = new ContentValues();
        vals.put("SCORE", c.getScore());
        vals.put("DATE", c.getDate());
        vals.put("TEXT", c.getText());
        vals.put("DISPLAY_ID", c.getDisplayId());
        vals.put("PARENT_ID", c.getParent());
        vals.put("DEPTH", c.getDepth());
        vals.put("AUTHOR_NAME", c.getAuthorName());
        vals.put("TABLE_NAME", c.getTable());
        Cursor cursor = query("SELECT * FROM BETA WHERE ID='" + c.getId() + "'");
        if (cursor.getCount() > 0) {
            db.update("BETA", vals, "ID='" + c.getId() + "'", null);
        } else {
            vals.put("ID", c.getId());
            db.insert("BETA", null, vals);
        }
        cursor.close();

    }

    public void insert(Rating r) {
        ContentValues vals = new ContentValues();
        //db.execSQL("CREATE TABLE IF NOT EXISTS RATINGS (ROUTE_ID INTEGER(11), YDS INTEGER(11), CLIMB_STYLE VARCHAR(31), PITCHES INTEGER(11), TIME_SECONDS INTEGER(11)" +
        //", DATE VARCHAR(255), USERNAME VARCHAR(100), SEND_TYPE VARCHAR(31), ATTEMPTS INTEGER(11))");

        vals.put("ROUTE_ID", r.getRouteId());
        vals.put("YDS", r.getYds());
        vals.put("DATE", r.getDate());
        vals.put("USERNAME", r.getUserName());
        vals.put("STARS", r.getStars());

        Cursor cursor = query("SELECT * FROM RATINGS WHERE ROUTE_ID='" + r.getRouteId() + "' AND USERNAME='" + r.getUserName() + "'");
        if (cursor.getCount() > 0) {
            db.update("RATINGS", vals, "ROUTE_ID='" + r.getRouteId() + "' AND USERNAME='" + r.getUserName() + "'", null);
        } else {
            db.insert("RATINGS", null, vals);
        }
        cursor.close();

    }

    public void insert(Send r) {
        ContentValues vals = new ContentValues();
        //db.execSQL("CREATE TABLE IF NOT EXISTS RATINGS (ROUTE_ID INTEGER(11), YDS INTEGER(11), CLIMB_STYLE VARCHAR(31), PITCHES INTEGER(11), TIME_SECONDS INTEGER(11)" +
        //", DATE VARCHAR(255), USERNAME VARCHAR(100), SEND_TYPE VARCHAR(31), ATTEMPTS INTEGER(11))");

        vals.put("ROUTE_ID", r.getRouteId());
        vals.put("PITCHES", r.getPitches());
        vals.put("DATE", r.getDate());
        vals.put("USERNAME", r.getUserName());
        vals.put("ATTEMPTS", r.getAttempts());
        vals.put("STYLE", r.getStyle());
        vals.put("SEND_TYPE", r.getSendType());

        Log.d("TRYING INSERT", r.getDate());
        Cursor cursor = query("SELECT * FROM SEND WHERE ROUTE_ID='" + r.getRouteId() + "' AND USERNAME='" + r.getUserName() + "' AND DATE='" + r.getDate() + "'");
        if (cursor.getCount() > 0) {
            db.update("SEND", vals, "ROUTE_ID='" + r.getRouteId() + "' AND USERNAME='" + r.getUserName() + "' AND DATE='" + r.getDate() + "'", null);
        } else {
            db.insert("SEND", null, vals);
        }
        cursor.close();

    }

    public void insert(Area r) {
        //Log.d("LOCALDB", "inserting " + r.getName());
        ContentValues vals = new ContentValues();
        vals.put("NAME", r.getName());
        vals.put("LATITUDE", r.getLatitude());
        vals.put("LONGITUDE", r.getLongitude());
        vals.put("REVISION", r.getRevision());
        Cursor cursor = query("SELECT * FROM AREAS WHERE ID='" + r.getId() + "'");
        if (cursor.getCount() > 0) {
            db.update("AREAS", vals, "ID='" + r.getId() + "'", null);
        } else {
            vals.put("ID", r.getId());
            db.insert("AREAS", null, vals);
        }
        cursor.close();

    }

    public void insert(Route r) {
        ContentValues vals = new ContentValues();
        vals.put("NAME", r.getName());
        vals.put("ROUTE_TYPE", r.getType());
        vals.put("LATITUDE", r.getLatitude());
        vals.put("LONGITUDE", r.getLongitude());
        vals.put("REVISION", r.getRevision());
        Cursor cursor = query("SELECT * FROM ROUTES WHERE ID='" + r.getId() + "'");
        if (cursor.getCount() > 0) {
            db.update("ROUTES", vals, "ID='" + r.getId() + "'", null);
        } else {
            vals.put("ID", r.getId());
            db.insert("ROUTES", null, vals);
        }
        cursor.close();

    }

    public void insert(int[] idRow) {
        ContentValues vals = new ContentValues();
        vals.put("PARENT_ID", idRow[Id.COLUMN_PARENT_ID]);
        vals.put("REVISION", idRow[Id.COLUMN_REVISION]);
        Cursor cursor = query("SELECT * FROM IDS_DISPLAYABLE WHERE ID='" + idRow[Id.COLUMN_ID] + "'");
        if (cursor.getCount() > 0) {
            db.update("IDS_DISPLAYABLE", vals, "ID='" + idRow[Id.COLUMN_ID] + "'", null);
        } else {
            vals.put("ID", idRow[Id.COLUMN_ID]);
            db.insert("IDS_DISPLAYABLE", null, vals);
        }
        cursor.close();

    }

    public void insert(Image image) {
        ContentValues vals = new ContentValues();
        vals.put("PARENT_ID", image.getDisplayId());
        vals.put("CAPTION", image.getCaption());
        vals.put("AUTHOR", image.getAuthor());
        vals.put("NAME", image.getName());
        vals.put("DATE", image.getDate());
        Cursor cursor = query("SELECT * FROM IMAGES WHERE NAME='" + image.getName() + "'");
        if (cursor.getCount() > 0) {
            db.update("IMAGES", vals, "NAME='" + image.getName() + "'", null);
        } else {
            db.insert("IMAGES", null, vals);
        }
        cursor.close();
    }

    public void update(Displayable d, CragChatActivity act) {
        if (d instanceof Route) {
            Route r = (Route) d;
            String sql = "UPDATE ROUTES SET LATITUDE = '" + r.getLatitude() + "', LONGITUDE = '" + r.getLongitude() + "', REVISION = '" + r.getRevision() + "' WHERE ID='" + r.getId() + "'";
            db.execSQL(sql);
            new UpdateRouteCommentsImagesTask(this, act, r).execute();
        } else {

        }
    }

    public boolean containsId(List<Comment> list, int id) {
        for (Comment i : list) {
            if (i.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /*
        Get parent list. Parent[0]->Children[n]
     */
    public Area[] getHierarchy(Displayable child) {
        List<Area> list = new LinkedList<>();
        Area parent;
        while ((parent = getParent(child)) != null) {
            list.add(parent);
            child = parent;
        }
        Collections.reverse(list);
        return list.toArray(new Area[list.size()]);
    }

    public Area getParent(Displayable area) {
        Cursor cTop = db.rawQuery("SELECT * FROM IDS_DISPLAYABLE WHERE ID LIKE '" + area.getId() + "'", null);
        if (cTop.moveToFirst()) {
            int parentId = cTop.getInt(Id.COLUMN_PARENT_ID);
            Cursor c = db.rawQuery("SELECT * FROM IDS_DISPLAYABLE WHERE ID LIKE '" + parentId + "'", null);
            if (c.moveToFirst()) {
                Cursor cArea = db.rawQuery("SELECT * FROM AREAS WHERE ID LIKE '" + c.getInt(Id.COLUMN_ID) + "'", null);
                if (cArea.moveToFirst()) {
                    Area a = new Area(cArea.getInt(Area.COLUMN_ID), cArea.getString(Area.COLUMN_NAME),
                            cArea.getDouble(Area.COLUMN_LATITUDE), cArea.getDouble(Area.COLUMN_LONGITUDE), cArea.getInt(Area.COLUMN_REVISION));
                    cArea.close();
                    c.close();
                    cTop.close();
                    return a;
                }
                cArea.close();
            }
            c.close();
        }
        cTop.close();
        return null;
    }

    /*
        Query on local database where $item is the text to search for in names of all Displayables.
     */
    public List<Displayable> findAllWithName(String item) {
        List<Displayable> list = new LinkedList<>();

        item = item.replace("'", "''");

        Cursor c = db.rawQuery("SELECT * FROM AREAS WHERE NAME LIKE '%" + item + "%'", null);
        while (c.moveToNext()) {
            Area a = new Area(c.getInt(Area.COLUMN_ID), c.getString(Area.COLUMN_NAME),
                    c.getDouble(Area.COLUMN_LATITUDE), c.getDouble(Area.COLUMN_LONGITUDE), c.getInt(Area.COLUMN_REVISION));
            list.add(a);
        }
        c.close();

        c = db.rawQuery("SELECT * FROM ROUTES WHERE NAME LIKE '%" + item + "%'", null);
        while (c.moveToNext()) {
            Route r = new Route(c.getInt(Route.COLUMN_ID), c.getString(Route.COLUMN_NAME),
                    c.getString(Route.COLUMN_ROUTE_TYPE),
                    c.getDouble(Route.COLUMN_LATITUDE), c.getDouble(Route.COLUMN_LONGITUDE), c.getInt(Route.COLUMN_REVISION));
            list.add(r);
        }
        c.close();

        return list;
    }

    /*
        Method for finding all areas within an area.
     */
    public List<Area> findAreasWithin(Area area) {
        List<Area> list = new LinkedList<>();
        Cursor c = db.rawQuery("SELECT * FROM AREAS WHERE ID LIKE '" + area.getId() + "'", null);
        if (c.moveToFirst()) {
            Area a = new Area(c.getInt(Area.COLUMN_ID), c.getString(Area.COLUMN_NAME),
                    c.getDouble(Area.COLUMN_LATITUDE), c.getDouble(Area.COLUMN_LONGITUDE), c.getInt(Area.COLUMN_REVISION));
            list.addAll(findAreasWithinRecursive(a));
        }
        c.close();
        return list;
    }

    /*
        Recursive part of method
     */
    private List<Area> findAreasWithinRecursive(Area area) {
        List<Area> list = new ArrayList<>();

        Cursor cTop = db.rawQuery("SELECT * FROM IDS_DISPLAYABLE WHERE PARENT_ID LIKE '" + area.getId() + "'", null);
        while (cTop.moveToNext()) {
            int areaId = cTop.getInt(Id.COLUMN_ID);
            Cursor c = db.rawQuery("SELECT * FROM AREAS WHERE ID LIKE '" + areaId + "'", null);
            if (c.moveToFirst()) {
                Area a = new Area(c.getInt(Area.COLUMN_ID), c.getString(Area.COLUMN_NAME),
                        c.getDouble(Area.COLUMN_LATITUDE), c.getDouble(Area.COLUMN_LONGITUDE), c.getInt(Area.COLUMN_REVISION));
                list.add(a);
                list.addAll(findAreasWithinRecursive(a));
            }
            c.close();
        }
        cTop.close();
        return list;
    }


    /*
        Retrieve all routes from within specified area
     */
    public List<Displayable> findRoutesWithin(Area area) {
        List<Displayable> list = new LinkedList<>();
        List<Area> allAreas = findAreasWithin(area);
        allAreas.add(area);
        for (Displayable cur : allAreas) {
            Cursor c = db.rawQuery("SELECT * FROM AREAS WHERE ID LIKE '" + cur.getId() + "'", null);
            if (c.moveToFirst()) {
                Area a = new Area(c.getInt(Area.COLUMN_ID), c.getString(Area.COLUMN_NAME),
                        c.getDouble(Area.COLUMN_LATITUDE), c.getDouble(Area.COLUMN_LONGITUDE), c.getInt(Area.COLUMN_REVISION));
                c.close();
                c = db.rawQuery("SELECT * FROM IDS_DISPLAYABLE WHERE PARENT_ID LIKE '" + a.getId() + "'", null);
                while (c.moveToNext()) {
                    int routeId = c.getInt(Id.COLUMN_ID);
                    Cursor cRoute = db.rawQuery("SELECT * FROM ROUTES WHERE ID LIKE '" + routeId + "'", null);
                    if (cRoute.moveToFirst()) {
                        Route r = new Route(cRoute.getInt(Route.COLUMN_ID), cRoute.getString(Route.COLUMN_NAME),
                                cRoute.getString(Route.COLUMN_ROUTE_TYPE),
                                cRoute.getDouble(Route.COLUMN_LATITUDE), cRoute.getDouble(Route.COLUMN_LONGITUDE), cRoute.getInt(Route.COLUMN_REVISION));
                        list.add(r);
                    }
                    cRoute.close();
                }
                c.close();
            }
        }

        return list;
    }

    public Displayable findExact(String name) {
        String query = "WHERE NAME LIKE '" + name.replaceAll("'", "''") + "'";
        return findExactArgs(query);
    }

    public Displayable findExact(int id) {
        String query = "WHERE ID LIKE '" + id + "'";
        return findExactArgs(query);
    }

    /*
        Find a route or area with exact name
     */
    public Displayable findExactArgs(String args) {
        Cursor c = db.rawQuery("SELECT * FROM AREAS " + args, null);
        if (c.moveToFirst()) {
            Area a = new Area(c.getInt(Area.COLUMN_ID), c.getString(Area.COLUMN_NAME),
                    c.getDouble(Area.COLUMN_LATITUDE), c.getDouble(Area.COLUMN_LONGITUDE), c.getInt(Area.COLUMN_REVISION));
            c.close();
            return a;
        }

        c = db.rawQuery("SELECT * FROM ROUTES " + args, null);
        if (c.moveToFirst()) {
            Route r = new Route(c.getInt(Route.COLUMN_ID), c.getString(Route.COLUMN_NAME),
                    c.getString(Route.COLUMN_ROUTE_TYPE),
                    c.getDouble(Route.COLUMN_LATITUDE), c.getDouble(Route.COLUMN_LONGITUDE), c.getInt(Route.COLUMN_REVISION));
            c.close();
            return r;
        }

        return null;
    }

    private class Id {
        public static final int COLUMN_ID = 0;
        public static final int COLUMN_PARENT_ID = 1;
        public static final int COLUMN_REVISION = 2;
    }

}