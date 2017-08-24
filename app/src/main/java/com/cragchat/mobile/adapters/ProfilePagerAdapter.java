package com.cragchat.mobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.cragchat.mobile.fragments.*;

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    private String username;

    public ProfilePagerAdapter(FragmentManager fm, String username) {
        super(fm);
        this.username = username;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProfileCommentsFragment.newInstance(username);
            case 1:
                return ProfileRatingsFragment.newInstance(username);
            case 2:
                return ProfileImagesFragment.newInstance(username);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Comments";
            case 1:
                return "Ratings";
            case 2:
                return "Images";
            default:
                return "NULL";
        }
    }
}