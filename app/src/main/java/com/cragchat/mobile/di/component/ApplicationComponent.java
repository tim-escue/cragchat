package com.cragchat.mobile.di.component;

import android.app.Application;
import android.content.Context;

import com.cragchat.mobile.CragChatApplication;
import com.cragchat.mobile.di.ApplicationContext;
import com.cragchat.mobile.di.module.ApplicationModule;
import com.cragchat.mobile.repository.Repository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(CragChatApplication demoApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    Repository getRepository();

}