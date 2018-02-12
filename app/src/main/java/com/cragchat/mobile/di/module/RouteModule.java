package com.cragchat.mobile.di.module;

import com.cragchat.mobile.di.FragmentScoped;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.ui.view.fragments.LocationFragment;
import com.cragchat.mobile.ui.view.fragments.RatingFragment;
import com.cragchat.mobile.ui.view.fragments.RecentActivityFragment;
import com.cragchat.mobile.ui.model.Route;
import com.cragchat.mobile.ui.view.activity.RouteActivity;
import com.cragchat.mobile.ui.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.ui.view.fragments.ImageFragment;
import com.cragchat.mobile.ui.view.fragments.SendsFragment;
import com.cragchat.mobile.util.NavigationUtil;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by timde on 2/5/2018.
 */

@Module
public abstract class RouteModule {

    @Provides
    static Route provideRoute(RouteActivity activity) {
        return activity.getIntent().getParcelableExtra(NavigationUtil.ENTITY);
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SendsFragment sendsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CommentSectionFragment commentSectionFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ImageFragment imageFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RatingFragment ratingFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LocationFragment locationFragment();

    @Provides
    @Named(InjectionNames.ENTITY_KEY)
    static String entityKey(Route route) {
        return route.getKey();
    }


}
