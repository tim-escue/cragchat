package com.cragchat.mobile.ui.view.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.R;
import com.cragchat.mobile.ui.model.Area;
import com.cragchat.mobile.features.area.AreaListFragment;
import com.cragchat.mobile.ui.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.ui.view.fragments.ImageFragment;
import com.cragchat.mobile.ui.view.fragments.LocationFragment;
import com.cragchat.mobile.ui.view.fragments.RecentActivityFragment;
import com.cragchat.mobile.ui.view.fragments.RouteListFragment;

public class AreaActivityPagerAdapter extends TabPagerAdapter {

    public AreaActivityPagerAdapter(Context context, AreaListFragment areaFragment, FragmentManager fragmentManager,
                                    AppBarLayout appBarLayout, Area area,
                                    FloatingActionButton floatingActionButton) {
        super(fragmentManager, appBarLayout, floatingActionButton);

        addFragment(context.getString(R.string.title_recent_activity),
                RecentActivityFragment.newInstance(area), false, false);

        RouteListFragment routeList = RouteListFragment.newInstance(area.getKey(),
                area.getRoutes().toArray(new String[area.getRoutes().size()]));
        addFragment(context.getString(R.string.title_routes),
                routeList,
                false,
                false);

        if (area.getSubAreas().size() > 0) {
            addFragment(context.getString(R.string.title_areas),
                    areaFragment,
                    false,
                    false);
        }

        addFragment(context.getString(R.string.title_discussion),
                CommentSectionFragment.newInstance(area.getKey(), CommentSectionFragment.TABLE_DISCUSSION), false, true);

        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(area.getKey()), false, true);

        addFragment(context.getString(R.string.title_images),
                ImageFragment.newInstance(area.getKey()), false, true);
    }

}