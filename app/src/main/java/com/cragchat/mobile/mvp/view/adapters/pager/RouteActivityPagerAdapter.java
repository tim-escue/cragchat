package com.cragchat.mobile.mvp.view.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.R;
import com.cragchat.mobile.mvp.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.mvp.view.fragments.ImageFragment;
import com.cragchat.mobile.mvp.view.fragments.LocationFragment;
import com.cragchat.mobile.mvp.view.fragments.RatingFragment;
import com.cragchat.mobile.mvp.view.fragments.SendsFragment;

public class RouteActivityPagerAdapter extends TabPagerAdapter {

    public static final int TAB_BETA = 0;
    public static final int TAB_RATINGS = 1;
    public static final int TAB_SENDS = 2;
    public static final int TAB_IMAGES = 4;

    public RouteActivityPagerAdapter(Context context, FragmentManager fragmentManager,
                                     CommentSectionFragment commentSectionFragment,
                                     RatingFragment ratingFragment,
                                     SendsFragment sendsFragment,
                                     ImageFragment imageFragment,
                                     LocationFragment locationFragment,
                                     AppBarLayout appBarLayout, FloatingActionButton floatingActionButton, String routeKey) {
        super(fragmentManager, appBarLayout, floatingActionButton);

        addFragment(context.getString(R.string.title_beta),
                commentSectionFragment, false, true);
        addFragment(context.getString(R.string.title_ratings),
                ratingFragment, false, true);
        addFragment(context.getString(R.string.title_sends),
                sendsFragment, false, true);
        addFragment(context.getString(R.string.title_location),
                locationFragment, false, true);
        addFragment(context.getString(R.string.title_images),
                imageFragment, false, true);
    }

}