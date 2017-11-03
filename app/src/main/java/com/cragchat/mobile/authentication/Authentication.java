package com.cragchat.mobile.authentication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by timde on 9/26/2017.
 */

public class Authentication {

    public static final String AUTHENTICATION_PREFERENCE = "authentication";
    public static final String AUTHENTICATION_TOKEN = "token";
    public static final String AUTHENTICATION_USERNAME = "username";
    public static final String DEFAULT_NULL = "null";

    private static AuthenticationProvider mProvider;
    private static AuthenticatedUser mUser;

    public static void init(Context applicationContext) {
        mProvider = new CragChatRestAuthenticationProvider(applicationContext);
    }

    public static boolean isLoggedIn(Context context) {
        return getAuthenticatedUser(context) != null;
    }

    public static void logout(Context context) {
        mProvider.logout();
        setTokenAndUsername(context, DEFAULT_NULL, DEFAULT_NULL);
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
            public void onAuthenticateFailed(String message) {
                if (callback != null) {
                    callback.onAuthenticateFailed(message);
                }
            }
        });
    }

    private static void setUser(Context context, AuthenticatedUser user) {
        mUser = user;
        setTokenAndUsername(context, user.getToken(), user.getName());
    }

    public static void register(final Context context, String username, String password, final AuthenticationCallback callback) {
        mProvider.register(username, password, new AuthenticationCallback() {
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

    public static AuthenticatedUser getAuthenticatedUser(Context context) {
        if (mUser == null) {
            String token = getSharedPreferences(context).getString(AUTHENTICATION_TOKEN, DEFAULT_NULL);
            String name = getSharedPreferences(context).getString(AUTHENTICATION_USERNAME, DEFAULT_NULL);
            System.out.println("gAU: " + token + " " + name);
            if (!token.equals(DEFAULT_NULL) && !name.equals(DEFAULT_NULL)) {
                mUser = new AuthenticatedUser(name, token);
            }
        }
        return mUser;
    }

    private static String getAuthenticationToken(Context context) {
        return getSharedPreferences(context).getString(AUTHENTICATION_TOKEN, DEFAULT_NULL);
    }

    private static void setTokenAndUsername(Context context, String token, String name) {
        SharedPreferences.Editor sharedPreference = getSharedPreferences(context).edit();
        sharedPreference.putString(AUTHENTICATION_TOKEN, token);
        sharedPreference.putString(AUTHENTICATION_USERNAME, name);
        sharedPreference.apply();
        System.out.println("now " + token + " " + name);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(AUTHENTICATION_PREFERENCE, Context.MODE_PRIVATE);
    }
}
