package com.cragchat.mobile.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

public class TabPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private SparseArray<PagerFragment> fragments;
    private int index;
    private AppBarLayout appBarLayout;

    public TabPagerAdapter(Context context, FragmentManager fragmentManager, AppBarLayout appBar) {
        super(fragmentManager);
        fragments = new SparseArray<>();
        index = 0;
        this.appBarLayout = appBar;
    }

    public void addFragment(String title, Fragment fragment, boolean alwaysShowAppBar) {
        fragments.append(index++, new PagerFragment(title, fragment, alwaysShowAppBar));
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
expandIfNecessary(position);
    }

    @Override
    public void onPageSelected(int position) {
        expandIfNecessary(position);
    }

    private void expandIfNecessary(int position) {
        Log.d("HUH", "OK");
        if (fragments.get(position).getExpandAppBar()) {
            Log.d("HUdfadfdf", "OK");
            appBarLayout.setExpanded(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class PagerFragment {
        private String title;
        private Fragment fragment;
        private boolean expandAppBar;

        PagerFragment(String title, Fragment fragment, boolean expandAppBar) {
            this.title = title;
            this.fragment = fragment;
            this.expandAppBar = expandAppBar;
        }

        public boolean getExpandAppBar() {
            return expandAppBar;
        }

        public String getTitle() {
            return title;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }
}