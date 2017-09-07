package com.cragchat.mobile.adapters.pager;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

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
    private FloatingActionButton fab;

    public TabPagerAdapter(Context context, FragmentManager fragmentManager, AppBarLayout appBar, FloatingActionButton fab) {
        super(fragmentManager);
        fragments = new SparseArray<>();
        index = 0;
        this.appBarLayout = appBar;
        this.fab = fab;
    }

    public void addFragment(String title, Fragment fragment, boolean alwaysShowAppBar, boolean showFloatingActionButton) {
        fragments.append(index++, new PagerFragment(title, fragment, alwaysShowAppBar, showFloatingActionButton));
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
        toggleOptionalViews(position);
    }

    @Override
    public void onPageSelected(int position) {
        toggleOptionalViews(position);
    }

    private void toggleOptionalViews(int position) {
        PagerFragment fragment = fragments.get(position);
        if (fragment.getExpandAppBar()) {
            appBarLayout.setExpanded(true);
        }
        if (fragment.getShowFloatingActionButton()) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class PagerFragment {
        private String title;
        private Fragment fragment;
        private boolean expandAppBar;
        private boolean showFloatingActionButton;

        PagerFragment(String title, Fragment fragment, boolean expandAppBar, boolean showFloatingActionButton) {
            this.title = title;
            this.fragment = fragment;
            this.expandAppBar = expandAppBar;
            this.showFloatingActionButton = showFloatingActionButton;
        }

        public boolean getShowFloatingActionButton() {
            return showFloatingActionButton;
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