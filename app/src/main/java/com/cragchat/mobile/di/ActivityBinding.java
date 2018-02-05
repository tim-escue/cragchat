package com.cragchat.mobile.di;

import com.cragchat.mobile.di.module.MainActivityModule;
import com.cragchat.mobile.di.module.PlaceHolderModule;
import com.cragchat.mobile.features.area.AreaActivity;
import com.cragchat.mobile.features.area.AreaModule;
import com.cragchat.mobile.ui.view.activity.EditImageActivity;
import com.cragchat.mobile.ui.view.activity.LoginActivity;
import com.cragchat.mobile.ui.view.activity.MainActivity;
import com.cragchat.mobile.ui.view.activity.ProfileActivity;
import com.cragchat.mobile.ui.view.activity.RateRouteActivity;
import com.cragchat.mobile.ui.view.activity.RegisterActivity;
import com.cragchat.mobile.ui.view.activity.RouteActivity;
import com.cragchat.mobile.ui.view.activity.SearchActivity;
import com.cragchat.mobile.ui.view.activity.SubmitSendActivity;
import com.cragchat.mobile.ui.view.activity.ViewImageActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBinding {

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AreaModule.class)
    abstract AreaActivity areaActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract EditImageActivity editImageActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract ProfileActivity profileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract RateRouteActivity rateRouteActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract RegisterActivity registerActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract RouteActivity routeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract SearchActivity searchActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract SubmitSendActivity submitSendActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PlaceHolderModule.class)
    abstract ViewImageActivity viewImageActivity();


}