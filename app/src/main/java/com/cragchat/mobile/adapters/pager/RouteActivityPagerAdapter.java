package com.cragchat.mobile.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;

import com.cragchat.mobile.R;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.fragments.LocationFragment;
import com.cragchat.mobile.fragments.RatingFragment;
import com.cragchat.mobile.fragments.SendsFragment;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.HashMap;
import java.util.Map;

public class RouteActivityPagerAdapter extends TabPagerAdapter {

    public RouteActivityPagerAdapter(Context context, FragmentManager fragmentManager, AppBarLayout appBarLayout, FloatingActionButton floatingActionButton, int routeId) {
        super(context, fragmentManager, appBarLayout, floatingActionButton);

        addFragment(context.getString(R.string.title_beta),
                CommentSectionFragment.newInstance(routeId, LocalDatabase.BETA), false, true);
        addFragment(context.getString(R.string.title_ratings),
                RatingFragment.newInstance(routeId), false, true);
        addFragment(context.getString(R.string.title_sends),
                SendsFragment.newInstance(routeId), true, true);
        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(routeId), true, true);
        addFragment(context.getString(R.string.title_images),
                ImageFragment.newInstance(routeId), true, true);
    }
    
}