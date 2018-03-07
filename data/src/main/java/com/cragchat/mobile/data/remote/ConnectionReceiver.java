package com.cragchat.mobile.data.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cragchat.mobile.data.authentication.Authentication;
import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.data.util.NetworkUtil;

/**
 * Created by timde on 11/28/2017.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    private static final int THIRTY_SECONDS = 30000;

    private static long lastTime;
    private Repository mrepository;
    private Authentication mAuthentication;

    public ConnectionReceiver(Repository repository, Authentication authentication) {
        this.mrepository = repository;
        this.mAuthentication = authentication;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtil.isConnected(context) && mAuthentication.isLoggedIn(context) && System.currentTimeMillis() - lastTime > THIRTY_SECONDS) {
            mrepository.sendQueuedRequests();
            lastTime = System.currentTimeMillis();
        }
    }
}
