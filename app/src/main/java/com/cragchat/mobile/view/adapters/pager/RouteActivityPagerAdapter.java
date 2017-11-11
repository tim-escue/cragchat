package com.cragchat.mobile.view.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.R;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.fragments.LocationFragment;
import com.cragchat.mobile.fragments.RatingFragment;
import com.cragchat.mobile.fragments.SendsFragment;
import com.cragchat.mobile.model.realm.RealmRoute;

public class RouteActivityPagerAdapter extends TabPagerAdapter {

    public RouteActivityPagerAdapter(Context context, FragmentManager fragmentManager, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton, RealmRoute route) {
        super(fragmentManager, appBarLayout, floatingActionButton);

        addFragment(context.getString(R.string.title_beta),
                CommentSectionFragment.newInstance(route.getKey(), "beta"), false, true);
        addFragment(context.getString(R.string.title_ratings, route.getRatings()),
                RatingFragment.newInstance(route.getKey()), false, true);
        addFragment(context.getString(R.string.title_sends, route.getSends()),
                SendsFragment.newInstance(route.getKey()), false, true);
        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(route.getKey()), false, true);
        addFragment(context.getString(R.string.title_images, route.getImages().size()),
                ImageFragment.newInstance(route.getKey()), false, true);
    }

}