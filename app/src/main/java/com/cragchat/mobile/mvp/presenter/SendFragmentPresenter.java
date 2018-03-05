package com.cragchat.mobile.mvp.presenter;

import com.cragchat.mobile.data.Repository;
import com.cragchat.mobile.mvp.contract.SendsContract;

public class SendFragmentPresenter {

    private SendsContract.SendView mView;
    private Repository mRepository;

    public SendFragmentPresenter(SendsContract.SendView view, Repository repository) {
        this.mView = view;
        this.mRepository = repository;
    }

    public void loadSends(String entityKey) {
        mRepository.observeSends(entityKey).subscribe(sends -> mView.show(sends));
    }
}