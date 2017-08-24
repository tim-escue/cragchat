package com.cragchat.mobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.cragchat.mobile.fragments.*;

public class RouteActivityPagerAdapter extends FragmentPagerAdapter {

    private int parentId;
    private ImageFragment imageFragment;

    public RouteActivityPagerAdapter(FragmentManager fm, ImageFragment imageFragment, int parentId) {
        super(fm);
        this.parentId = parentId;
        this.imageFragment = imageFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CommentSectionFragment.newInstance(parentId, "BETA");
            case 1:
                return RatingFragment.newInstance(parentId);
            case 2:
                return SendsFragment.newInstance(parentId);
            case 3:
                return LocationFragment.newInstance(parentId);
            case 4:
                return imageFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Beta";
            case 1:
                return "Ratings";
            case 2:
                return "Sends";
            case 3:
                return "Location";
            case 4:
                return "Images";
            default:
                return "NULL";
        }
    }
}