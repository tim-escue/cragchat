package com.cragchat.mobile.view.adapters.pager;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.fragments.CragChatMapFragment;

public class LocationPagerAdapter extends TabPagerAdapter {

    public LocationPagerAdapter(FragmentManager fm, String entityId, FloatingActionButton button) {
        super(fm, null, button);
        System.out.println("FAB IS:" + (button != null));
        addFragment("Map", CragChatMapFragment.newInstance(), false, false);
        addFragment("Text Description", CommentSectionFragment.newInstance(entityId, "Location"), false, true);
    }
}