package com.cragchat.mobile.view.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.cragchat.mobile.R;
import com.cragchat.mobile.fragments.DisplayableListFragment;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.fragments.LocationFragment;
import com.cragchat.mobile.model.Area;
import com.cragchat.mobile.model.Route;

import java.util.List;

public class AreaActivityPagerAdapter extends TabPagerAdapter {

    public AreaActivityPagerAdapter(Context context, FragmentManager fragmentManager,
                                    AppBarLayout appBarLayout, Area area,
                                    List<? extends Area> subAreas,
                                    List<? extends Route> routes,
                                    FloatingActionButton floatingActionButton) {
        super(context, fragmentManager, appBarLayout, floatingActionButton);

        /*addFragment(context.getString(R.string.title_recent_activity),
                RecentActivityFragment.newInstance(-343), false, false);*/

        Log.d("Here122","here");
        DisplayableListFragment routeList = DisplayableListFragment.newInstance();
        routeList.setDisplayables(routes);
        addFragment(context.getString(R.string.title_routes, routes.size()), routeList, false, false);
        Log.d("Here124442","here");

        if (subAreas.size() > 0) {
            DisplayableListFragment areaFragment = DisplayableListFragment.newInstance();
            areaFragment.setDisplayables(subAreas);
            areaFragment.hideFilterAndSortButtons();
            addFragment(context.getString(R.string.title_areas, subAreas.size()), areaFragment, false, false);
        }
        Log.d("Here143243222","here");

        /*addFragment(context.getString(R.string.title_discussion),
                CommentSectionFragment.newInstance(area.getId(), LocalDatabase.DISCUSSION), false, true); */

        addFragment(context.getString(R.string.title_location),
                LocationFragment.newInstance(area.getKey()), false, true);

        addFragment(context.getString(R.string.title_images),
                ImageFragment.newInstance(area.getKey()), false, true);
    }

}