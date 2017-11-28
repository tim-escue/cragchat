package com.cragchat.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.network.Network;
import com.cragchat.mobile.repository.Repository;

/**
 * Created by timde on 11/28/2017.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    private static long lastTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Network.isConnected(context) && Authentication.isLoggedIn(context) && System.currentTimeMillis() - lastTime > 15000) {
            Log.w("ConnectionRececeiver", "Triggering queue upload.");
            Repository.sendQueuedRequests();
            lastTime = System.currentTimeMillis();
        }
    }
}
