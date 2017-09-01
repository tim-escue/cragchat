package com.cragchat.mobile.adapters.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cragchat.mobile.R;
import com.cragchat.mobile.activity.CragChatActivity;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.fragments.AreaListFragment;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.fragments.LocationFragment;
import com.cragchat.mobile.fragments.RatingFragment;
import com.cragchat.mobile.fragments.RecentActivityFragment;
import com.cragchat.mobile.fragments.RouteListFragment;
import com.cragchat.mobile.fragments.SendsFragment;
import com.cragchat.mobile.sql.LocalDatabase;

public class AreaActivityPagerAdapter extends TabPagerAdapter {

    public AreaActivityPagerAdapter(Context context, FragmentManager fragmentManager, Area area) {
        super(context, fragmentManager);

        addFragment(context.getString(R.string.title_recent_activity),
                RecentActivityFragment.newInstance(area.getId()));
        addFragment(context.getString(R.string.title_routes),
                RouteListFragment.newInstance(area));
        if (LocalDatabase.getInstance(context).findAreasWithin(area).size() > 0) {
            addFragment(context.getString(R.string.title_areas),
                    AreaListFragment.newInstance(area));
        }
        addFragment(context.getString(R.string.title_discussion),
                CommentSectionFragment.newInstance(area.getId(), LocalDatabase.DISCUSSION));
        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(area.getId()));
        addFragment(context.getString(R.string.title_images),
                ImageFragment.newInstance(area.getId()));
    }

}