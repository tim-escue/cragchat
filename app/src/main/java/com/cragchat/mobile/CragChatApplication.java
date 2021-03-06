package com.cragchat.mobile;

import android.content.Context;
import android.content.IntentFilter;

import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.di.component.DaggerApplicationComponent;
import com.cragchat.mobile.data.remote.ConnectionReceiver;
import com.cragchat.mobile.data.Repository;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * Created by timde on 9/28/2017.
 */

public class CragChatApplication extends DaggerApplication {

    @Inject
    Repository repository;

    @Inject
    Authentication authentication;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }

    public Repository getRepository() {
        return repository;
    }

    public Authentication authentication() {
        return authentication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent.builder().application(this).build().inject(this);
        registerReceiver(new ConnectionReceiver(repository, authentication),
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public static CragChatApplication get(Context context) {
        return (CragChatApplication) context.getApplicationContext();
    }

}
