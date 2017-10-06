package com.cragchat.mobile;

import android.app.Activity;
import android.app.Application;

import com.cragchat.mobile.database.Database;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by timde on 9/28/2017.
 */

public class CragChatApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Database.init(this);
      //  DaggerCragChatApplicationComponent.create().inject(this);
    }


}
