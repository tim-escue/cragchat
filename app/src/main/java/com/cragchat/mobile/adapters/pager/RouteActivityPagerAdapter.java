package com.cragchat.mobile.adapters.pager;

import android.content.Context;
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

    public RouteActivityPagerAdapter(Context context, FragmentManager fragmentManager, int routeId) {
        super(context, fragmentManager);

        addFragment(context.getString(R.string.title_beta),
                CommentSectionFragment.newInstance(routeId, LocalDatabase.BETA));
        addFragment(context.getString(R.string.title_ratings),
                RatingFragment.newInstance(routeId));
        addFragment(context.getString(R.string.title_sends),
                SendsFragment.newInstance(routeId));
        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(routeId));
        addFragment(context.getString(R.string.title_images),
                ImageFragment.newInstance(routeId));
    }
    
}