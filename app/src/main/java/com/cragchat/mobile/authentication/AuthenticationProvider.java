package com.cragchat.mobile.authentication;

/**
 * Created by timde on 9/26/2017.
 */

public interface AuthenticationProvider {

    void logIn(String name, String password, AuthenticationCallback callback);

    void register(String name, String password, AuthenticationCallback callback);

    void logout();

    AuthenticatedUser getAuthenticatedUser(String token);

}
