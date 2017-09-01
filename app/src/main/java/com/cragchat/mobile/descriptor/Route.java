package com.cragchat.mobile.descriptor;

import android.content.Context;

import com.cragchat.mobile.sql.LocalDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route extends Displayable {

    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ROUTE_TYPE = 2;
    public static final int COLUMN_LATITUDE = 3;
    public static final int COLUMN_LONGITUDE = 4;
    public static final int COLUMN_REVISION = 5;

    private String name;
    private String type;
    private double latitude;
    private double longitude;

    public Route(int id, String name, String type,
                 double latitude, double longitude, int revision) {
        super(id, revision);
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private static Map<Integer, RouteInfo> infos;

    private class RouteInfo {

        public RouteInfo(int yds, double stars) {
            this.yds = yds;
            this.stars = stars;
        }

        public int yds;
        public double stars;
    }

    public int getYds(Context con) {
        if (infos == null) {
            infos = new HashMap<>();
        }
        if (infos.containsKey(con)) {
            return infos.get(con).yds;
        } else {
            List<Rating> raintgs = LocalDatabase.getInstance(con).getRatingsFor(id);
            int yds = 0;
            int stars = 0;
            for (Rating i : raintgs) {
                yds += i.getYds();
                stars += i.getStars();
            }
            if (raintgs.size() > 0) {
                infos.put(id, new RouteInfo(yds / raintgs.size(), stars / (double) raintgs.size()));
            } else {
                infos.put(id, new RouteInfo(-1, -1.0));
            }
            return infos.get(id).yds;
        }
    }

    public double getStars(Context con) {
        if (infos == null) {
            infos = new HashMap<>();
        }
        if (infos.containsKey(con)) {
            return infos.get(con).stars;
        } else {
            List<Rating> raintgs = LocalDatabase.getInstance(con).getRatingsFor(id);
            int yds = 0;
            int stars = 0;
            for (Rating i : raintgs) {
                yds += i.getYds();
                stars += i.getStars();
            }
            if (raintgs.size() > 0) {
                infos.put(id, new RouteInfo(yds / raintgs.size(), stars / (double) raintgs.size()));
            } else {
                infos.put(id, new RouteInfo(-1, -1.0));
            }
            return infos.get(id).stars;
        }
    }

    public String getStarsString(Context activity) {
        double d = getStars(activity);
        if (d != -1.0) {
            NumberFormat formatter = new DecimalFormat("#0.0");
            return String.valueOf(formatter.format(d));
        } else {
            return "Not rated";
        }
    }


    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}