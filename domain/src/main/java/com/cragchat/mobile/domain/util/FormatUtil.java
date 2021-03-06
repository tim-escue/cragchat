package com.cragchat.mobile.domain.util;

import android.content.Context;

import com.cragchat.mobile.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by tim on 8/12/17.
 */

public class FormatUtil {

    public static final SimpleDateFormat RAW_FORMAT;
    public static final SimpleDateFormat MONTH_DAY_YEAR;
    public static final long secondsInMilli = 1000;
    public static final long minutesInMilli = secondsInMilli * 60;
    public static final long hoursInMilli = minutesInMilli * 60;
    public static final long daysInMilli = hoursInMilli * 24;


    static {
        RAW_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RAW_FORMAT.setTimeZone(TimeZone.getTimeZone("PST"));
        MONTH_DAY_YEAR = new SimpleDateFormat("MMMM d, yyyy");
        MONTH_DAY_YEAR.setTimeZone(TimeZone.getTimeZone("PST"));
    }

    public static String getDateAsElapsed(String rawDate) {
        Date dateObject = null;
        try {
            dateObject = FormatUtil.RAW_FORMAT.parse(rawDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateObject != null ? FormatUtil.elapsed(dateObject, Calendar.getInstance().getTime()) : rawDate;
    }

    public static String getFormattedDate(String rawDateString) {
        try {
            return MONTH_DAY_YEAR.format(RAW_FORMAT.parse(rawDateString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rawDateString;
    }

    public static String elapsed(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        if (elapsedDays > 0) {
            return elapsedDays + " days ago";
        }

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        if (elapsedHours > 0) {
            return elapsedHours + " hours ago";
        }

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        if (elapsedMinutes > 0) {
            return elapsedMinutes + " minutes ago";
        }

        long elapsedSeconds = different / secondsInMilli;
        if (elapsedSeconds < 30) {
            return "Just now";
        }
        return elapsedSeconds + " seconds ago";
    }

    public static <T, K> List<K> transformList(List<T> list, Converter<T, K> converter) {
        List<K> newList = new ArrayList<>();
        for (T t : list) {
            newList.add(converter.convert(t));
        }
        return newList;
    }

    public interface Converter<T, K> {
        K convert(T value);
    }

    public static String getYdsString(Context context, int yds) {
        return yds != -1 ? context.getResources().getStringArray(R.array.yds_options)[yds] : "Not rated";
    }

    public static String getStarsString(double stars) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(stars);
    }

    public static String[] getStringArrayFromStrings(List<String> strings) {
        String[] areaIds = new String[strings.size()];
        for (int i = 0; i < areaIds.length; i++) {
            areaIds[i] = strings.get(i);
        }
        return areaIds;
    }

}
