package com.cragchat.mobile.di.module;

import android.content.Context;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.remote.CragChatRestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by timde on 1/24/2018.
 */

@Module
public class RepositoryModule {

    @Singleton
    @Provides
    Repository provideRepository(Context context, CragChatDatabase database,
                                 CragChatRestApi restApi, Authentication authentication) {
        return new Repository(context, database, restApi, authentication);
    }

}
