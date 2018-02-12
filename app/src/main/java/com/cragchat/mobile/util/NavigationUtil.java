package com.cragchat.mobile.util;

import android.content.Context;
import android.content.Intent;

import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.view.activity.AreaActivity;
import com.cragchat.mobile.ui.view.activity.RouteActivity;

/**
 * Created by timde on 12/23/2017.
 */

public class NavigationUtil {

    public static final String ENTITY_KEY = "ENTITY_KEY";

    public static final String ENTITY = "ENTITY";
    public static final String TAB = "TAB";
    public static final String IMAGE = "IMAGE";

    public static void launch(Context context, Area area) {
        launch(context, area, 0);
    }

    public static void launch(Context context, Area area, int tab) {
        Intent intent = new Intent(context, AreaActivity.class);
        intent.putExtra(ENTITY, area);
        intent.putExtra(TAB, tab);
        context.startActivity(intent);
    }

    public static void launch(Context context, Route route) {
        launch(context, route, 0);
    }

    public static void launch(Context context, Route route, int tab) {
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(ENTITY, route);
        intent.putExtra(TAB, tab);
        context.startActivity(intent);
    }

}
