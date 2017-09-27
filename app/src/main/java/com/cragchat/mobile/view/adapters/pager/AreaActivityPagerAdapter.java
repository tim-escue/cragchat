package com.cragchat.mobile.view.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.R;
import com.cragchat.mobile.descriptor.Area;
import com.cragchat.mobile.descriptor.Displayable;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.fragments.LocationFragment;
import com.cragchat.mobile.fragments.RecentActivityFragment;
import com.cragchat.mobile.fragments.DisplayableListFragment;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.List;

public class AreaActivityPagerAdapter extends TabPagerAdapter {

    public AreaActivityPagerAdapter(Context context, FragmentManager fragmentManager,
                                    AppBarLayout appBarLayout, Area area,
                                    List<Area> subAreas,
                                    List<Displayable> routes,
                                    FloatingActionButton floatingActionButton) {
        super(context, fragmentManager, appBarLayout, floatingActionButton);

        addFragment(context.getString(R.string.title_recent_activity),
                RecentActivityFragment.newInstance(area.getId()), false, false);

        DisplayableListFragment routeList = DisplayableListFragment.newInstance();
        routeList.setDisplayables(routes);
        addFragment(context.getString(R.string.title_routes, routes.size()), routeList, false, false);

        if (subAreas.size() > 0) {
            DisplayableListFragment areaFragment = DisplayableListFragment.newInstance();
            List<Displayable> areasAsDisplayables = (List) subAreas;
            areaFragment.setDisplayables(areasAsDisplayables);
            areaFragment.hideFilterAndSortButtons();
            addFragment(context.getString(R.string.title_areas, subAreas.size()), areaFragment, false, false);
        }

        addFragment(context.getString(R.string.title_discussion),
                CommentSectionFragment.newInstance(area.getId(), LocalDatabase.DISCUSSION), false, true);

        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(area.getId()), false, true);

        addFragment(context.getString(R.string.title_images),
                ImageFragment.newInstance(area.getId()), false, true);
    }

}