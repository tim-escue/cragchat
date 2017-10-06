package com.cragchat.mobile.util;

import android.app.Activity;
import android.content.Context;

import com.cragchat.mobile.R;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

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
}
