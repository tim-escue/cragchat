package com.cragchat.mobile.adapters.pager;

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

    public AreaActivityPagerAdapter(Context context, FragmentManager fragmentManager, AppBarLayout appBarLayout, Area area, FloatingActionButton floatingActionButton) {
        super(context, fragmentManager, appBarLayout, floatingActionButton);

        addFragment(context.getString(R.string.title_recent_activity),
                RecentActivityFragment.newInstance(area.getId()), false, false);

        DisplayableListFragment routeList = DisplayableListFragment.newInstance();
        routeList.setDisplayables(LocalDatabase.getInstance(context).findRoutesWithin(area));
        addFragment(context.getString(R.string.title_routes), routeList, false, false);

        List<Displayable> subAreas = LocalDatabase.getInstance(context).findAreasWithin(area);
        if (subAreas.size() > 0) {
            DisplayableListFragment areaFragment = DisplayableListFragment.newInstance();
            areaFragment.setDisplayables(subAreas);
            addFragment(context.getString(R.string.title_areas), areaFragment, false, false);
        }

        addFragment(context.getString(R.string.title_discussion),
                CommentSectionFragment.newInstance(area.getId(), LocalDatabase.DISCUSSION), false, true);

        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(area.getId()), true, true);

        addFragment(context.getString(R.string.title_images),
                ImageFragment.newInstance(area.getId()), true, true);
    }

}