package com.cragchat.mobile.repository;

/**
 * Created by timde on 11/13/2017.
 */

public interface Callback<T> {
    void onSuccess(T object);

    void onFailure();
}
