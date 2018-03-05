package com.cragchat.mobile.di.module;

import android.app.Application;
import android.content.Context;

import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.data.local.ClimbrLocalDatabase;
import com.cragchat.mobile.data.local.realm.RealmDatabase;
import com.cragchat.mobile.data.remote.ClimbrRemoteDatasource;
import com.cragchat.mobile.data.remote.retrofit.RetrofitRestAPI;

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
    ClimbrLocalDatabase provideCragChatDatabase(Application application) {
        return new RealmDatabase(application);
    }

    @Singleton
    @Provides
    ClimbrRemoteDatasource provideCragChatRestApi() {
        return new RetrofitRestAPI();
    }

    @Singleton
    @Provides
    Authentication provideAuthentication(Context context, ClimbrRemoteDatasource remoteDatasource) {
        return new Authentication(context, remoteDatasource);
    }

    @Singleton
    @Provides
    Repository provideRepository(Context context, ClimbrLocalDatabase database,
                                 ClimbrRemoteDatasource restApi, Authentication authentication) {
        return new Repository(context, database, restApi, authentication);
    }
}


