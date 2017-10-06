package com.cragchat.mobile.authentication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by timde on 9/26/2017.
 */

public class Authentication {

    public static final String AUTHENTICATION_PREFERENCE = "authentication";
    public static final String AUTHENTICATION_TOKEN = "token";
    public static final String DEFAULT_NULL = "null";

    private static AuthenticationProvider mProvider;
    private static AuthenticatedUser mUser;

    static {
        mProvider = new RealmAuthenticationProvider();
    }

    public static boolean isLoggedIn(Context context) {
        String token = getAuthenticationToken(context);
        if (!token.equals(DEFAULT_NULL)) {
            mUser = mProvider.getAuthenticatedUser(token);
            return true;
        } else {
            return false;
        }
    }

    public static void logout(Context context) {
        mProvider.logout();
        setToken(context, DEFAULT_NULL);
        mUser = null;
    }

    public static void login(final Context context, String name, String password, final AuthenticationCallback callback) {
        mProvider.logIn(name, password, new AuthenticationCallback() {
           @Override
           public void onAuthenticateSuccess(AuthenticatedUser justLoggedIn) {
               setUser(context, justLoggedIn);
               if (callback != null) {
                   callback.onAuthenticateSuccess(justLoggedIn);
               }
           }

           @Override
           public void onAuthenticateFailed() {
               if (callback != null) {
                   callback.onAuthenticateFailed();
               }
           }
       });
    }

    private static void setUser(Context context, AuthenticatedUser user) {
        mUser = user;
        setToken(context, user.getToken());
    }

    public static void register(final Context context, String username, String password, final AuthenticationCallback callback) {
        mProvider.register(username, password,new AuthenticationCallback() {
            @Override
            public void onAuthenticateSuccess(AuthenticatedUser justLoggedIn) {
                setUser(context, justLoggedIn);
                if (callback != null) {
                    callback.onAuthenticateSuccess(justLoggedIn);
                }
            }

            @Override
            public void onAuthenticateFailed() {
                if (callback != null) {
                    callback.onAuthenticateFailed();
                }
            }
        });
    }

    public static AuthenticatedUser getAuthenticatedUser(Context context) {
        if (mUser == null) {
            return mProvider.getAuthenticatedUser(getAuthenticationToken(context));
        } else {
            return mUser;
        }
    }

    private static String getAuthenticationToken(Context context) {
        return getSharedPreferences(context).getString(AUTHENTICATION_TOKEN, DEFAULT_NULL);
    }

    private static void setToken(Context context, String token) {
        SharedPreferences.Editor sharedPreference = getSharedPreferences(context).edit();
        sharedPreference.putString(AUTHENTICATION_TOKEN, token);
        sharedPreference.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(AUTHENTICATION_PREFERENCE, Context.MODE_PRIVATE);
    }
}
