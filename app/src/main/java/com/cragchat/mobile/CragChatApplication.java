package com.cragchat.mobile;

import android.app.Application;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.database.RealmDatabase;

import io.realm.Realm;

/**
 * Created by timde on 9/28/2017.
 */

public class CragChatApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmDatabase.init();
        Authentication.init(getApplicationContext());
        //  DaggerCragChatApplicationComponent.create().inject(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RealmDatabase.close();
    }
}
