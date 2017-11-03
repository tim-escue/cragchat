package com.cragchat.networkapi;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by timde on 10/22/2017.
 */

public abstract class ErrorHandlingObserverable<T> implements Observer<T> {

    public abstract void onSuccess(T object);

    @Override
    public void onSubscribe(Disposable disposable) {

    }

    @Override
    public void onNext(T o) {
        onSuccess(o);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Caught error");
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
