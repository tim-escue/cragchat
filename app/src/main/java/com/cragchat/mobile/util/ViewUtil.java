package com.cragchat.mobile.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.cragchat.mobile.domain.model.Route;
import com.cragchat.mobile.mvp.view.activity.AreaActivity;
import com.cragchat.mobile.mvp.view.activity.RouteActivity;

/**
 * Created by timde on 2/5/2018.
 */

public class ViewUtil {


    public static final String ENTITY_KEY = "ENTITY_KEY";
    public static final String ENTITY = "ENTITY";
    public static final String TAB = "TAB";
    public static final String IMAGE = "IMAGE";

    public static void addFragmentToActivity (@NonNull android.app.FragmentManager fragmentManager,
                                              @NonNull android.app.Fragment fragment, int frameId,
                                              String tag) {
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, tag);
        transaction.commit();
    }

    public static void launch(Context context, String areaKey) {
        launch(context, areaKey, 0);
    }

    public static void launch(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    public static void launch(Context context, String areaKey, int tab) {
        Intent intent = new Intent(context, AreaActivity.class);
        intent.putExtra(ENTITY_KEY, areaKey);
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
