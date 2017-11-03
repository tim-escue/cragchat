package com.cragchat.mobile.authentication;

/**
 * Created by timde on 9/26/2017.
 */

public interface AuthenticationCallback {

    void onAuthenticateSuccess(AuthenticatedUser user);

    void onAuthenticateFailed(String message);

}
