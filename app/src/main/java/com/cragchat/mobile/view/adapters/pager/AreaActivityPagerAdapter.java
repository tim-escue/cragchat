package com.cragchat.mobile.view.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.R;
import com.cragchat.mobile.database.models.RealmArea;
import com.cragchat.mobile.fragments.AreaListFragment;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.fragments.LocationFragment;
import com.cragchat.mobile.fragments.RouteListFragment;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.util.FormatUtil;

public class AreaActivityPagerAdapter extends TabPagerAdapter {

    public AreaActivityPagerAdapter(Context context, FragmentManager fragmentManager,
                                    AppBarLayout appBarLayout, RealmArea area,
                                    FloatingActionButton floatingActionButton) {
        super(fragmentManager, appBarLayout, floatingActionButton);

        /*addFragment(context.getString(R.string.title_recent_activity),
                RecentActivityFragment.newInstance(-343), false, false);*/

        RouteListFragment routeList = RouteListFragment.newInstance(area.getKey(),
                FormatUtil.getStringArrayFromTags(area.getRoutes()));
        addFragment(context.getString(R.string.title_routes, area.getRoutes().size()),
                routeList,
                false,
                false);


        if (area.getSubAreas().size() > 0) {
            AreaListFragment areaFragment = AreaListFragment.newInstance(area.getKey(),
                    FormatUtil.getStringArrayFromTags(area.getSubAreas()));
            addFragment(context.getString(R.string.title_areas, area.getSubAreas().size()),
                    areaFragment,
                    false,
                    false);
        }

        addFragment(context.getString(R.string.title_discussion),
                CommentSectionFragment.newInstance(area.getKey(), "Discussion"), false, true);

        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(area.getKey()), false, true);

        addFragment(context.getString(R.string.title_images, area.getImages().size()),
                ImageFragment.newInstance(area.getKey()), false, true);
    }

}