package com.cragchat.mobile.di.module;

import com.cragchat.mobile.di.FragmentScoped;
import com.cragchat.mobile.ui.view.activity.MainActivity;
import com.cragchat.mobile.ui.view.fragments.CragsFragment;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by timde on 1/31/2018.
 */

@Module
public abstract class MainActivityModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CragsFragment cragsFragment();

    @Provides
    static String provideMessage() {
        return "ABLJEEFECEExxxx";
    }


}