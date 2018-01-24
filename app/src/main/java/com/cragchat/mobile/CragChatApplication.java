package com.cragchat.mobile;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.di.component.DaggerRepositoryComponent;
import com.cragchat.mobile.di.component.RepositoryComponent;
import com.cragchat.mobile.di.module.ApplicationModule;
import com.cragchat.mobile.di.module.RepositoryModule;
import com.cragchat.mobile.receiver.ConnectionReceiver;
import com.cragchat.mobile.repository.Repository;

import javax.inject.Inject;

/**
 * Created by timde on 9/28/2017.
 */

public class CragChatApplication extends Application {

    protected RepositoryComponent applicationComponent;

    @Inject
    Repository repository;

    @Inject
    Authentication authentication;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerRepositoryComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .repositoryModule(new RepositoryModule())
                .build();
        applicationComponent.inject(this);

        registerReceiver(new ConnectionReceiver(repository, authentication),
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public static CragChatApplication get(Context context) {
        return (CragChatApplication) context.getApplicationContext();
    }

    public RepositoryComponent getComponent() {
        return applicationComponent;
    }

}
