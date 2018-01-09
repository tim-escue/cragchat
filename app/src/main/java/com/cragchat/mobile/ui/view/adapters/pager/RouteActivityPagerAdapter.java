package com.cragchat.mobile.ui.view.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.ui.view.fragments.ImageFragment;
import com.cragchat.mobile.ui.view.fragments.LocationFragment;
import com.cragchat.mobile.ui.view.fragments.RatingFragment;
import com.cragchat.mobile.ui.view.fragments.SendsFragment;

public class RouteActivityPagerAdapter extends TabPagerAdapter {

    public static final int TAB_BETA = 0;
    public static final int TAB_RATINGS = 1;
    public static final int TAB_SENDS = 2;
    public static final int TAB_IMAGES = 4;

    public RouteActivityPagerAdapter(Context context, FragmentManager fragmentManager, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton, String routeKey) {
        super(fragmentManager, appBarLayout, floatingActionButton);

        addFragment(context.getString(R.string.title_beta),
                CommentSectionFragment.newInstance(routeKey, CommentSectionFragment.TABLE_BETA), false, true);
        addFragment(context.getString(R.string.title_ratings),
                RatingFragment.newInstance(routeKey), false, true);
        addFragment(context.getString(R.string.title_sends),
                SendsFragment.newInstance(routeKey), false, true);
        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(routeKey), false, true);
        addFragment(context.getString(R.string.title_images),
                ImageFragment.newInstance(routeKey), false, true);
    }

}