package com.cragchat.mobile.authentication;

/**
 * Created by timde on 9/26/2017.
 */

public class AuthenticatedUser {

    private String name;
    private String token;

    public AuthenticatedUser(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
