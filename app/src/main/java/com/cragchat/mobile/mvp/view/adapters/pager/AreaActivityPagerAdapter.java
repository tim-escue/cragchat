package com.cragchat.mobile.mvp.view.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.R;
import com.cragchat.mobile.domain.model.Area;
import com.cragchat.mobile.mvp.view.fragments.AreaListFragment;
import com.cragchat.mobile.mvp.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.mvp.view.fragments.ImageFragment;
import com.cragchat.mobile.mvp.view.fragments.LocationFragment;
import com.cragchat.mobile.mvp.view.fragments.RecentActivityFragment;
import com.cragchat.mobile.mvp.view.fragments.RouteListFragment;

public class AreaActivityPagerAdapter extends TabPagerAdapter {

    public AreaActivityPagerAdapter(Context context, AreaListFragment areaFragment,
                                    RecentActivityFragment recentActivityFragment,
                                    CommentSectionFragment commentSectionFragment,
                                    ImageFragment imageFragment,
                                    RouteListFragment routeListFragment,
                                    LocationFragment locationFragment,
                                    FragmentManager fragmentManager,
                                    AppBarLayout appBarLayout, Area area,
                                    FloatingActionButton floatingActionButton) {
        super(fragmentManager, appBarLayout, floatingActionButton);
        if (recentActivityFragment != null)
            addFragment(context.getString(R.string.title_recent_activity),
                    recentActivityFragment, false, false);

        if (routeListFragment != null)
            addFragment(context.getString(R.string.title_routes),
                    routeListFragment,
                    false,
                    false);

        if (areaFragment != null)
            if (area.getSubAreas().size() > 0) {
                addFragment(context.getString(R.string.title_areas),
                        areaFragment,
                        false,
                        false);
            }

        if (commentSectionFragment != null)
            addFragment(context.getString(R.string.title_discussion),
                    commentSectionFragment, false, true);

        if (locationFragment != null)
        addFragment(context.getString(R.string.title_location),
                locationFragment, false, true);

        if (imageFragment != null)
            addFragment(context.getString(R.string.title_images),
                    imageFragment, false, true);
    }

}