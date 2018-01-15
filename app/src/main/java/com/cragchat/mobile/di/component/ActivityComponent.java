package com.cragchat.mobile.di.component;

import android.app.Activity;

import com.cragchat.mobile.di.IndividualActivity;
import com.cragchat.mobile.di.module.ActivityModule;

import dagger.Component;

@IndividualActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(Activity activity);

}