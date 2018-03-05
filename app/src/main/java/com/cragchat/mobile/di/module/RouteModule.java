package com.cragchat.mobile.di.module;

import com.cragchat.mobile.di.FragmentScoped;
import com.cragchat.mobile.di.InjectionNames;
import com.cragchat.mobile.mvp.view.fragments.LocationFragment;
import com.cragchat.mobile.mvp.view.fragments.RatingFragment;
import com.cragchat.mobile.domain.model.Route;
import com.cragchat.mobile.mvp.view.activity.RouteActivity;
import com.cragchat.mobile.mvp.view.fragments.CommentSectionFragment;
import com.cragchat.mobile.mvp.view.fragments.ImageFragment;
import com.cragchat.mobile.mvp.view.fragments.SendsFragment;
import com.cragchat.mobile.util.ViewUtil;

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
        return activity.getIntent().getParcelableExtra(ViewUtil.ENTITY);
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
