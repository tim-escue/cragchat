package com.cragchat.mobile.adapters.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;

import com.cragchat.mobile.R;
import com.cragchat.mobile.fragments.CommentSectionFragment;
import com.cragchat.mobile.fragments.ImageFragment;
import com.cragchat.mobile.fragments.LocationFragment;
import com.cragchat.mobile.fragments.RatingFragment;
import com.cragchat.mobile.fragments.SendsFragment;
import com.cragchat.mobile.sql.LocalDatabase;

import java.util.HashMap;
import java.util.Map;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<PagerFragment> fragments;
    private int index;

    public TabPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);

        fragments = new SparseArray<>();
        index = 0;
    }

    public void addFragment(String title, Fragment fragment) {
        fragments.append(index++, new PagerFragment(title, fragment));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }

    private class PagerFragment {
        private String title;
        private Fragment fragment;

        PagerFragment(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }
}