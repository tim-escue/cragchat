package com.cragchat.mobile.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import com.cragchat.mobile.repository.remote.CragChatRestApi;

import javax.inject.Inject;

/**
 * Created by timde on 9/26/2017.
 */

public class Authentication {

    public final String AUTHENTICATION_PREFERENCE = "authentication";
    public final String AUTHENTICATION_TOKEN = "token";
    public final String AUTHENTICATION_USERNAME = "username";
    public final String DEFAULT_NULL = "null";

    private AuthenticationProvider mProvider;
    private AuthenticatedUser mUser;

    @Inject
    public Authentication(Context applicationContext, CragChatRestApi cragChatRestApi) {
        mProvider = new CragChatRestAuthenticationProvider(applicationContext, cragChatRestApi);
    }

    public boolean isLoggedIn(Context context) {
        return getAuthenticatedUser(context) != null;
    }

    public void logout(Context context) {
        mProvider.logout();
        setTokenAndUsername(context, DEFAULT_NULL, DEFAULT_NULL);
        mUser = null;
    }

    public void login(final Context context, String name, String password, final AuthenticationCallback callback) {
        mProvider.logIn(name, password, new AuthenticationCallback() {
            @Override
            public void onAuthenticateSuccess(AuthenticatedUser justLoggedIn) {
                setUser(context, justLoggedIn);
                if (callback != null) {
                    callback.onAuthenticateSuccess(justLoggedIn);
                }

            }

            @Override
            public void onAuthenticateFailed(String message) {
                if (callback != null) {
                    callback.onAuthenticateFailed(message);
                }
            }
        });
    }

    private void setUser(Context context, AuthenticatedUser user) {
        mUser = user;
        setTokenAndUsername(context, user.getToken(), user.getName());
    }

    public void register(final Context context, String username, String password, String email,
                         final boolean autoLogin, final AuthenticationCallback callback) {
        mProvider.register(username, password, email, new AuthenticationCallback() {
            @Override
            public void onAuthenticateSuccess(AuthenticatedUser justLoggedIn) {
                if (autoLogin) {
                    setUser(context, justLoggedIn);
                }
                if (callback != null) {
                    callback.onAuthenticateSuccess(justLoggedIn);
                }
            }

            @Override
            public void onAuthenticateFailed(String message) {
                if (callback != null) {
                    callback.onAuthenticateFailed(message);
                }
            }
        });
    }

    public AuthenticatedUser getAuthenticatedUser(Context context) {
        if (mUser == null) {
            String token = getSharedPreferences(context).getString(AUTHENTICATION_TOKEN, DEFAULT_NULL);
            String name = getSharedPreferences(context).getString(AUTHENTICATION_USERNAME, DEFAULT_NULL);
            if (!token.equals(DEFAULT_NULL) && !name.equals(DEFAULT_NULL)) {
                mUser = new AuthenticatedUser(name, token);
            }
        }
        return mUser;
    }

    private String getAuthenticationToken(Context context) {
        return getSharedPreferences(context).getString(AUTHENTICATION_TOKEN, DEFAULT_NULL);
    }

    private void setTokenAndUsername(Context context, String token, String name) {
        SharedPreferences.Editor sharedPreference = getSharedPreferences(context).edit();
        sharedPreference.putString(AUTHENTICATION_TOKEN, token);
        sharedPreference.putString(AUTHENTICATION_USERNAME, name);
        sharedPreference.apply();
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(AUTHENTICATION_PREFERENCE, Context.MODE_PRIVATE);
    }
}
