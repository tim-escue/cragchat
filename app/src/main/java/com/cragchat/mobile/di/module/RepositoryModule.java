package com.cragchat.mobile.di.module;

import android.content.Context;

import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.repository.local.CragChatDatabase;

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
    Repository provideRepository(Context context, CragChatDatabase database) {
        return new Repository(context, database);
    }
}
