package com.cragchat.mobile.descriptor;

import android.app.Activity;
import android.content.Context;

import com.cragchat.mobile.R;
import com.cragchat.mobile.sql.LocalDatabase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class Displayable implements Comparable<Displayable> {

    //Empty constructor for FireBase
    public Displayable() {}

    public abstract String getName();

    public int id;
    public int revision;

    public int getId() {
        return id;
    }

    public int getRevision() {
        return revision;
    }

    public Displayable(int id, int revision) {
        this.id = id;
        this.revision = revision;
    }

    @Override
    public int compareTo(Displayable b) {
        return getName().compareTo(b.getName());
    }

    @Override
    public String toString() {
        if (this instanceof Route) {
            return encodeRoute((Route) this).toString();
        } else if (this instanceof Area) {
            return encodeAsString((Area) this);
        }
        return "NULL_STRING(toString()displayable)";
    }

    public static String encodeAsString(Area a) {
        return "AREA#" + a.getId() + "#" + a.getName() + "#" + a.getLatitude() + "#" + a.getLongitude() + "#" + a.getRevision();
    }

    public static Area decodeAreaString(String s) {
        String[] split = s.split("#");
        return new Area(Integer.parseInt(split[1 + Area.COLUMN_ID]), split[1 + Area.COLUMN_NAME],
                Double.parseDouble(split[1 + Area.COLUMN_LATITUDE]), Double.parseDouble(split[1 + Area.COLUMN_LONGITUDE]), Integer.parseInt(split[1 + Area.COLUMN_REVISION]));
    }
///    public Rating(int routeId, int yds, int stars, String style, int pitches, int timeSeconds, String userName, String date, String sendType, int attempts) {

    public static JSONObject encodeRating(Rating rating) {
        Map<String, String> map = new HashMap<>();
        map.put("objectType", "rating");
        map.put("routeId", String.valueOf(rating.getRouteId()));
        map.put("yds", String.valueOf(rating.getYds()));
        map.put("stars", String.valueOf(rating.getStars()));
        map.put("username", rating.getUserName());
        map.put("date", rating.getDate());
        return new JSONObject(map);
    }

    public static String getYdsString(Activity activity, int yds) {
        return yds != -1 ? activity.getResources().getStringArray(R.array.yds_options)[yds] : "Not rated";
    }

    public static JSONObject encodeRoute(Route route) {
        Map<String, String> map = new HashMap<>();
        map.put("objectType", "route");
        map.put("id", String.valueOf(route.getId()));
        map.put("name", route.getName());
        map.put("type", route.getType());
        map.put("latitude", String.valueOf(route.getLatitude()));
        map.put("longitude", String.valueOf(route.getLongitude()));
        map.put("revision", String.valueOf(route.getRevision()));
        return new JSONObject(map);
    }

    public String getSubTitle(Context context) {
        Area[] hierarchy = LocalDatabase.getInstance(context).getHierarchy(this);
        StringBuilder subtitle = new StringBuilder();
        for (int i = 0; i < hierarchy.length; i++) {
            Area area = hierarchy[i];
            if (i != 0) {
                subtitle.append(" -> ");
            }
            subtitle.append(area.getName());
        }
        return subtitle.toString();
    }

    public static Rating decodeRating(JSONObject obj) {
        try {
            return new Rating(obj.getInt("routeId"), obj.getInt("yds"), obj.getInt("stars"),
                    obj.getString("username"), obj.getString("date"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Route decodeRoute(JSONObject obj) {
        try {
            return new Route(obj.getInt("id"), obj.getString("name"), obj.getString("type"), obj.getDouble("latitude"), obj.getDouble("longitude"), obj.getInt("revision"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}