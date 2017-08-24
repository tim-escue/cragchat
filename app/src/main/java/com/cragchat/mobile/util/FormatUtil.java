package com.cragchat.mobile.util;

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
}
