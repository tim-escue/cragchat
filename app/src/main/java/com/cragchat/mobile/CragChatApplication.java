package com.cragchat.mobile;

import android.app.Application;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Repository;

/**
 * Created by timde on 9/28/2017.
 */

public class CragChatApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Repository.init(this);
        Authentication.init(getApplicationContext());
        //  DaggerCragChatApplicationComponent.create().inject(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
