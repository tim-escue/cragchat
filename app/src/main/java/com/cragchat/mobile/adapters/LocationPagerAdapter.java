package com.cragchat.mobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.fragments.MapFragment;

public class LocationPagerAdapter extends FragmentPagerAdapter {

    private int parentId;

    public LocationPagerAdapter(FragmentManager fm, int parentId) {
        super(fm);
        this.parentId = parentId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MapFragment.newInstance(parentId);
            case 1:
                return CommentSectionFragment.newInstance(parentId, "LOCATION");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Map";
            case 1:
                return "Text";
            default:
                return "NULL";
        }
    }
}