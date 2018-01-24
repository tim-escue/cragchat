package com.cragchat.mobile.di.module;

import android.app.Application;
import android.content.Context;

import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.local.RealmDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    CragChatDatabase provideCragChatDatabase() {
        return new RealmDatabase(mApplication);
    }
}