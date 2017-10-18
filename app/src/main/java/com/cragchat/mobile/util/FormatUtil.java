package com.cragchat.mobile.util;

import android.app.Activity;
import android.content.Context;

import com.cragchat.mobile.R;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by tim on 8/12/17.
 */

public class FormatUtil {

    public static final SimpleDateFormat RAW_FORMAT;
    public static final SimpleDateFormat MONTH_DAY_YEAR;

    static {
        RAW_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MONTH_DAY_YEAR = new SimpleDateFormat("MMMM d, yyyy");
    }

    public static String getFormattedDate(String rawDateString) {
        try {
            return MONTH_DAY_YEAR.format(RAW_FORMAT.parse(rawDateString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rawDateString;
    }

    public static String getYdsString(Context context, int yds) {
        return yds != -1 ? context.getResources().getStringArray(R.array.yds_options)[yds] : "Not rated";
    }

    public static String getStarsString(double stars) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(stars);
    }

    public static String areaListToString(List<? extends Area> list) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        if (list.size() > 0) {
            builder.append(list.get(index).getKey());
            index++;
        }
        while (index < list.size()) {
            builder.append(",");
            builder.append(list.get(index).getKey());
            index++;
        }
        return builder.toString();
    }

    public static String routeListToString(List<? extends Route> list) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        if (list.size() > 0) {
            builder.append(list.get(index).getKey());
            index++;
        }
        while (index < list.size()) {
            builder.append(",");
            builder.append(list.get(index).getKey());
            index++;
        }
        return builder.toString();
    }
}
