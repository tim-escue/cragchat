package com.cragchat.mobile.data.authentication;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
