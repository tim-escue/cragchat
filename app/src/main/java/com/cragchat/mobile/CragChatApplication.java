package com.cragchat.mobile;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.di.component.ApplicationComponent;
import com.cragchat.mobile.di.component.DaggerApplicationComponent;
import com.cragchat.mobile.di.module.ApplicationModule;
import com.cragchat.mobile.receiver.ConnectionReceiver;
import com.cragchat.mobile.repository.Repository;

import javax.inject.Inject;

/**
 * Created by timde on 9/28/2017.
 */

public class CragChatApplication extends Application {

    protected ApplicationComponent applicationComponent;

    @Inject
    Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);

        Authentication.init(this);
        registerReceiver(new ConnectionReceiver(),
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public static CragChatApplication get(Context context) {
        return (CragChatApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

}
