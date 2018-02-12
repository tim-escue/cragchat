package com.cragchat.mobile.di.module;

import android.app.Application;
import android.content.Context;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.local.RealmDatabase;
import com.cragchat.mobile.repository.remote.CragChatRestApi;
import com.cragchat.mobile.repository.remote.RetroFitRestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Singleton
    @Provides
    CragChatDatabase provideCragChatDatabase(Application application) {
        return new RealmDatabase(application);
    }

    @Singleton
    @Provides
    CragChatRestApi provideCragChatRestApi() {
        return new RetroFitRestApi();
    }

    @Singleton
    @Provides
    Authentication provideAuthentication(Context context, CragChatRestApi cragChatRestApi) {
        return new Authentication(context, cragChatRestApi);
    }

    @Singleton
    @Provides
    Repository provideRepository(Context context, CragChatDatabase database,
                                 CragChatRestApi restApi, Authentication authentication) {
        return new Repository(context, database, restApi, authentication);
    }
}


