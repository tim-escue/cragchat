package com.cragchat.mobile.task;

/**
 * Created by timde on 10/26/2017.
 */

public interface TaskCallback<T> {
    void onResult(T result);
}
