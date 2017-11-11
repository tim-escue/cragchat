package com.cragchat.mobile.authentication;

import android.content.Context;

import com.cragchat.mobile.network.Network;
import com.cragchat.mobile.repository.Repository;
import com.cragchat.mobile.repository.remote.RetroFitRestApi;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by timde on 10/20/2017.
 */

public class CragChatRestAuthenticationProvider implements AuthenticationProvider {

    private Context context;

    public CragChatRestAuthenticationProvider(Context context) {
        this.context = context;
    }

    @Override
    public void logIn(String name, String password, final AuthenticationCallback callback) {
        if (Network.isConnected(context)) {
            Repository.login(name, password)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<AuthenticatedUser>() {
                        @Override
                        public void accept(AuthenticatedUser authenticatedUser) throws Exception {
                            if (callback != null) {
                                if (authenticatedUser != null) {
                                    callback.onAuthenticateSuccess(authenticatedUser);
                                } else {
                                    callback.onAuthenticateFailed("Login failed for username-password combination");
                                }
                            }
                        }
                    });
        } else if (callback != null) {
            callback.onAuthenticateFailed("Cannot login while offline.");
        }
    }

    @Override
    public void register(String name, String password, AuthenticationCallback callback) {

    }

    @Override
    public void logout() {

    }
}
