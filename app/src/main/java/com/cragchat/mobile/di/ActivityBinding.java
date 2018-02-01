package com.cragchat.mobile.di;

import com.cragchat.mobile.di.module.MainActivityModule;
import com.cragchat.mobile.ui.view.activity.MainActivity;
import com.cragchat.mobile.ui.view.fragments.CragsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBinding {

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();


}