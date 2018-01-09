package com.cragchat.mobile.ui.view.adapters.pager;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;

import com.cragchat.mobile.ui.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.ui.view.fragments.CragChatMapFragment;

public class LocationPagerAdapter extends TabPagerAdapter {

    public LocationPagerAdapter(FragmentManager fm, String entityId, FloatingActionButton button) {
        super(fm, null, button);
        addFragment("Map", CragChatMapFragment.newInstance(), false, false);
        addFragment("Text Description", CommentSectionFragment.newInstance(entityId, "Location"), false, true);
    }
}