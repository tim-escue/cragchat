package com.cragchat.mobile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.di.component.DaggerApplicationComponent;
import com.cragchat.mobile.receiver.ConnectionReceiver;
import com.cragchat.mobile.repository.Repository;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
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
        return  DaggerApplicationComponent.builder().application(this).build();
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
