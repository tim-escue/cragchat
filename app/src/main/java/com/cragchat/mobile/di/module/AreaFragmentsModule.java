package com.cragchat.mobile.di.module;

import android.location.Location;

import com.cragchat.mobile.di.FragmentScoped;
import com.cragchat.mobile.ui.view.fragments.AreaListFragment;
import com.cragchat.mobile.ui.view.fragments.LocationFragment;
import com.cragchat.mobile.ui.view.fragments.RecentActivityFragment;
import com.cragchat.mobile.ui.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.ui.view.fragments.ImageFragment;
import com.cragchat.mobile.ui.view.fragments.RouteListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by timde on 2/6/2018.
 */

@Module
public abstract class AreaFragmentsModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RecentActivityFragment recentActivityFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AreaListFragment areaListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CommentSectionFragment commentSectionFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ImageFragment imageFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RouteListFragment routeListFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LocationFragment locationFragment();

}