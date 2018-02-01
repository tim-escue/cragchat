package com.cragchat.mobile.di.component;

import android.app.Application;

import com.cragchat.mobile.CragChatApplication;
import com.cragchat.mobile.di.ActivityBinding;
import com.cragchat.mobile.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityBinding.class})
public interface ApplicationComponent extends AndroidInjector<CragChatApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        ApplicationComponent.Builder application(Application application);

        ApplicationComponent build();
    }

    void inject(CragChatApplication app);
}