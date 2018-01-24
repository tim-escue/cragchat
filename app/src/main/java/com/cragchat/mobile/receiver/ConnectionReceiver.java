package com.cragchat.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.util.NetworkUtil;

/**
 * Created by timde on 11/28/2017.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    private static long lastTime;
    private Repository mrepository;
    private Authentication mAuthentication;

    public ConnectionReceiver(Repository repository, Authentication authentication) {
        this.mrepository = repository;
        this.mAuthentication = authentication;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtil.isConnected(context) && mAuthentication.isLoggedIn(context) && System.currentTimeMillis() - lastTime > 15000) {
            Log.w("ConnectionRececeiver", "Triggering queue upload.");
            mrepository.sendQueuedRequests();
            lastTime = System.currentTimeMillis();
        }
    }
}
