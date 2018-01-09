package com.cragchat.mobile;

import android.app.Application;
import android.content.IntentFilter;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.receiver.ConnectionReceiver;
import com.cragchat.mobile.repository.Repository;

/**
 * Created by timde on 9/28/2017.
 */

public class CragChatApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.init(this);
        Authentication.init(this);
        registerReceiver(new ConnectionReceiver(),
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

}
