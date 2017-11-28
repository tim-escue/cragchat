package com.cragchat.mobile.authentication;

import android.content.Context;
import android.widget.Toast;

import com.cragchat.mobile.network.Network;
import com.cragchat.mobile.repository.remote.EntityRequestObserver;
import com.cragchat.mobile.repository.remote.RetroFitRestApi;
import com.google.gson.Gson;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by timde on 10/20/2017.
 */

public class CragChatRestAuthenticationProvider implements AuthenticationProvider {

    private Context context;

    public CragChatRestAuthenticationProvider(Context context) {
        this.context = context;
    }

    @Override
    public void logIn(final String name, final String password, final AuthenticationCallback callback) {
        if (Network.isConnected(context)) {
            RetroFitRestApi.getInstance().login(name, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new EntityRequestObserver<AuthenticatedUser>() {
                        @Override
                        public void onNext(AuthenticatedUser authenticatedUser) {
                            if (callback != null) {
                                if (authenticatedUser != null && authenticatedUser.getName() != null && authenticatedUser.getToken() != null) {
                                    callback.onAuthenticateSuccess(authenticatedUser);
                                } else {
                                    callback.onAuthenticateFailed("Login failed for username-password combination");
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Toast.makeText(context, "Error - Login failed", Toast.LENGTH_LONG).show();
                            throwable.printStackTrace();
                        }
                    });
        } else if (callback != null) {
            callback.onAuthenticateFailed("Cannot login while offline.");
        }
    }

    @Override
    public void register(String username, String password, String email, final AuthenticationCallback callback) {
        RetroFitRestApi.getInstance().register(username, password, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EntityRequestObserver<ResponseBody>() {
                               @Override
                               public void onNext(ResponseBody object) {
                                   String response = null;
                                   try {
                                       response = object.string();
                                       JSONObject jsonObject = new JSONObject(response);
                                       if (jsonObject.has("error") && jsonObject.has("message")) {
                                           if (callback != null) {
                                               callback.onAuthenticateFailed(jsonObject.getString("message"));
                                           }
                                           return;
                                       }
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                                   Gson gson = new Gson();
                                   AuthenticatedUser user = gson.fromJson(response, AuthenticatedUser.class);
                                   boolean registered = user != null && user.getToken() != null && user.getName() != null;
                                   if (callback != null) {
                                       if (registered) {
                                           callback.onAuthenticateSuccess(user);
                                       } else {
                                           callback.onAuthenticateFailed("unspecified");
                                       }
                                   }
                               }

                               @Override
                               public void onError(Throwable throwable) {
                                   Toast.makeText(context, "Error - Registration failed", Toast.LENGTH_LONG).show();
                                   throwable.printStackTrace();
                               }
                           }
                );
    }

    @Override
    public void logout() {

    }
}
