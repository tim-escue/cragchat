package com.cragchat.mobile.di.module;

import android.app.Application;
import android.content.Context;

import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.local.RealmDatabase;
import com.cragchat.mobile.repository.remote.CragChatRestApi;
import com.cragchat.mobile.repository.remote.RetroFitRestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return mApplication;
    }

    @Singleton
    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    CragChatDatabase provideCragChatDatabase() {
        return new RealmDatabase(mApplication);
    }

    @Singleton
    @Provides
    CragChatRestApi provideCragChatRestApi() {
        return new RetroFitRestApi();
    }
}