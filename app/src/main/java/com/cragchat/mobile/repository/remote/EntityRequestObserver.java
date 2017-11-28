package com.cragchat.mobile.repository.remote;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by timde on 10/22/2017.
 */

public abstract class EntityRequestObserver<T> implements Observer<T> {

    public abstract void onNext(T o);

    public abstract void onError(Throwable throwable);

    @Override
    public void onSubscribe(Disposable disposable) {
    }

    @Override
    public void onComplete() {

    }
}
