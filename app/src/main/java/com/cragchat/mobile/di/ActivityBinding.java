package com.cragchat.mobile.di;

import com.cragchat.mobile.di.module.RouteModule;
import com.cragchat.mobile.di.module.SearchModule;
import com.cragchat.mobile.mvp.view.activity.AreaActivity;
import com.cragchat.mobile.di.module.AreaFragmentsModule;
import com.cragchat.mobile.di.module.AreaModule;
import com.cragchat.mobile.mvp.view.activity.EditImageActivity;
import com.cragchat.mobile.mvp.view.activity.LoginActivity;
import com.cragchat.mobile.mvp.view.activity.MainActivity;
import com.cragchat.mobile.mvp.view.activity.MapActivity;
import com.cragchat.mobile.mvp.view.activity.ProfileActivity;
import com.cragchat.mobile.mvp.view.activity.RateRouteActivity;
import com.cragchat.mobile.mvp.view.activity.RegisterActivity;
import com.cragchat.mobile.mvp.view.activity.RouteActivity;
import com.cragchat.mobile.mvp.view.activity.SearchActivity;
import com.cragchat.mobile.mvp.view.activity.SubmitSendActivity;
import com.cragchat.mobile.mvp.view.activity.ViewImageActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBinding {

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AreaModule.class, AreaFragmentsModule.class})
    abstract AreaActivity areaActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract EditImageActivity editImageActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ProfileActivity profileActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RateRouteActivity rateRouteActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract RegisterActivity registerActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = RouteModule.class)
    abstract RouteActivity routeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SearchModule.class)
    abstract SearchActivity searchActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SubmitSendActivity submitSendActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ViewImageActivity viewImageActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MapActivity mapActivity();
}