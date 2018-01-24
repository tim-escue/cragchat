package com.cragchat.mobile.di.module;

import android.content.Context;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.remote.CragChatRestApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by timde on 1/24/2018.
 */

@Module
public class AuthenticationModule {

    @Singleton
    @Provides
    Authentication provideAuthentication(Context context, CragChatRestApi cragChatRestApi) {
        return new Authentication(context, cragChatRestApi);
    }

}
